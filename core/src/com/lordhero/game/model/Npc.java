package com.lordhero.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Npc implements IEntity {
	private String _name;
	
	private int _price;

	private Texture _texture;
	
	public Npc(String name, int price) {
		_name = name;
		_price = price;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getPrice() {
		return _price;
	}
	
	public void setTexture(Texture texture) {
		_texture = texture;
	}
	
	public Texture getTexture() {
		return _texture;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTerminated() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Sprite getSprite() {
		// TODO Auto-generated method stub
		return null;
	}
}
