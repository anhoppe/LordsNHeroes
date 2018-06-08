package com.lordhero.game.model.items;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.lordhero.game.model.Npc;

public interface IItemFactory {
	IItem produce(Npc.Type producerType);

	IItem produce(String selectedItem, int xPosCell, int yPosCell);

	IItem produce(Element itemNode);
}
