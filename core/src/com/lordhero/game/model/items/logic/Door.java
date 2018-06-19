package com.lordhero.game.model.items.logic;

import com.lordhero.game.Consts;
import com.lordhero.game.model.items.IGenericItem;

public class Door implements IItemLogic {
	
	@Override
	public boolean blocksMovement(IGenericItem item) {
		return !item.getBoolean(Consts.ItemPropIsOpen);
	}

	@Override
	public void use(IGenericItem item, int xPosPx, int yPosPx) {
		boolean isLocked = item.getBoolean(Consts.ItemPropIsLocked);
		
		if (!isLocked) {
			boolean isOpen = item.getBoolean(Consts.ItemPropIsOpen);
			item.setBoolean(Consts.ItemPropIsOpen, !isOpen);
		}
	}	
}
