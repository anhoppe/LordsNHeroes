package com.lordhero.game.model.items;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.model.Dice;

public class RangeWeapon extends ItemBase {
	public enum Type {
		None,
		SlingShot,
		Bow,
	}
	
	public static RangeWeapon[] Templates = new RangeWeapon[] {
			new RangeWeapon("Sling shot", Type.SlingShot, 500, new Dice(1, 6, 0), 200),
			new RangeWeapon("Bow", Type.Bow, 2000, new Dice(1, 6, 0), 800)
	};
	
	private String _name;
	
	private Type _type;
	
	private Dice _damage;
	
	private int _range;
		
	public RangeWeapon(String name, Type type, int price, Dice damage, int range) {
		_name = name;
		_type = type;
		_price = price;
		_damage = damage;
		_range = range;
	}

	public RangeWeapon(RangeWeapon rangeWeapon) {
		_name = rangeWeapon._name;
		_type = rangeWeapon._type;
		_price = rangeWeapon._price;
		_damage = rangeWeapon._damage;
		_range = rangeWeapon._range;
	}

	public static RangeWeapon Create() {
		int index = ThreadLocalRandom.current().nextInt(0, Templates.length);
		
		return new RangeWeapon(Templates[index]);
	}

	@Override
	public String getName() {
		return _name;
	}
	
	@Override
	public void write(XmlWriter writer) throws IOException {
		writer.element("RangedWeapon").
		attribute("Name", _name).
		attribute("Type", _type);
		super.write(writer);
		writer.pop();
	}
	
	public Dice getDamage() {
		return _damage;
	}
}
