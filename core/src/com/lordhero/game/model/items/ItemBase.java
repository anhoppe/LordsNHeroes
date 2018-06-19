package com.lordhero.game.model.items;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.lordhero.game.Consts;
import com.badlogic.gdx.utils.XmlWriter;

public class ItemBase implements IItem {
	protected String _name;
	protected int _price;	
	protected Sprite _sprite;
	
	
	public ItemBase() {
		
	}
	
	public ItemBase(Element itemNode) {
		_name = itemNode.getAttribute(Consts.SerializeName);
		_price = itemNode.getInt(Consts.SerializePrice);		
	}

	@Override
	public String getName() {
		return _name;
	}
	
	@Override
	public int getPrice() {
		return _price;
	}
	
	@Override
	public Sprite getSprite() {
		return _sprite;
	}
	
	@Override 
	public boolean isAt(int xPosPx, int yPosPx) {
		boolean isAt = false;
		if (_sprite != null) {
			if (Math.abs(xPosPx - (int)_sprite.getX()) < Consts.IsAtRange &&
				Math.abs(yPosPx - (int)_sprite.getY()) < Consts.IsAtRange) {
				isAt = true;
			}			
		}
			
		return isAt;
	}
	
	@Override
	public void write(XmlWriter writer) throws IOException {
		writer.element("Item").attribute(Consts.SerializeName, _name).attribute(Consts.SerializePrice, _price);
		writer.pop();
	}
	
	public void setName(String name) {
		_name = name;
	}	
}
