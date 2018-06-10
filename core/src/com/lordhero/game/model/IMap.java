package com.lordhero.game.model;

import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.lordhero.game.INetwork;

public interface IMap {

	// Setters for view
	TiledMapRenderer getTiledMapRenderer();
	int getCursorX();
	int getCursorY();
	
	// Setters for controller
	void checkForCollision(IEntities _entities);
	boolean enter(INetwork _network);
	void setTile();
	void setCursorPosition(int xPos, int yPos);
	void loadFromRemote(INetwork _network);
	void addSite(INetwork network);
}
