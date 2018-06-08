package com.lordhero.game.model.items;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlWriter;

public interface IItem {
	String getName();
	int getPrice();
	Sprite getSprite();
	
	boolean isAt(int xPosPx, int yPosPx);
	
	void write(XmlWriter writer) throws IOException;
}
