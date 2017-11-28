package com.lordhero.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public class Main extends ApplicationAdapter implements InputProcessor, IMenuSelector {
	
	private MainPanel _mainPanel;
	private LordSheet _lordSheet;
	private WorldEditor _worldEditor;
	private NpcEditor _npcEditor;
	private WorldMap _worldMap;
	private Player _player;
	
	InputMultiplexer _inputMultiplexer;
	
	private static final String WorldEditor = "Map editor";
	private static final String NpcEditor = "Npc editor";
	
	private String _currentLordMenu = WorldEditor; 
    
	@Override
	public void create () {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        
        _worldMap = new WorldMap(0, 0, (int)w, (int)h);
		Gdx.input.setInputProcessor(this);
    
		_player = new Player();
		_lordSheet = new LordSheet();
		_lordSheet.setLord(_player);
		_lordSheet.setMenuSelector(this);
		
        _worldEditor = new WorldEditor();
        _npcEditor = new NpcEditor();        
        
        _worldMap.setSelectedCellProvider(_worldEditor);
        _worldMap.setLord(_player);
        
        _mainPanel = new MainPanel();
        
		_inputMultiplexer = new InputMultiplexer();
		_inputMultiplexer.addProcessor(_worldEditor.getInputProcessor());
		_inputMultiplexer.addProcessor(_lordSheet.getInputProcessor());
		_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
		_inputMultiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(_inputMultiplexer);
    }

	@Override
	public void render () {
        _worldMap.render();
        _mainPanel.draw();
        
        if (_currentLordMenu == WorldEditor) {
            _worldEditor.draw();                	
        }
        else if (_currentLordMenu == NpcEditor) {
        	_npcEditor.draw();
        }
		_lordSheet.draw();
	}
	
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
    	if (keycode == Input.Keys.E)
    	{
    		_worldMap.enter();
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
    	_worldMap.setTile(screenX, screenY);
    	return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
    	_worldMap.setCursorPosition(screenX, screenY);        
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    
	@Override
	public void dispose () {
		_worldMap.dispose();
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
        }
        else if (_currentLordMenu == NpcEditor) {
    		_inputMultiplexer.addProcessor(_npcEditor.getInputProcessor());
    		_inputMultiplexer.addProcessor(_lordSheet.getInputProcessor());
    		_inputMultiplexer.addProcessor(_mainPanel.getInputProcessor());
    		_inputMultiplexer.addProcessor(this);
        }

		render();		
	}
}
