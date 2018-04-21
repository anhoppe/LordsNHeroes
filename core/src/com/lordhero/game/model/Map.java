package com.lordhero.game.model;

import java.io.FileOutputStream;
import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.lordhero.game.Consts;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.IGameSourceProvider;
import com.lordhero.game.INetwork;
import com.lordhero.game.ISelectedCellProvider;
import com.lordhero.game.ISelectedSiteProvider;

public class Map implements IMap, IMapInfo {

	private TiledMapRenderer _tiledMapRenderer;

    private IMapInstance _currentMap;

    private IPlayer _player;
    
    // Absolute cursor position
    private int _xCursor;
    private int _yCursor;
    
	private ISelectedCellProvider _selectedCellProvider;
	
	private ISelectedSiteProvider _selectedSiteProvider;
	
	private IGameMode _gameMode;
	
	private IGameSourceProvider _gameSourceProvider;
	
	public void init() throws NullPointerException {
		if (_gameSourceProvider == null) {
			throw new NullPointerException();
		}
		_currentMap = MapInstance.Create(Consts.BaseMap, _gameSourceProvider);
	}

	public void setPlayer(IPlayer player) {
		_player = player;
	}	
	
    public void setSelectedCellProvider(ISelectedCellProvider selectedCellProvider) {
    	_selectedCellProvider = selectedCellProvider;
    }
    
    public void setGameMode(IGameMode gameMode) {
    	_gameMode = gameMode;
    }
    
    public void setGameSourceProvider(IGameSourceProvider gameSourceProvider) {
    	_gameSourceProvider = gameSourceProvider;
    }
    
    public void setSelectedSiteProvider(ISelectedSiteProvider selectedSiteProvider) {
    	_selectedSiteProvider = selectedSiteProvider;
    }

	@Override
	public String getCurrentMap() {
		return _currentMap.getName();
	}
	
	@Override
	public TiledMapRenderer getTiledMapRenderer() {
		return _tiledMapRenderer;
	}

	@Override
	public int getCursorX() {
		return _xCursor;
	}

	@Override
	public int getCursorY() {
		return _yCursor;
	}

	@Override
	public void setCursorPosition(int xPos, int yPos) {
		_xCursor = ((xPos - Gdx.graphics.getWidth() / 2) + (int)_player.getX()) / Consts.TileWidth * Consts.TileWidth + Consts.TileWidth / 2;
		_yCursor = (Gdx.graphics.getHeight() / 2 - yPos + (int)_player.getY()) / Consts.TileHeight * Consts.TileHeight + Consts.TileHeight / 2;		
	}

	@Override
	public void checkForCollision() {        
		int collisions = _currentMap.getCollisions(_player.getX(), _player.getY());
        
        _player.setCollisions((collisions & Consts.Up) != 0,
        		(collisions & Consts.Down) != 0,
        		(collisions & Consts.Left) != 0,
        		(collisions & Consts.Right) != 0);
	}

	@Override
	public boolean enter(INetwork network) {
		boolean entered = false;
		IMapInstance childMap = _currentMap.getChildMap(_player.getX(), _player.getY());
		
		if (childMap != null && _currentMap != null) {
			entered = true;
			if (_gameMode.get() != GameMode.Play) {
				_currentMap.write();
			}
			
			_currentMap = childMap;
			
			loadFromRemote(network);
			
			_player.setPosition(_currentMap.getXEntry(), _currentMap.getYEntry());
		}
				
		return entered;
	}

	public void setTile() {
        int[][] selectedCells = _selectedCellProvider.getSelectedCellIndexArray();
        int price = _selectedCellProvider.getSelectedCellPrice(selectedCells);
        
        if (_player.pay(price)) {
        	_currentMap.setTileArray(_xCursor, _yCursor, _selectedCellProvider.getLayerName(), selectedCells);
        }
	}

	@Override
	public void addSite(INetwork network) {
		String newMapFileName = "MyNewMap";
		String siteTemplateFileName = _selectedSiteProvider.getSelectedSiteFileName();
		MapCreationInfo mapCreationInfo = new MapCreationInfo(siteTemplateFileName, newMapFileName, _currentMap.getName(), _xCursor, _yCursor);
		
		if (network.createMapFromTemplate(mapCreationInfo))
		{
			IMapInstance selectedSite = MapInstance.Create(newMapFileName, _gameSourceProvider);
            
	        if (_currentMap.canBuildAt(_xCursor, _yCursor) && _player.pay(selectedSite.getPrice())) {
	        	_currentMap.addSubSite(_xCursor, _yCursor, selectedSite, mapCreationInfo._newMapsExitXCell, mapCreationInfo._newMapsExitYCell);
			}
		}
		else {
			System.err.println("Could not add site");
		}
	}

	public void loadFromRemote(INetwork network) {
		byte[] fileAsArray = network.requestMap(_currentMap.getName());
				
        writeMapToDisc(fileAsArray);
        
        _tiledMapRenderer = _currentMap.load();
   	}

	public void dispose() {
		if (_currentMap != null) {			
			_currentMap.dispose();
		}
	}

	private void writeMapToDisc(byte[] fileAsArray) {
		try {
			FileOutputStream fos = new FileOutputStream(Consts.Map);
	        fos.write(fileAsArray);
	        fos.flush();
	        fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
