package com.lordhero.game.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.lordhero.game.IPlayer;
import com.lordhero.game.model.IMap;

public class MapController implements IController {

	private int _xCursor;
	private int _yCursor;
	
	private IMap _map;
	
	private IPlayer _player;
	
	public void setMap(IMap map) {
		_map = map;
	}
	
	public void setPlayer(IPlayer player) {
		_player = player;
	}
	
	@Override
	public void update() {
		movePlayer();

        _map.checkForCollision();
	}
	
	private void movePlayer() {
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            _player.move(IPlayer.Direction.Up);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            _player.move(IPlayer.Direction.Down);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            _player.move(IPlayer.Direction.Left);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            _player.move(IPlayer.Direction.Right);
		}
	}

	@Override
	public boolean processKeyUp(int keyCode) {
    	if (keyCode == Input.Keys.E)
    	{
    		_map.enter();
    	}
		return false;
	}

	@Override
	public boolean processMouseUp(int xPos, int yPos) {
		_map.setTile();
		return true;
	}

	@Override
	public boolean processMouseMove(int xPos, int yPos) {
		_map.setCursorPosition(xPos, yPos);
		return true;
	}
}
