package com.lordhero.game.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface ICreature extends IEntity {
	float getRotation();
	
	boolean isInRange(int xPos, int yPos);

	TextureRegion getWeaponAnimationFrame();
	
	void addHitPoints(int hitPoints);
}
