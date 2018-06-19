package com.lordhero.game.model.items.logic;

import com.lordhero.game.model.items.IGenericItem;

public interface IItemLogic {
	boolean blocksMovement(IGenericItem item);
	void use(IGenericItem item, int xPosPx, int yPosPx);
}
