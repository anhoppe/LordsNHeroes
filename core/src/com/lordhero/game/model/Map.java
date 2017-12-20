package com.lordhero.game.model;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.IPlayer;
import com.lordhero.game.ISelectedCellProvider;

import net.dermetfan.gdx.maps.tiled.TmxMapWriter;
import net.dermetfan.gdx.maps.tiled.TmxMapWriter.Format;

public class Map implements IMap, IMapInfo {
	private static final String SendMap = "sendMap";
	private static final String RequestWorldName = "requestWorldName";
	private static final String CloseConnection = "closeConnection";
	
    private BufferedInputStream _inputStream;
    private PrintWriter _outputStream;

    private TiledMapRenderer _tiledMapRenderer;

    private String _currentMap;

    private TiledMap _tiledMap; 

    private IPlayer _player;
    
    private int _xCursor;
    private int _yCursor;
    
	private ISelectedCellProvider _selectedCellProvider;
	
	private IGameMode _gameMode;
	
	private String _worldName;
	private int _homePort;
	private int _visitorPort;

	public Map(int homePort, int visitorPort) {
		_homePort = homePort;
		_visitorPort = visitorPort;
		
		_currentMap = "baseMap";
		
		connectToServer(homePort);
		loadRemoteMap();
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

	@Override
	public String getCurrentMap() {
		return _currentMap;
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
		_xCursor = ((xPos - Gdx.graphics.getWidth() / 2) + (int)_player.getX()) / 32 * 32 + 16;
		_yCursor = (Gdx.graphics.getHeight() / 2 - yPos + (int)_player.getY()) / 32 * 32 + 16;		
	}

	@Override
	public void checkForCollision() {
        TiledMapTileLayer collisionObjectLayer = (TiledMapTileLayer)_tiledMap.getLayers().get("Collision");

        // get the player position
        int xPos = (int)(_player.getX() / collisionObjectLayer.getTileWidth());
        int yPos = (int)(_player.getY() / collisionObjectLayer.getTileHeight());                
        
        // check top
        _player.setCollisions(collisionObjectLayer.getCell(xPos, yPos+1) != null,
        		collisionObjectLayer.getCell(xPos, yPos-1) != null,
        		collisionObjectLayer.getCell(xPos-1, yPos) != null,
        		collisionObjectLayer.getCell(xPos+1, yPos) != null);
	}

	@Override
	public void visitWorld() {
		_outputStream.println(CloseConnection);
		
		// Do I know hot to implement proper network connections ?
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			_inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_outputStream.close();
		
		_gameMode.set(GameMode.Play);
		connectToServer(_visitorPort);
		loadRemoteMap();
	}

	@Override
	public void enter() {
        MapLayer layer = (MapLayer)_tiledMap.getLayers().get("Buildings");

        MapObjects objects = layer.getObjects();
		
		for (int i = 0; i < objects.getCount(); i++) {
			RectangleMapObject mapObject = (RectangleMapObject)objects.get(i);
			
			if (Math.abs(mapObject.getRectangle().x -_player.getX()) < 32f &&
				Math.abs(mapObject.getRectangle().y -_player.getY()) < 32f) {
				_currentMap = (String)mapObject.getProperties().get("TargetMap");
				
				_player.setPosition(Integer.parseInt(mapObject.getProperties().get("StartX").toString()), 
						Integer.parseInt(mapObject.getProperties().get("StartY").toString()));
				
				loadRemoteMap();		        
		        
				break;
			}
		}
	}

	public void setTile() {
        TiledMapTileLayer backgroundLayer = (TiledMapTileLayer)_tiledMap.getLayers().get(_selectedCellProvider.getLayerName());
   
        int[][] selectedCells = _selectedCellProvider.getSelectedCellIndexArray();
        int price = _selectedCellProvider.getSelectedCellPrice(selectedCells);
        
        if (_player.pay(price)) {
            int width = selectedCells.length;
            int height = selectedCells[0].length;
                    
            for (int x = 0; x < width; x++) {
            	for (int y = 0; y < height; y++) {
                	int xCell = x + (int)(_xCursor / backgroundLayer.getTileWidth());
                	int yCell = (int)(_yCursor / backgroundLayer.getTileHeight()) - y;

                	TiledMapTileLayer.Cell prevCell = backgroundLayer.getCell(xCell, yCell); 
                	
                	if (prevCell == null)
                	{
                		prevCell = new TiledMapTileLayer.Cell();
                		backgroundLayer.setCell(xCell,  yCell, prevCell);
                	}
                	prevCell.setTile(getSelectedTile(selectedCells[x][y]));        		
            	}
            }         
        }
	}

	public void dispose() {
		writeCurrentMap();
		_tiledMap.dispose();		
	}

	private void connectToServer(int port) {
        Socket socket;

		try {
			socket = new Socket("127.0.0.1", port);
	        _outputStream = new PrintWriter(socket.getOutputStream(), true);
	        _inputStream = new BufferedInputStream(socket.getInputStream());

	        // Read the name of the world the client is connected to
	        _outputStream.println(RequestWorldName);

	        byte[] fileLengthInBytes = new byte[4];
	        _inputStream.read(fileLengthInBytes, 0, 4);
	        int worldNameLength = new BigInteger(fileLengthInBytes).intValue();
	        
	        byte[] worldNameAsArray = new byte[worldNameLength];
	        _inputStream.read(worldNameAsArray, 0, worldNameLength);
	        
	        _worldName = new String(worldNameAsArray, "UTF-8");
	        
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadRemoteMap() {
        _outputStream.println("sendMap:" + _currentMap);

        byte[] fileLengthInBytes = new byte[4];
        try {
			_inputStream.read(fileLengthInBytes, 0, 4);
	        int fileLength = new BigInteger(fileLengthInBytes).intValue();
	        
	        byte[] fileAsArray = new byte[fileLength];
	        _inputStream.read(fileAsArray, 0, fileLength);

	        FileOutputStream fos = new FileOutputStream("map");
	        fos.write(fileAsArray);
	        fos.flush();
	        fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        _tiledMap =  new TmxMapLoader().load("map");
        _tiledMapRenderer = new OrthogonalTiledMapRenderer(_tiledMap);                
	}

	private void writeCurrentMap() {
		FileWriter fileWriter;
		try {
			Path path = Paths.get(System.getProperty("user.home"), "Lords'n'Heroes", _worldName, "maps", _currentMap + ".tmx");
			fileWriter = new FileWriter(path.toString(), false);
			TmxMapWriter tmxWriter = new TmxMapWriter(fileWriter);
			tmxWriter.tmx(_tiledMap, Format.Base64Zlib);
			tmxWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private TiledMapTile getSelectedTile(int selectedCellIndex) {
		TiledMapTile selectedTile = null;
		
		TiledMapTileSets tileSets = _tiledMap.getTileSets();
        Iterator<TiledMapTileSet> it = tileSets.iterator();

        int count = 0;
        while(it.hasNext()) {
        	TiledMapTileSet tileSet = it.next();
        	Iterator<TiledMapTile> tileIt = tileSet.iterator();
        	
        	while(tileIt.hasNext()) {
        		selectedTile = tileIt.next();
        		
        		if (count == selectedCellIndex) {
        			break;
        		}
        		count++;
        	}

        	if (count == selectedCellIndex) {
    			break;
    		}
        }
		
        return selectedTile;
	}
}
