package com.lordhero.game.model;

import java.util.Arrays;
import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Npc implements IEntity {
	public enum Type {
		None,
		Hobo,
		Blacksmith,
		Bowyer,
		Warrior,
		TownGuard,
		King,
		Healer,
		Landlord,
		Knight
	}
	
	public static final LinkedList<Npc> Templates = new LinkedList<Npc>(Arrays.asList(
			new Npc("Hobo", 100, Npc.Type.Hobo, 0), 
			new Npc("Blacksmith", 5000, Npc.Type.Blacksmith, 50),
			new Npc("Bowyer", 4000, Npc.Type.Bowyer, 50),
			new Npc("Warrior", 6000, Npc.Type.Warrior, 0),
			new Npc("Town guard", 4500, Npc.Type.TownGuard, 0),
			new Npc("King", 20000, Npc.Type.King, 0),
			new Npc("Healer", 8000, Npc.Type.Healer, 0),
			new Npc("Landlord", 1500, Npc.Type.Landlord, 0),
			new Npc("Knight", 15000, Npc.Type.Knight, 0)
			));


	private String _name;
	
	private int _price;

	private Sprite _sprite;
	
	private Type _type;
	
	private int _productionProbabilityPerMinute;

	public Npc(String name, int price, Type type, int productionProbabilityPerMinute) {
		_name = name;
		_price = price;
		_type = type;
		_productionProbabilityPerMinute = productionProbabilityPerMinute;
	}
	
	public Npc(Npc npc, int xPos, int yPos) {
		_name = npc._name;
		_price = npc._price;
		_type = npc._type;
		_productionProbabilityPerMinute = npc._productionProbabilityPerMinute;
		
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
