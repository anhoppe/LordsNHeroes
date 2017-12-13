package com.lordhero.game.model.items;

import com.lordhero.game.model.Npc;
import com.lordhero.game.model.Npc.Type;

public class ItemFactory implements IItemFactory {

	@Override
	public IItem produce(Npc.Type producerType) {
		IItem item = null;
		
		switch (producerType) {
		case Hobo:
			break;
		case Blacksmith:
			item = Weapon.Create();
			break;
		case Bowyer:
			item = RangeWeapon.Create();
			break;
		case Healer:
			break;
		case King:
			break;
		case Knight:
			break;
		case Landlord:
			break;
		case None:
			break;
		case TownGuard:
			break;
		case Warrior:
			break;
		default:
			break;
		}
		
		return item;
	}

}
