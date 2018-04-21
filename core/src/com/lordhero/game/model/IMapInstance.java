package com.lordhero.game.model;

import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public interface IMapInstance {
	String getName();

	OrthogonalTiledMapRenderer load();

	IMapInstance getChildMap(float x, float y);

	// Returns the x/y position of the map's entry point
	float getXEntry();
	float getYEntry();

	int getPrice();

	void write();

	void dispose();

	boolean canBuildAt(int xPos, int yPos);

	void addSubSite(int xPos, int yPos, IMapInstance sitePattern, int newMapsExitXCell, int newMapsExitYCell);

	void setTileArray(int _xCursor, int _yCursor, String layerName, int[][] selectedCells);

	int getCollisions(float x, float y);
}
