package com.lordhero.game.model;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.model.IPlayer;

public interface IEntity {

	Sprite getSprite();

	float getX();
	
	float getY();

	float getRotation();

	boolean isAt(int xPos, int yPos);
	
	boolean isInRange(int xPos, int yPos);
	
	// Can be overwritten by derived class in order to restore non-serializable members
	void restore();

	TextureRegion getWeaponAnimationFrame();
	
	void write(XmlWriter writer) throws IOException;
	
	void dispose();

}
