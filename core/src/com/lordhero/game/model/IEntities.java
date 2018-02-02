package com.lordhero.game.model;

import java.util.List;

import com.lordhero.game.model.IPlayer;
import com.lordhero.game.model.IEntity;
import com.lordhero.game.model.items.IWeapon;

public interface IEntities {
	public enum EntityType {
		None,
		Enemy,
		Npc
	};

	void update(IPlayer player);
	
	List<IEntity> getEntitiesOnSite();
	
	void addNpc(int xPos, int yPos);

	void selectEntity(int xPos, int yPos);
	
	INpc getNpcInRange(int xPos, int yPos);

	void load();
	
	void save();

	void hitEntity(int xPos, int yPos, IWeapon hitWeapon);
}
