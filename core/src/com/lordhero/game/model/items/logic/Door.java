package com.lordhero.game.model.items.logic;

import com.lordhero.game.Consts;
import com.lordhero.game.model.items.IGenericItem;

public class Door implements IItemLogic {

	@Override
	public boolean blocksMovement(IGenericItem item) {
		return !item.getBoolean(Consts.ItemPropIsOpen);
	}	
}
