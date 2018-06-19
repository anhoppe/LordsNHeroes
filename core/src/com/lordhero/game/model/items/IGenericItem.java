package com.lordhero.game.model.items;

public interface IGenericItem extends IItem {
	boolean is(String itemName);
	
	boolean getBoolean(String propertyName);
	void setBoolean(String propertyName, boolean value);
	
	int getInteger(String propertyName);
}
