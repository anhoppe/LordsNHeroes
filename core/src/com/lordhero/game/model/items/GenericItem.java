package com.lordhero.game.model.items;

import java.io.IOException;
import java.util.Hashtable;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.Consts;

public class GenericItem extends ItemBase implements IGenericItem {

	private Hashtable<String, ItemProperty> _properties;
	
	private int _tileIndex;
	
	public GenericItem() {
		_properties = new Hashtable<String, ItemProperty>();		
	}
	
	public GenericItem(Element itemNode) {
		super(itemNode.getChildByName("Item"));
		
		_properties = new Hashtable<String, ItemProperty>();
		
		Array<Element> propertyNodes = itemNode.getChildrenByName("Property");
		
		for (Element propertyNode : propertyNodes) {
			String name = propertyNode.get(Consts.SerializeName);
			ItemProperty itemProperty = new ItemProperty(propertyNode);
			
			if (_properties.containsKey(name)) {
				System.err.println("Could not read property from xml, has already property with name: " + name);
				return;
			}
			_properties.put(name, itemProperty);			
		}
	}
	
	@Override
	public boolean is(String itemName) {
		return _name.equals(itemName);
	}
	
	@Override
	public boolean getBoolean(String propertyName) {
		Object value = getPropertyValue(propertyName);
		
		if (!(value instanceof Boolean)) {
			System.err.println("Property " + propertyName + " is not of type boolean");
			return false;
		}
		
		return ((Boolean)value).booleanValue();
	}
	
	@Override
	public void setBoolean(String propertyName, boolean value) {
		Object prevValue = getPropertyValue(propertyName);
		
		if (!(prevValue instanceof Boolean)) {
			System.err.println("Property " + propertyName + " is not of type boolean");
		}

		addProperty(propertyName, Boolean.valueOf(value));
	}
	
	@Override
	public int getInteger(String propertyName) {
		Object value = getPropertyValue(propertyName);
		
		if (!(value instanceof Integer)) {
			System.err.println("Property " + propertyName + " is not of type integer");
			return 0;
		}
		
		return ((Integer)value).intValue();
	}

	@Override
	public void write(XmlWriter writer) throws IOException {
		writer.element("GenericItem").attribute("XPos", (int)_sprite.getX()).attribute("YPos", (int)_sprite.getY()).attribute("TileIndex", _tileIndex);
		super.write(writer);
		
		for (String key : _properties.keySet()) {
			ItemProperty property = _properties.get(key);
			writer.element("Property");
			writer.attribute("Name", key);
			property.write(writer);
			
			writer.pop();
		}
		
		writer.pop();
	}
	
	public void setSprite(Sprite sprite, int tileIndex) {
		_sprite = sprite;
		_tileIndex = tileIndex;
	}
	
	public void setPosition(int xPosPx, int yPosPx) {
		if (_sprite != null) {
			_sprite.setX(xPosPx);
			_sprite.setY(yPosPx);
		}
	}

	public void addProperty(String propertyName, Object object) {
		if (_properties.containsKey(propertyName)) {
			System.err.println("Generic item already contains property: " + propertyName);
			return;
		}
		
		_properties.put(propertyName, new ItemProperty(object));
	}
	
	private Object getPropertyValue(String propertyName) {
		ItemProperty property = _properties.get(propertyName);
		
		if (property == null) {
			System.err.println("Generic item " + _name + " has no such property: " + propertyName);
			return false;
		}
		
		return property.get();
	}		
}
