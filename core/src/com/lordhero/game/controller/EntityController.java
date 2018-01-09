package com.lordhero.game.controller;

import java.util.List;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.IPlayer;
import com.lordhero.game.model.IEntities;
import com.lordhero.game.model.IEntity;
import com.lordhero.game.model.INpc;

public class EntityController implements IController {
	
	IEntities _entities;
	
	IGameMode _gameMode;
	
	IPlayer _player;
	
	public EntityController() {
	}
		
	public void setEntities(IEntities entities) {
		_entities = entities;
	}
	
	public void setGameMode(IGameMode gameMode) {
		_gameMode = gameMode;
	}
	
	public void setPlayer(IPlayer player) {
		_player = player;
	}
  	
	@Override
	public void update() {		
		_entities.update();
	}

	@Override
	public boolean processKeyUp(int keyCode) {
		if (keyCode == Input.Keys.T && _gameMode.is(GameMode.Play)) {
			INpc npc = _entities.getNpcInRange((int)_player.getX(), (int)_player.getY());
			
			npc.talk();
		}
		return false;
	}

	@Override
	public boolean processMouseUp(int xScreen, int yScreen, int xCursor, int yCursor) {
		if (_gameMode.get() == IGameMode.GameMode.AddNpc) {
			_entities.addNpc(xCursor, yCursor);
			return true;
		}
		else if (_gameMode.get() == IGameMode.GameMode.SelectMapItems) {
			_entities.selectEntity(xCursor, yCursor);
			return true;
		}

		return false;
	}

	@Override
	public boolean processMouseMove(int xPos, int yPos) {
		// TODO Auto-generated method stub
		return false;
	}	
}
