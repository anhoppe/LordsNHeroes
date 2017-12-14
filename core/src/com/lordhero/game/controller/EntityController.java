package com.lordhero.game.controller;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lordhero.game.IGameMode;
import com.lordhero.game.model.IEntities;
import com.lordhero.game.model.IEntity;

public class EntityController implements IController {
	
	IEntities _entities;
	
	IGameMode _gameMode;
	
	public EntityController() {
	}
		
	public void setEntities(IEntities entities) {
		_entities = entities;
	}
	
	public void setGameMode(IGameMode gameMode) {
		_gameMode = gameMode;
	}
  	
	@Override
	public void update() {		
		_entities.update();
	}

	@Override
	public boolean processKeyUp(int keyCode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean processMouseUp(int xPos, int yPos) {
		if (_gameMode.get() == IGameMode.GameMode.AddNpc) {
			_entities.addNpc(xPos, yPos);
			return true;
		}
		else if (_gameMode.get() == IGameMode.GameMode.SelectMapItems) {
			_entities.selectEntity(xPos, yPos);
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
