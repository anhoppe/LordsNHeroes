package com.lordhero.game.model.items;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.utils.XmlWriter;

public class RangeWeapon extends ItemBase {
	public enum Type {
		None,
		SlingShot,
		Bow,
	}
	
	public static RangeWeapon[] Templates = new RangeWeapon[] {
			new RangeWeapon("Sling shot", Type.SlingShot, 500),
			new RangeWeapon("Bow", Type.Bow, 800)
	};
	
	private String _name;
	
	private Type _type;
		
	public RangeWeapon(String name, Type type, int price) {
		_name = name;
		_type = type;
		_price = price;
	}

	public RangeWeapon(RangeWeapon rangeWeapon) {
		_name = rangeWeapon._name;
		_type = rangeWeapon._type;
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
		writer.element("RangedWeapon").attribute("Name", _name).attribute("Type", _type);
		super.write(writer);
		writer.pop();
	}

}
