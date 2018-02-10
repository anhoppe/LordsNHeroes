package com.lordhero.game.model.items;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

public interface IItem {
	String getName();
	int getPrice();
	
	void write(XmlWriter writer) throws IOException;
}
