package com.lordhero.game.model;

import java.util.List;

import com.lordhero.game.IPlayer;
import com.lordhero.game.model.items.IItem;

public interface INpc extends IEntity {
	List<IItem> getItems();
}
