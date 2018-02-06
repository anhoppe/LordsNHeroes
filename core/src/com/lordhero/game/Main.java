package com.lordhero.game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.INetwork.ConnectionType;
import com.lordhero.game.controller.EntityController;
import com.lordhero.game.controller.IController;
import com.lordhero.game.controller.MapController;
import com.lordhero.game.model.Entities;
import com.lordhero.game.model.IEntity;
import com.lordhero.game.model.INpc;
import com.lordhero.game.model.Map;
import com.lordhero.game.model.Player;
import com.lordhero.game.view.CharacterSheet;
import com.lordhero.game.view.HeroSheet;
import com.lordhero.game.view.LordSheet;
import com.lordhero.game.view.MainPanel;
import com.lordhero.game.view.MonsterPitPanel;
import com.lordhero.game.view.NpcEditor;
import com.lordhero.game.view.NpcView;
import com.lordhero.game.view.PurchaseSheet;
import com.lordhero.game.view.WorldEditor;
import com.lordhero.game.view.WorldMap;

public class Main extends ApplicationAdapter implements InputProcessor, IGameMode, IGameSourceProvider {
	 private static final java.util.Map<IGameMode.SaveType, String> SaveTypeFolder;
	    static {
	        java.util.Map<IGameMode.SaveType, String> aMap = new HashMap<IGameMode.SaveType, String>();
	        aMap.put(SaveType.None, "illegal");
	        aMap.put(SaveType.Map, "maps");
	        aMap.put(SaveType.Entities, "");
	        SaveTypeFolder = Collections.unmodifiableMap(aMap);
	    }
	    
//	private static final String[] SaveTypeFolder = {"illegal", "map", "entities"};
	
	// Model objects
	private Player _player;
	private Map _map;
	private Entities _entities;
	
	// Controller objects
	
	// View objects
	private MainPanel _mainPanel;
	private LordSheet _lordSheet;
	private WorldEditor _worldEditor;
	private NpcEditor _npcEditor;
	private NpcView _npcView;
	private WorldMap _worldMap;	
	private HeroSheet _heroSheet;
	private PurchaseSheet _purchaseSheet;
	private CharacterSheet _characterSheet;
	private MonsterPitPanel _monsterPitPanel;

	// Others...
	InputMultiplexer _inputMultiplexer;
    
	private List<IController> _controllers = new LinkedList<IController>();
	
	private IGameMode.GameMode _gameMode = IGameMode.GameMode.None;
	
	private INetwork _network;
	private String _worldName;
	
	public Main(String configPath) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(configPath);

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			int homePort = Integer.parseInt(prop.getProperty("homeport"));
			int visitorPort = Integer.parseInt(prop.getProperty("visitorport"));
			_network = new Network(this, homePort, visitorPort);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void create () {
		
		// 1st thing is connecting to the local server
		_network.connectToServer(ConnectionType.Local);

		//////////////////////////////////////////////////////////////////
		// Instantiation
		
		// Create UI components
		_monsterPitPanel = new MonsterPitPanel();
		_characterSheet = new CharacterSheet();
		_purchaseSheet = new PurchaseSheet();
		_lordSheet = new LordSheet();
		_heroSheet = new HeroSheet();
        _worldEditor = new WorldEditor();
        _npcEditor = new NpcEditor();
        _npcView = new NpcView();
        _mainPanel = new MainPanel();

        // Create models
		_player = new Player();
		_map = new Map();
		
        _entities = new Entities();
        
		// Create controllers
        EntityController entityController = new EntityController();
        MapController mapController = new MapController();

        _controllers.add(entityController);
        _controllers.add(mapController);

		// Create views
		float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        
        _worldMap = new WorldMap((int)w, (int)h);
        
        ///////////////////////////////////////////////////////////////////
        // Poor man's DI
        
		// Model DI
        _entities.setMapInfo(_map);
        _entities.setSelectedNpcProvider(_npcEditor);
        _entities.setNpcSelectionReceiver(_npcView);
       
        _map.setPlayer(_player);
        _map.setSelectedCellProvider(_worldEditor);
        _map.setGameMode(this);               
        _map.setGameSourceProvider(this);
        
		// Controller DI
        mapController.setMap(_map);
        mapController.setPlayer(_player);
        mapController.setEntities(_entities);
        mapController.setGameMode(this);
        mapController.setNetwork(_network);        
        
        entityController.setEntities(_entities);
        entityController.setGameMode(this);       
		entityController.setPlayer(_player);
		
		// View DI
		_lordSheet.setLord(_player);
		_lordSheet.setGameMode(this);
		_lordSheet.setGameSourceProvider(this);

		_heroSheet.setHero(_player);
		_heroSheet.setGameSourceProvider(this);
		
		_worldMap.setEntities(_entities);
        _worldMap.setMap(_map);
        _worldMap.setPlayer(_player);
        _worldMap.setGameMode(this);
        
        _purchaseSheet.setGameMode(this);
        _purchaseSheet.setPlayer(_player);
       
        _characterSheet.setGameMode(this);
        _characterSheet.setPlayer(_player);
        
        ///////////////////////////////////////////////////////////////////
        // Required initialization
		Path path = Paths.get(getSaveFolder(SaveType.Entities).toString(), "player.xml");
		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(path.toString());
			XmlReader reader = new XmlReader();
			XmlReader.Element root = reader.parse(inputStream);
			_player.read(root);
			inputStream.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        _map.loadFromRemote(_network);
        try {
			_entities.loadFromRemote(_network);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		}
        
        ///////////////////////////////////////////////////////////////////
        // set input multiplexer
		_inputMultiplexer = new InputMultiplexer();
		
		set(GameMode.BuyTiles, null);

        Gdx.input.setInputProcessor(_inputMultiplexer);
    }

	@Override
	public void render () {
		for (IController controller : _controllers) {
			controller.update();
		}
		
		_worldMap.render();
        
        if (_gameMode == GameMode.Play) {
        	_heroSheet.draw();
        }
        else if (_gameMode == GameMode.Conversation) {
        	_purchaseSheet.draw();
        }
        else if (_gameMode == GameMode.CharacterSheet) {
        	_characterSheet.draw();
        }
        else {
            _mainPanel.draw();
    		_lordSheet.draw();

            if (_gameMode == GameMode.BuyTiles) {
                _worldEditor.draw();
            }
            else if (_gameMode == GameMode.AddNpc) {
            	_npcEditor.draw();
            }
            else if (_gameMode == GameMode.MonsterPit) {
            	_monsterPitPanel.draw();
            }
            else if (_gameMode == GameMode.SelectMapItems) {
            	_npcView.draw();
            }
        }
	}
	
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {    	
		for (IController controller : _controllers) {
			if (controller.processKeyUp(keycode)) {
				break;
			}
		}
    	
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		for (IController controller : _controllers) {
			if (controller.processMouseDown(screenX, screenY, _map.getCursorX(), _map.getCursorY())) {
				break;
			}
		}

		return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (IController controller : _controllers) {
			if (controller.processMouseUp(screenX, screenY, _map.getCursorX(), _map.getCursorY())) {
				break;
			}
		}

		return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
		for (IController controller : _controllers) {
			if (controller.processMouseMove(screenX, screenY)) {
				break;
			}
		}
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
	@Override
	public void dispose () {
		_worldMap.dispose();
		_map.dispose();
		
		try {
			Path path = Paths.get(getSaveFolder(SaveType.Entities).toString(), "entities.xml");
			FileOutputStream outputStream = new FileOutputStream(path.toString());
			XmlWriter writer = new XmlWriter(new OutputStreamWriter(outputStream));
			_entities.save(writer);		
			writer.flush();
			outputStream.flush();
			outputStream.close();

			
			path = Paths.get(getSaveFolder(SaveType.Entities).toString(), "player.xml");
			outputStream = new FileOutputStream(path.toString());
			writer = new XmlWriter(new OutputStreamWriter(outputStream));
			_player.write(writer);
			writer.flush();
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
	}	

	@Override
	public GameMode get() {
		return _gameMode;
	}
	
	@Override
	public boolean is(GameMode gameMode) {
		return _gameMode == gameMode;
	}

	@Override
	public void set(GameMode gameMode, IEntity entity) {
		_gameMode = gameMode;
		
		_inputMultiplexer.clear();
        if (_gameMode == GameMode.BuyTiles) {
    		_inputMultiplexer.addProcessor(_worldEditor.getInputProcessor());
    		_inputMultiplexer.addProcessor(_lordSheet.getInputProcessor());
    		_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
    		_inputMultiplexer.addProcessor(this);
        }
        else if (_gameMode == GameMode.AddNpc) {
    		_inputMultiplexer.addProcessor(_npcEditor.getInputProcessor());
    		_inputMultiplexer.addProcessor(_lordSheet.getInputProcessor());
    		_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
    		_inputMultiplexer.addProcessor(this);
        }
        else if (_gameMode == GameMode.MonsterPit) {
        	_inputMultiplexer.addProcessor(_monsterPitPanel.getInputProcessor());
    		_inputMultiplexer.addProcessor(_lordSheet.getInputProcessor());
    		_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
    		_inputMultiplexer.addProcessor(this);        	
        }
        else if (_gameMode == GameMode.SelectMapItems) {
    		_inputMultiplexer.addProcessor(_npcView.getInputProcessor());
    		_inputMultiplexer.addProcessor(_lordSheet.getInputProcessor());
    		_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
    		_inputMultiplexer.addProcessor(this);
        }
        else if (_gameMode == GameMode.Play) {
        	_inputMultiplexer.addProcessor(_heroSheet.getInputProcessor());
        	_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
    		_inputMultiplexer.addProcessor(this);
        }
        else if (_gameMode == GameMode.Conversation) {
        	if (entity instanceof INpc) {
            	_purchaseSheet.setNpc((INpc)entity);        		
        	}        		
        	_inputMultiplexer.addProcessor(_purchaseSheet.getInputProcessor());        	
        }
        else if (_gameMode == GameMode.CharacterSheet) {
        	_characterSheet.update();
        	_inputMultiplexer.addProcessor(_characterSheet.getInputProcessor());
        }

		render();		
	}

	@Override
	public String getWorldName() {
		return _worldName;
	}

	@Override
	public void setWorldName(String worldName) {
		_worldName = worldName;		
	}
	
	@Override
	public void visitWorld() {
		_network.connectToServer(ConnectionType.Remote);
		
		set(GameMode.Play, null);

		_map.loadFromRemote(_network);
		try {
			_entities.loadFromRemote(_network);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void goHome() {
		_network.connectToServer(ConnectionType.Local);
		
		set(GameMode.BuyTiles, null);

		_map.loadFromRemote(_network);
		try {
			_entities.loadFromRemote(_network);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Path getSaveFolder(SaveType saveType) {
		return Paths.get(System.getProperty("user.home"), "Lords'n'Heroes", _worldName, SaveTypeFolder.get(saveType));
	}
}
