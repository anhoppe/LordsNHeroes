package com.lordhero.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.lordhero.game.controller.EntityController;
import com.lordhero.game.controller.IController;
import com.lordhero.game.controller.MapController;
import com.lordhero.game.model.Entities;
import com.lordhero.game.model.Map;
import com.lordhero.game.view.HeroSheet;
import com.lordhero.game.view.LordSheet;
import com.lordhero.game.view.MainPanel;
import com.lordhero.game.view.NpcEditor;
import com.lordhero.game.view.NpcView;
import com.lordhero.game.view.WorldEditor;
import com.lordhero.game.view.WorldMap;

public class Main extends ApplicationAdapter implements InputProcessor, IGameMode {
	
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

	// Others...
	InputMultiplexer _inputMultiplexer;
    
	private List<IController> _controllers = new LinkedList<IController>();
	
	private IGameMode.GameMode _gameMode = IGameMode.GameMode.BuyTiles;
	
	private int _homePort;
	private int _visitorPort;
	
	public Main(String configPath) {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(configPath);

			// load a properties file
			prop.load(input);

			// get the property value and print it out
			_homePort = Integer.parseInt(prop.getProperty("homeport"));
			_visitorPort = Integer.parseInt(prop.getProperty("visitorport"));
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
		//////////////////////////////////////////////////////////////////
		// Instantiation
		
		// Create UI components
		_lordSheet = new LordSheet();
		_heroSheet = new HeroSheet();
        _worldEditor = new WorldEditor();
        _npcEditor = new NpcEditor();
        _npcView = new NpcView();
        _mainPanel = new MainPanel();

        // Create models
		_player = new Player();
		_map = new Map(_homePort, _visitorPort);
		
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
        _entities.setPlayer(_player);
       
        _map.setPlayer(_player);
        _map.setSelectedCellProvider(_worldEditor);
        _map.setGameMode(this);
        
		// Controller DI
        mapController.setMap(_map);
        mapController.setPlayer(_player);
        mapController.setGameMode(this);

        entityController.setEntities(_entities);
        entityController.setGameMode(this);       
		
		// View DI
		_lordSheet.setLord(_player);
		_lordSheet.setGameMode(this);
		_lordSheet.setMapController(mapController);

		_worldMap.setEntities(_entities);
        _worldMap.setMap(_map);
        _worldMap.setPlayer(_player);
        
        ///////////////////////////////////////////////////////////////////
        // set input multiplexer
		_inputMultiplexer = new InputMultiplexer();
		_inputMultiplexer.addProcessor(_worldEditor.getInputProcessor());
		_inputMultiplexer.addProcessor(_lordSheet.getInputProcessor());
		_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
		_inputMultiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(_inputMultiplexer);
    }

	@Override
	public void render () {

		for (IController controller : _controllers) {
			controller.update();
		}
		
		_worldMap.render();
        _mainPanel.draw();
        
        if (_gameMode == GameMode.BuyTiles) {
            _worldEditor.draw();                	
        }
        else if (_gameMode == GameMode.AddNpc) {
        	_npcEditor.draw();
        }
        else if (_gameMode == GameMode.SelectMapItems) {
        	_npcView.draw();
        }
        
        if (_gameMode == GameMode.Play) {
        	_heroSheet.draw();
        }
        else {
    		_lordSheet.draw();
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
		_entities.save();
	}	

	@Override
	public GameMode get() {
		return _gameMode;
	}

	@Override
	public void set(GameMode gameMode) {
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

		render();		
	}
}
