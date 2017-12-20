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

	void set(GameMode gameMode);
}
