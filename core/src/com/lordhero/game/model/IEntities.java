package com.lordhero.game.model;

import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.IGameMode;
import com.lordhero.game.model.items.IItem;
import com.lordhero.game.model.items.IWeapon;

public interface IEntities {
	public enum EntityType {
		None,
		Enemy,
		Npc
	};

	void update(IPlayer player, IGameMode gameMode);
	
	List<IEntity> getEntitiesOnSite();
	
	List<IItem> getItemsOnSite();
	
	void addNpc(int xPos, int yPos);
	
	void addMonsterPit(int xCursor, int yCursor);

	void selectEntity(int xPos, int yPos);
	
	INpc getNpcInRange(int xPos, int yPos);

	void save(XmlWriter writer) throws IOException;

	void hitEntity(int xPos, int yPos, IPlayer player, IWeapon weapon);

	void addItem(int xCursor, int yCursor);

	void remove(IItem item);

	IItem getItemInRange(int x, int y);

	int getCollisions(int xPosPx, int yPosPx);

}
