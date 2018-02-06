package com.lordhero.game;

import java.nio.file.Path;

import com.lordhero.game.model.IEntity;

public interface IGameMode {
	public enum GameMode {
		None,
		BuyTiles,
		AddNpc,
		MonsterPit,
		SelectMapItems,
		Play, 
		Conversation, 
		CharacterSheet
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
