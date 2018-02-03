package com.lordhero.game;

import java.nio.file.Path;

import com.lordhero.game.IGameMode.SaveType;

public interface IGameSourceProvider {
	void visitWorld();
	void goHome();
	
	String getWorldName();
	
	void setWorldName(String worldName);
	
	Path getSaveFolder(SaveType saveType);
}
