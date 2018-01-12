package com.lordhero.game.model.items;

public class ItemBase implements IItem {
	protected String _name;
	protected int _price;
	
	public String getName() {
		return _name;
	}
	
	public int getPrice() {
		return _price;
	}
}
