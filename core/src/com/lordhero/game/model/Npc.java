package com.lordhero.game.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Npc implements IEntity {
	private String _name;
	
	private int _price;

	private Sprite _sprite;

	public Npc(String name, int price) {
		_name = name;
		_price = price;
	}
	
	public Npc(Npc npc, int xPos, int yPos) {
		_name = npc._name;
		_price = npc._price;
		_sprite = new Sprite(npc._sprite);
		
		_sprite.setCenter((float)xPos, (float)yPos);
	}

	public String getName() {
		return _name;
	}
	
	public int getPrice() {
		return _price;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub		
	}

	@Override
	public boolean isTerminated() {
		return false;
	}

	@Override
	public Sprite getSprite() {
		return _sprite;
	}
	
	public void setSprite(Sprite sprite) {
		_sprite = sprite;
	}
}
