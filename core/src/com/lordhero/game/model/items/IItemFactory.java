package com.lordhero.game.model.items;

import com.lordhero.game.model.Npc;
import com.lordhero.game.model.Npc.Type;

public interface IItemFactory {
	IItem produce(Npc.Type producerType);
}
