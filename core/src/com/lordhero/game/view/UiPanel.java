	package com.lordhero.game.view;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class UiPanel {
	protected Stage _stage;
	protected Skin _skin;
    protected Table _table;
	
	public UiPanel()
	{		
		this._stage  = new Stage();
		Gdx.input.setInputProcessor(_stage);

        _skin = new Skin(Gdx.files.internal("data/uiskin.json"), new TextureAtlas("data/uiskin.atlas"));
        _table = new Table();
        _stage.addActor(_table);
	}
	
	public void draw()
	{        
		_stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	    _stage.draw();
	    //Table.drawDebug(stage);
	}
	
	public void resize(int width, int height)
	{
		ScreenViewport vp = new ScreenViewport();
		vp.setScreenBounds(0, 0, width, height);
		_stage.setViewport(vp);
	}
	
	public void dispose()
	{
		this._skin.dispose();
		this._stage.dispose();
	}

	public InputProcessor getInputProcessor()
	{
		return this._stage;
	}
}
