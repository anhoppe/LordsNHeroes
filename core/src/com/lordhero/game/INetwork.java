package com.lordhero.game;

import com.lordhero.game.model.MapCreationInfo;

public interface INetwork {
	enum ConnectionType {
		None,
		Local,
		Remote
	}

	void connectToServer(ConnectionType local);

	byte[] requestMap(String currentMap);

	byte[] requestEntities();

	boolean createMapFromTemplate(MapCreationInfo mapCreationInfo);
}
