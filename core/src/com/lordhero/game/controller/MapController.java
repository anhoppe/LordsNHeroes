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
import com.lordhero.game.model.IEntities;
import com.lordhero.game.model.IMap;
import com.lordhero.game.model.items.IWeapon;

public class MapController implements IController, IMapController {

	private static final float AngleSpeed = 1.0f;

	private IMap _map;
	
	private IPlayer _player;
	
	private IEntities _entities;
	
	private IGameMode _gameMode;
	
	private INetwork _network;
	
	public void setMap(IMap map) {
		_map = map;
	}
	
	public void setPlayer(IPlayer player) {
		_player = player;
	}
	
	public void setEntities(IEntities entities) {
		_entities = entities;
	}
	
	public void setGameMode(IGameMode gameMode) {
		_gameMode = gameMode;
	}
	
	public void setNetwork(INetwork network) {
		_network = network;
	}
	
	@Override
	public void update() {		
		if (_gameMode.is(GameMode.Play)) {
			movePlayer();
		}
		else {
			moveLord();
		}

        _map.checkForCollision();
        
        _player.evaluateAttack(_entities);
	}
	
	@Override
	public boolean processKeyUp(int keyCode) {
    	if (keyCode == Input.Keys.E)
    	{
    		if (_map.enter()) {
    			_map.loadRemoteMap(_network);
    			return true;
    		}
    	}		
		else if (_gameMode.is(GameMode.Play) && keyCode == Input.Keys.C) {
			_gameMode.set(GameMode.CharacterSheet, null);
			return true;
		}

		return false;
	}

	@Override
	public boolean processMouseDown(int xScreen, int yScreen, int xCursor, int yCursor) {
		if (_gameMode.is(IGameMode.GameMode.Play)) {
			_player.startAttack();
			return true;			
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
		if (_gameMode.is(GameMode.Play)) {
			float angleDeg = xPos / AngleSpeed;
			_player.setViewAngle(angleDeg);
			return true;
		}
		else {
			_map.setCursorPosition(xPos, yPos);			
			return true;
		}
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
	

	private void moveLord() {
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            _player.moveAbsolute(IPlayer.Direction.Up);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            _player.moveAbsolute(IPlayer.Direction.Down);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            _player.moveAbsolute(IPlayer.Direction.Left);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            _player.moveAbsolute(IPlayer.Direction.Right);
		}
	}
	
	private void movePlayer() {
		_player.setViewAngle(Gdx.input.getX() * AngleSpeed);

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            _player.moveDirection(IPlayer.Direction.Up);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            _player.moveDirection(IPlayer.Direction.Down);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            _player.moveDirection(IPlayer.Direction.Left);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            _player.moveDirection(IPlayer.Direction.Right);
		}			
		
	}
}
