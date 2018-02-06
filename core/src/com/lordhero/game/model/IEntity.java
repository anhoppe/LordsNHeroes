package com.lordhero.game.model;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlWriter;

public interface IEntity {

	Sprite getSprite();

	float getX();
	
	float getY();

	boolean isAt(int xPos, int yPos);

	// Can be overwritten by derived class in order to restore non-serializable members
	void restore();
	
	void write(XmlWriter writer) throws IOException;
	
	void dispose();

}
