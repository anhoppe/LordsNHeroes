package com.lordhero.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Main extends ApplicationAdapter implements InputProcessor {
	    
	private WorldEditor _worldEditor;
	private WorldMap _worldMap;
	
	InputMultiplexer _inputMultiplexer;
    
	@Override
	public void create () {				
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        
        _worldMap = new WorldMap(0, 0, (int)w, (int)h);
		Gdx.input.setInputProcessor(this);
                
        _worldEditor = new WorldEditor(null);

        _worldMap.setCellSelector(_worldEditor);
        
		_inputMultiplexer = new InputMultiplexer();
		_inputMultiplexer.addProcessor(_worldEditor.getInputProcessor());
		_inputMultiplexer.addProcessor(this);

        Gdx.input.setInputProcessor(_inputMultiplexer);
    }

	@Override
	public void render () {
        _worldMap.render();
        _worldEditor.draw();        
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
}
