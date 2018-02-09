package com.lordhero.game.model;

import java.util.List;

import com.lordhero.game.model.IPlayer;
import com.lordhero.game.model.items.IItem;

public interface INpc extends INonPlayer {
	List<IItem> getItems();

	void addMoney(int costs);
}
