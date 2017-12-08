package com.lordhero.game.controller;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lordhero.game.model.IEntities;
import com.lordhero.game.model.IEntity;

public class EntityController implements IController {
	
	IEntities _entities;
	
	public EntityController() {
	}
		
	public void setEntities(IEntities entities) {
		_entities = entities;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean processMouseMove(int xPos, int yPos) {
		// TODO Auto-generated method stub
		return false;
	}	
}
