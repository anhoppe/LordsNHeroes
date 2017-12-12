package com.lordhero.game;

public interface IGameMode {
	public enum GameMode {
		None,
		BuyTiles,
		AddNpc		
	}
	
	GameMode get();
}
