package com.lordhero.game.model;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface IEntity {

	void update();

	boolean isTerminated();

	Sprite getSprite();

	boolean isAt(int xPos, int yPos);
	
	// Can be overwritten by derived class in order to restore non-serializable members
	void restore();
}
