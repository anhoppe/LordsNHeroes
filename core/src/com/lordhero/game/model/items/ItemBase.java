package com.lordhero.game.model.items;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlWriter;

public class ItemBase implements IItem {
	protected String _name;
	protected int _price;
	
	@Override
	public String getName() {
		return _name;
	}
	
	@Override
	public int getPrice() {
		return _price;
	}
	
	@Override
	public void write(XmlWriter writer) throws IOException {
		writer.element("Item").attribute("Name", _name).attribute("Price", _price);
		writer.pop();
	}
}
