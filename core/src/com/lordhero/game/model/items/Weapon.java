package com.lordhero.game.model.items;

import java.util.concurrent.ThreadLocalRandom;

public class Weapon implements IItem {

	public static Weapon[] WeaponTemplates = new Weapon[] {
		new Weapon("Knife", Type.Knife),
		new Weapon("Dagger", Type.Dagger),
		new Weapon("Axe", Type.Axe),
		new Weapon("Saber", Type.Saber),
		new Weapon("Sword", Type.Sword),
		new Weapon("TwoHander", Type.TwoHander)
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
	
	private String _name;	
	
	private Type _type;
	
	public Weapon(String name, Type type) {
		_name = name;
		_type = type;
	}
	
	public Weapon(Weapon weapon) {
		_name = weapon._name;
		_type = weapon._type;
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
