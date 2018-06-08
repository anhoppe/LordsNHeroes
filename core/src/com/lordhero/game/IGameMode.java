package com.lordhero.game;

import com.lordhero.game.model.IEntity;

public interface IGameMode {
	public enum GameMode {
		None,
		BuyTiles,
		AddNpc,
		MonsterPit,
		SelectMapItems,
		Play, 
		Purchase,
		Inn,
		CharacterSheet, 
		AddSite, 
		AddItem
	}
	
	public enum SaveType {
		None,
		Map,
		Entities
	}
	
	GameMode get();
	
	boolean is(GameMode gameMode);

	void set(GameMode gameMode, IEntity entity);
}
