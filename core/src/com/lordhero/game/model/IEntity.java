package com.lordhero.game.model;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.lordhero.game.IPlayer;

public interface IEntity extends IMovable {

	void update(IPlayer player);

	boolean isTerminated();

	Sprite getSprite();

	float getX();
	
	float getY();

	boolean isAt(int xPos, int yPos);
	
	boolean isInRange(int xPos, int yPos);
	
	// Can be overwritten by derived class in order to restore non-serializable members
	void restore();

	TextureRegion getWeaponAnimationFrame();

}
