package com.lordhero.game;

public interface IGameMode {
	public enum GameMode {
		None,
		BuyTiles,
		AddNpc,
		SelectMapItems,
		Play
	}
	
	GameMode get();
	
	boolean is(GameMode gameMode);

	void set(GameMode gameMode);
	
	String getWorldName();
	
	void setWorldName(String worldName);
}
