package com.lordhero.game;

import com.lordhero.game.model.IEntity;

public interface IGameMode {
	public enum GameMode {
		None,
		BuyTiles,
		AddNpc,
		SelectMapItems,
		Play, 
		Conversation, 
		CharacterSheet
	}
	
	GameMode get();
	
	boolean is(GameMode gameMode);

	void set(GameMode gameMode, IEntity entity);
	
	String getWorldName();
	
	void setWorldName(String worldName);
}
