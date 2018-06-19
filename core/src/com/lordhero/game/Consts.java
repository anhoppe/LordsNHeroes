package com.lordhero.game;

import com.badlogic.gdx.Input;

public class Consts {
	
	/////////////////////////////////////////////////////////////////////
	// Keyboard commands
	public static final int KeyCodeCharacterSheet = Input.Keys.C;
	public static final int KeyCodeEnterBuilding = Input.Keys.E;
	public static final int KeyCodePickUpItem = Input.Keys.SPACE;
	public static final int KeyCodeSlot1 = Input.Keys.NUM_1;
	public static final int KeyCodeSlot9 = Input.Keys.NUM_9;
	public static final int KeyCodeOpen = Input.Keys.O;	
	public static final int KeyCodeSwitchWeapon = Input.Keys.X;
	public static final int KeyCodeTalkToNpc = Input.Keys.T;
	
	/////////////////////////////////////////////////////////////////////
	// Strings
	
	// Menu of the lord
	public static final String EditMapMode = "Editor map";
	public static final String AddNpcMode = "Add npc";
	public static final String AddSiteMode = "Add site";
	public static final String AddItemMode = "Add item";
	public static final String MonsterPit = "Monster pit";
	public static final String SelectMode = "Select";
	
	// Site types
	public static final String TownSite = "Town";
	public static final String DungeonSite = "Dungeon";
	
	// Item types
	public static final String ItemKey = "Key";
	public static final String ItemDoor = "Door";
	
	// Map layer names
	public static final String Background = "Background";
	public static final String Obstacles = "Obstacles";
	public static final String Collision = "Collision";
	public static final String SiteLayer = "Buildings";

	// Map tags
	public static final String StartX = "StartX";
	public static final String StartY = "StartY";

	public static final String ChildMapEntryX = "xChildMapEntry";
	public static final String ChildMapEntryY = "yChildMapEntry";

	public static final String Map = "map";
	public static final String BaseMap = "baseMap";
	public static final String TargetMap = "TargetMap";
	
	// Map template names
	public static final String TownTemplate = "town_template";
	public static final String DungeonTemplate = "dungeon_template";

	// Item properties
	public static final String ItemPropIsLocked = "is_locked";
	public static final String ItemPropIsOpen = "is_open";
	public static final String ItemPropKeyCode = "key_code";
	
	// Serialization attributes
	public static final String SerializeName = "Name";
	public static final String SerializePrice = "Price";

	//////////////////////////////////////////////////////////////////////
	// Numeric
	public static final int TileWidth = 32;
	public static final int TileHeight = 32;
	public static final int ItemTileSetWidthFields = 30;
	public static final int IsAtRange = 32;
	
	// Direction constants
	public static final int Up = 1;
	public static final int Down = 2;
	public static final int Left = 4;
	public static final int Right = 8;

	// Item tile position for sprite
	public static final int ItemSpriteIndexDoor = 755;
	public static final int ItemSprintIndexKey = 528;
}
