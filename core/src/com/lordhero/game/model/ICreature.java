package com.lordhero.game.model;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.lordhero.game.model.items.IItem;

public interface ICreature extends IEntity {
	float getRotation();
	
	boolean isInRange(int xPos, int yPos);

	TextureRegion getWeaponAnimationFrame();
	
	void addHitPoints(int hitPoints);
	
	List<IItem> getItems();
}
