package com.lordhero.game.model.items;

import java.io.IOException;
import java.util.Hashtable;

import javax.xml.crypto.dsig.keyinfo.KeyValue;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;

public class GenericItem extends ItemBase {

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
			String name = propertyNode.get("Name");
			ItemProperty itemProperty = new ItemProperty(propertyNode);
			
			if (_properties.containsKey(name)) {
				System.err.println("Could not read property from xml, has already property with name: " + name);
				return;
			}
			_properties.put(name, itemProperty);			
		}
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
			_sprite.setCenterX(xPosPx);
			_sprite.setCenterY(yPosPx);
		}
	}

	public void addProperty(String propertyName, Object object) {
		if (_properties.containsKey(propertyName)) {
			System.err.println("Generic item already contains property: " + propertyName);
			return;
		}
		
		_properties.put(propertyName, new ItemProperty(object));
	}
}
