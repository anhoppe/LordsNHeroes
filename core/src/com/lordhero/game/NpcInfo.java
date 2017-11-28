package com.lordhero.game;

import com.badlogic.gdx.graphics.Texture;

public class NpcInfo {
	private String _name;
	
	private int _price;

	private Texture _texture;
	
	public NpcInfo(String name, int price) {
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
}
