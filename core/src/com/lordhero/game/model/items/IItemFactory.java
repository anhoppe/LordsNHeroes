package com.lordhero.game.model.items;

import com.lordhero.game.model.Npc;

public interface IItemFactory {
	IItem produce(Npc.Type producerType);
}
