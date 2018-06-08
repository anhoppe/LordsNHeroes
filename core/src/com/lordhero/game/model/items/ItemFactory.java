package com.lordhero.game.model.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.lordhero.game.Consts;
import com.lordhero.game.model.Npc;

public class ItemFactory implements IItemFactory {

	private static Texture _itemTileSet = new Texture(Gdx.files.internal("data/tileset/Monsters.png"));

	@Override
	public IItem produce(Npc.Type producerType) {
		IItem item = null;
		
		switch (producerType) {
		case Hobo:
			break;
		case Blacksmith:
			item = MeleeWeapon.Create();
			break;
		case Bowyer:
			item = RangeWeapon.Create();
			break;
		case Healer:
			item = Potion.Create();
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

	@Override
	public IItem produce(Element itemNode) {
		IItem item = null;
		
		if (itemNode.getName().equals("GenericItem")) {
			int xPosPx = itemNode.getInt("XPos");
			int yPosPx = itemNode.getInt("YPos");
			int tileIndex = itemNode.getInt("TileIndex");
			
			GenericItem genericItem = new GenericItem(itemNode);
			genericItem.setSprite(getSpriteFromTileSet(tileIndex), tileIndex);
			genericItem.setPosition(xPosPx, yPosPx);
			
			item = genericItem;
		}
		else {
			System.err.println("Unknwon item type: " + itemNode.getName());
		}
		
		return item;
	}

	@Override
	public IItem produce(String itemName, int xPosCell, int yPosCell) {
		return produceGenericItem(itemName, xPosCell, yPosCell);
	}	
	
	private GenericItem produceGenericItem(String itemName, int xPosCell, int yPosCell) {
		GenericItem item = new GenericItem();
		item.setName(itemName);
		
		if (itemName.equals(Consts.DoorItem)) {
			createDoor(item);
		}
		else if (itemName.equals(Consts.KeyItem)) {
			createKey(item);
		}
		else {
			System.err.println("No such item type: " + itemName);
			item = null;
		}
		
		if (item != null) {
			item.setPosition(xPosCell, yPosCell);			
		}
		
		return item;
	}

	private void createDoor(GenericItem genericItem) {
		genericItem.addProperty(Consts.ItemPropKeyCode, new Integer(0));
		genericItem.addProperty(Consts.ItemPropIsOpen, new Boolean(false));
		
		genericItem.setSprite(getSpriteFromTileSet(Consts.ItemSpriteIndexDoor), Consts.ItemSpriteIndexDoor);
	}
	
	private void createKey(GenericItem genericItem) {
		genericItem.addProperty(Consts.ItemPropKeyCode, new Integer(0));

		genericItem.setSprite(getSpriteFromTileSet(Consts.ItemSprintIndexKey), Consts.ItemSprintIndexKey);		
	}

	private Sprite getSpriteFromTileSet(int itemSpriteIndex) {
		TextureRegion region = new TextureRegion(_itemTileSet, itemSpriteIndex % Consts.ItemTileSetWidthFields * Consts.TileWidth, itemSpriteIndex / Consts.ItemTileSetWidthFields * Consts.TileHeight, Consts.TileWidth, Consts.TileWidth);
		 
		return new Sprite(region);
	}
}
