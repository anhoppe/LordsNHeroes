package com.lordhero.game.model;

public class MapCreationInfo {	
	public String _templateMapName;
	public String _newMapName;
	
	// Name of the map that the new map is created on
	public String _parentMapName;
	
	// Position where the exit of the newly created map leads to on the parent map
	public int _exitTargetX;
	public int _exitTargetY;
	
	// Cell positions of the new maps exit (which leads back to the parent map)
	public int _newMapsExitXCell;
	public int _newMapsExitYCell;
	
	public MapCreationInfo(String templateMapName, String newMapName, String parentMapName, int exitTargetX, int exitTargetY) {
		_templateMapName = templateMapName;
		_newMapName = newMapName; 
		_parentMapName = parentMapName; 
		_exitTargetX = exitTargetX;
		_exitTargetY = exitTargetY;
	}
}
