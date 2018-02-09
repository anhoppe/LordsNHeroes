package com.lordhero.game.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.IGameMode;
import com.lordhero.game.model.items.IItem;
import com.lordhero.game.model.items.IItemFactory;

public class Npc extends CreatureBase implements INpc {
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
			new Npc("Blacksmith", 5000, Npc.Type.Blacksmith, 0.005),
			new Npc("Bowyer", 4000, Npc.Type.Bowyer, 0.008),
			new Npc("Warrior", 6000, Npc.Type.Warrior, 0),
			new Npc("Town guard", 4500, Npc.Type.TownGuard, 0),
			new Npc("King", 20000, Npc.Type.King, 0),
			new Npc("Healer", 8000, Npc.Type.Healer, 0),
			new Npc("Landlord", 1500, Npc.Type.Landlord, 0),
			new Npc("Knight", 15000, Npc.Type.Knight, 0)
			));

	private String _name;
	
	private int _price;
	
	private Type _type;
	
	private double _productionProbability;
	
	private List<IItem> _items = new LinkedList<IItem>();
	
	private static IItemFactory ItemFactory = new com.lordhero.game.model.items.ItemFactory();

	public Npc(String name, int price, Type type, double productionProbabilityPerMinute) {
		_name = name;
		_price = price;
		_type = type;
		_productionProbability = productionProbabilityPerMinute;
	}
	
	public Npc(Npc npc, int xPos, int yPos) {		
		_name = npc._name;
		_price = npc._price;
		_type = npc._type;
		_productionProbability = npc._productionProbability;
		
		_sprite = new Sprite(npc._sprite);
	
		_xPos = xPos;
		_yPos = yPos;
		
		_sprite.setCenter((float)xPos, (float)yPos);
	}
	
	public Npc(Element entityNode) {
		super(entityNode.getChildByName("CreatureBase"));

		_name = entityNode.getAttribute("Name");
		_price = entityNode.getIntAttribute("Price");
		_productionProbability = entityNode.getFloatAttribute("ProductionProbability");
		_type = Type.valueOf(entityNode.getAttribute("Type"));
				
		restore();
	}

	@Override
	public List<IItem> getItems() {
		return _items;
	}

	@Override
	public void update(IPlayer player, IGameMode gameMode) {
		if (Math.random() < _productionProbability) {
			_items.add(ItemFactory.produce(_type));
		}
	}

	@Override
	public boolean isTerminated() {
		return false;
	}
	
	public void setSprite(Sprite sprite) {
		_sprite = sprite;
	}
	
	@Override
	public void restore() {
		for (Npc npc : Templates) {
			if (npc._type == _type) {
				_sprite = new Sprite(npc._sprite);
				_sprite.setCenter((float)_xPos, (float)_yPos);
				break;
			}
		}
	}

	@Override
	public TextureRegion getWeaponAnimationFrame() {
		return null;
	}

	public String getName() {
		return _name;
	}
	
	public int getPrice() {
		return _price;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void write(XmlWriter writer) throws IOException {
		writer.element("Npc").
		attribute("Name", _name).
		attribute("Price", _price).
		attribute("Type", _type).
		attribute("ProductionProbability", _productionProbability);
		
		super.write(writer);

		for (IItem item : _items) {
			item.write(writer);
		}
				
		writer.pop();
	}
}
