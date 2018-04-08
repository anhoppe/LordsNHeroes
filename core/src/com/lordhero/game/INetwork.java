package com.lordhero.game;

public interface INetwork {
	enum ConnectionType {
		None,
		Local,
		Remote
	}

	void connectToServer(ConnectionType local);

	byte[] requestMap(String currentMap);

	byte[] requestEntities();

	boolean createMapFromTemplate(String siteTemplateFileName, String newMapFileName);
}
