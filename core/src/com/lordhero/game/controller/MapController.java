package com.lordhero.game.controller;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.lordhero.game.INetwork.ConnectionType;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IPlayer;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.INetwork;
import com.lordhero.game.model.IMap;

public class MapController implements IController, IMapController {

	private IMap _map;
	
	private IPlayer _player;
	
	private IGameMode _gameMode;
	
	private INetwork _network;
	
	public void setMap(IMap map) {
		_map = map;
	}
	
	public void setPlayer(IPlayer player) {
		_player = player;
	}
	
	public void setGameMode(IGameMode gameMode) {
		_gameMode = gameMode;
	}
	
	public void setNetwork(INetwork network) {
		_network = network;
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
    		if (_map.enter()) {
    			_map.loadRemoteMap(_network);
    		}
    	}
		return false;
	}

	@Override
	public boolean processMouseUp(int xScreen, int yScreen, int xCursor, int yCursor) {
		
		if (_gameMode.get() == IGameMode.GameMode.BuyTiles)
		{
			_map.setTile();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean processMouseMove(int xPos, int yPos) {
		_map.setCursorPosition(xPos, yPos);
		return true;
	}

	@Override
	public void visitWorld() {
		_network.connectToServer(ConnectionType.Remote);
		
		_gameMode.set(GameMode.Play, null);

		_map.loadRemoteMap(_network);	
	}

	@Override
	public void goHome() {
		_network.connectToServer(ConnectionType.Local);
		
		_gameMode.set(GameMode.BuyTiles, null);

		_map.loadRemoteMap(_network);
	}
}
