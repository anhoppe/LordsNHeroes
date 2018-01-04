package com.lordhero.game.model;

import com.badlogic.gdx.maps.tiled.TiledMapRenderer;

public interface IMap {

	// Setters for view
	TiledMapRenderer getTiledMapRenderer();
	int getCursorX();
	int getCursorY();
	
	// Setters for controller
	void checkForCollision();
	void enter();
	void setTile();
	void setCursorPosition(int xPos, int yPos);
	void visitWorld();
	void goHome();
}
