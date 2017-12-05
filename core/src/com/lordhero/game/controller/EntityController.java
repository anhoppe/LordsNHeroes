package com.lordhero.game.controller;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.lordhero.game.model.IEntities;
import com.lordhero.game.model.IEntity;

public class EntityController {
	
	IEntities _entities;
	List<IEntityController> _entityControllers;
	
	public EntityController() {
		_entityControllers = new LinkedList<IEntityController>();
	}
	
	public void addController(IEntityController controller) {
		_entityControllers.add(controller);
	}
	
	public void setEntities(IEntities entities) {
		_entities = entities;
	}
  	
	public void render(SpriteBatch spriteBatch, String site) {
		
		_entities.update();

		List<IEntity> entitiesOnSite = _entities.getEntitiesOnSite(site);
		
		if (entitiesOnSite != null) {
			for (IEntity entity : entitiesOnSite) {					
				Sprite sprite = entity.getSprite();
				sprite.draw(spriteBatch);
			}		
		}
	}
	
}
