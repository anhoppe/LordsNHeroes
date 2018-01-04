package com.lordhero.game.model;

import java.util.List;

import com.lordhero.game.model.IEntity;

public interface IEntities {
	public enum EntityType {
		None,
		Enemy,
		Npc
	};

	void update();
	
	List<IEntity> getEntitiesOnSite();
	
	void addNpc(int xPos, int yPos);

	void selectEntity(int xPos, int yPos);
	
	void load();
	
	void save();
}
