package com.lordhero.game;

import java.util.LinkedList;
import java.util.List;

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
import com.lordhero.game.view.LordSheet;
import com.lordhero.game.view.MainPanel;
import com.lordhero.game.view.NpcEditor;
import com.lordhero.game.view.NpcView;
import com.lordhero.game.view.WorldEditor;
import com.lordhero.game.view.WorldMap;

public class Main extends ApplicationAdapter implements InputProcessor, IMenuSelector, IGameMode {
	
	private MainPanel _mainPanel;
	private LordSheet _lordSheet;
	private WorldEditor _worldEditor;
	private NpcEditor _npcEditor;
	private NpcView _npcView;
	private WorldMap _worldMap;
	private Player _player;
	
	private Map _map;
	
	InputMultiplexer _inputMultiplexer;
	
	private static final String WorldEditor = "Edit map";
	private static final String NpcEditor = "Add npc";
	private static final String SelectMap = "Select";
	
	private String _currentLordMenu = WorldEditor; 
    
	private List<IController> _controllers = new LinkedList<IController>();
	
	private IGameMode.GameMode _gameMode = IGameMode.GameMode.BuyTiles;
	
	@Override
	public void create () {
		//////////////////////////////////////////////////////////////////
		// Instantiation
		
		// Create UI components
		_lordSheet = new LordSheet();		
        _worldEditor = new WorldEditor();
        _npcEditor = new NpcEditor();
        _npcView = new NpcView();
        _mainPanel = new MainPanel();

        // Create models
		_player = new Player();
		_map = new Map();
        Entities entities = new Entities();
        
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
        
        // UI DI
		_lordSheet.setLord(_player);
		_lordSheet.setMenuSelector(this);

		// Model DI
        entities.setMapInfo(_map);
        entities.setSelectedNpcProvider(_npcEditor);
        entities.setNpcSelectionReceiver(_npcView);
        entities.setPlayer(_player);
       
        _map.setPlayer(_player);
        _map.setSelectedCellProvider(_worldEditor);
        
		// Controller DI
        mapController.setMap(_map);
        mapController.setPlayer(_player);
        mapController.setGameMode(this);

        entityController.setEntities(entities);
        entityController.setGameMode(this);       
		
		// View DI
        _worldMap.setEntities(entities);
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
        
        if (_currentLordMenu == WorldEditor) {
            _worldEditor.draw();                	
        }
        else if (_currentLordMenu == NpcEditor) {
        	_npcEditor.draw();
        }
        else if (_currentLordMenu == SelectMap) {
        	_npcView.draw();
        }
        
		_lordSheet.draw();
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
	}

	@Override
	public void setSelection(String selection) {
		_currentLordMenu = selection;

		_inputMultiplexer.clear();
        if (_currentLordMenu == WorldEditor) {
    		_inputMultiplexer.addProcessor(_worldEditor.getInputProcessor());
    		_inputMultiplexer.addProcessor(_lordSheet.getInputProcessor());
    		_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
    		_inputMultiplexer.addProcessor(this);
    		_gameMode = IGameMode.GameMode.BuyTiles;
        }
        else if (_currentLordMenu == NpcEditor) {
    		_inputMultiplexer.addProcessor(_npcEditor.getInputProcessor());
    		_inputMultiplexer.addProcessor(_lordSheet.getInputProcessor());
    		_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
    		_inputMultiplexer.addProcessor(this);
    		_gameMode = IGameMode.GameMode.AddNpc;
        }
        else if (_currentLordMenu == SelectMap) {
    		_inputMultiplexer.addProcessor(_npcView.getInputProcessor());
    		_inputMultiplexer.addProcessor(_lordSheet.getInputProcessor());
    		_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
    		_inputMultiplexer.addProcessor(this);
    		_gameMode = IGameMode.GameMode.SelectMapItems;
        }

		render();		
	}
	

	@Override
	public GameMode get() {
		return _gameMode;
	}

}
