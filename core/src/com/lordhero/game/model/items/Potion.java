package com.lordhero.game.model.items;

import java.util.concurrent.ThreadLocalRandom;

import com.lordhero.game.model.Dice;
import com.lordhero.game.model.ICreature;

public class Potion extends ItemBase {

	public static Potion[] PotionTemplates = new Potion[] {
		new Potion("Cure light wounds", Type.CureLightWounds, 20, new Dice(2, 10, 20)),
		new Potion("Cure medium wounds", Type.CureMediumWounds, 40, new Dice(4, 10, 40)),
		new Potion("Cure serious wounds", Type.CureSeriousWounds, 100, new Dice(6, 10, 60)),
		new Potion("Cure critical wounds", Type.CureCriticalWounds, 400, new Dice(8, 10, 80)),
	};
	
	enum Type {
		None,
		CureLightWounds,
		CureMediumWounds,
		CureSeriousWounds,
		CureCriticalWounds
	}
	
	private Type _type;
	
	private Dice _dice;
	
	private boolean _isFull = true;
	
	public Potion(String name, Type type, int price, Dice dice) {
		_name = name;
		_type = type;
		_price = price;
		_dice = dice;
	}

	public Potion(Potion potion) {
		_name = potion._name;
		_type = potion._type;
		_price = potion._price;
		_dice = potion._dice;
	}

	public static Potion Create() {
		return new Potion(PotionTemplates[ThreadLocalRandom.current().nextInt(0, PotionTemplates.length - 1)]);		
	}

	public void quaff(ICreature targetCreature) {
		if (_isFull) {
			targetCreature.addHitPoints(_dice.roll());
			_isFull = false;
		}
	}
}
