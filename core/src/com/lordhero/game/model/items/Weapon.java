package com.lordhero.game.model.items;

import java.util.concurrent.ThreadLocalRandom;

public class Weapon extends ItemBase {

	public static Weapon[] WeaponTemplates = new Weapon[] {
		new Weapon("Knife", Type.Knife, 100),
		new Weapon("Dagger", Type.Dagger, 150),
		new Weapon("Axe", Type.Axe, 1200),
		new Weapon("Saber", Type.Saber, 1500),
		new Weapon("Sword", Type.Sword, 3500),
		new Weapon("TwoHander", Type.TwoHander, 5000)
	};
	
	enum Type {
		None,
		Knife,
		Dagger,
		Axe,
		Saber,
		Sword,
		TwoHander
	}
	
	private Type _type;
	
	public Weapon(String name, Type type, int price) {
		_name = name;
		_type = type;
		_price = price;
	}
	
	public Weapon(Weapon weapon) {
		_name = weapon._name;
		_type = weapon._type;
		_price = weapon._price;
	}

	public static Weapon Create() {
		int index = ThreadLocalRandom.current().nextInt(0, WeaponTemplates.length - 1);
		
		return new Weapon(WeaponTemplates[index]);
	}

	@Override
	public String getName() {
		return _name;
	}
	
}
