package com.lordhero.game.model.items;

import java.util.concurrent.ThreadLocalRandom;

public class RangeWeapon implements IItem {
	public enum Type {
		None,
		SlingShot,
		Bow,
	}
	
	public static RangeWeapon[] Templates = new RangeWeapon[] {
			new RangeWeapon("Sling shot", Type.SlingShot),
			new RangeWeapon("Boe", Type.Bow)
	};
	
	private String _name;
	
	private Type _type;
		
	public RangeWeapon(String string, Type slingshot) {
		// TODO Auto-generated constructor stub
	}

	public RangeWeapon(RangeWeapon rangeWeapon) {
		_name = rangeWeapon._name;
		_type = rangeWeapon._type;
	}

	public static RangeWeapon Create() {
		int index = ThreadLocalRandom.current().nextInt(0, Templates.length - 1);
		
		return new RangeWeapon(Templates[index]);
	}

	@Override
	public String getName() {
		return _name;
	}
}
