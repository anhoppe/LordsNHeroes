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
		_entities.update(_player);
	}

	@Override
	public boolean processKeyUp(int keyCode) {
		if (_gameMode.is(GameMode.Play)) {
			if (keyCode == Input.Keys.T) {
				INpc npc = _entities.getNpcInRange((int)_player.getX(), (int)_player.getY());				
				if (npc != null) {
					_gameMode.set(GameMode.Conversation, npc);
					return true;					
				}
			}
			
			else if (keyCode == Input.Keys.C) {
				_gameMode.set(GameMode.CharacterSheet, null);
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean processMouseDown(int xScreen, int yScreen, int xCursor, int yCursor) {
		// TODO Auto-generated method stub
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
