package com.lordhero.game;

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
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.math.Vector2;
import com.lordhero.game.controller.EntityController;
import com.lordhero.game.model.Npc;

import net.dermetfan.gdx.maps.tiled.TmxMapWriter;
import net.dermetfan.gdx.maps.tiled.TmxMapWriter.Format;

public class WorldMap {
    private static final float _playerSpeed = 150f;

    private Texture _cursorImage;
    private Sprite _cursorSprite;
    
    private int _xCursor;
    private int _yCursor;
    
	private Texture _playerImage;
	private Sprite _playerSprite;
    private TiledMap _tiledMap; 
    private OrthographicCamera _camera;
    private TiledMapRenderer _tiledMapRenderer;

    private SpriteBatch _spriteBatch;

    private boolean _leftBlocked = false;
    private boolean _upBlocked = false;
    private boolean _rightBlocked = false;
    private boolean _downBlocked = false;
    
    private String _currentMap;
    
    private boolean _collision = false;

    BufferedInputStream _inputStream;
    PrintWriter _outputStream;
    
    EntityController _entityController;
    
    ILord _lord;

	private ISelectedCellProvider _selectedCellProvider;
    
    public WorldMap(int xPos, int yPos, int width, int height) {
		_spriteBatch = new SpriteBatch();
		
		_playerImage = new Texture(Gdx.files.internal("My1stHero.png"));
		_playerSprite = new Sprite(_playerImage);
		
		_cursorImage = new Texture(Gdx.files.internal("cursor.png"));
		_cursorSprite = new Sprite(_cursorImage);
		
		_xCursor = 0;
		_yCursor = 0;
		
		_currentMap = "baseMap";
		
        _camera = new OrthographicCamera();
        _camera.setToOrtho(false, width, height);
        _camera.update();
        _camera.translate(new Vector2(200, 0));
        
        // connect to server to receive the main map
        connectToServer();
        loadRemoteMap();
                
    }
    
    public void setEntityController(EntityController entityController) {
    	_entityController = entityController;
    }
    
    public void setSelectedCellProvider(ISelectedCellProvider selectedCellProvider) {
    	_selectedCellProvider = selectedCellProvider;
    }
    
    public void setLord(ILord lord) {
    	_lord = lord;
    }
    
	public void render () {
		moveCamera();

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        _camera.update();
        
        _tiledMapRenderer.setView(_camera);
        _tiledMapRenderer.render();	
    
        _playerSprite.setCenter(_camera.position.x, _camera.position.y);
        _cursorSprite.setCenter(_xCursor + _camera.position.x - Gdx.graphics.getWidth() / 2, _yCursor + _camera.position.y - Gdx.graphics.getHeight() / 2);
        
        // render
        _spriteBatch.setProjectionMatrix(_camera.combined);
        _spriteBatch.begin();
        _entityController.render(_spriteBatch, _currentMap);
        _cursorSprite.draw(_spriteBatch);
        _playerSprite.draw(_spriteBatch);
        _spriteBatch.end();
        
        checkForCollision();                
	}

	private void moveCamera() {
		if (!_collision) {
			float delta = Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Input.Keys.UP) && !_upBlocked) {
	            _camera.translate(0, _playerSpeed * delta);			
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !_downBlocked) {
	            _camera.translate(0, _playerSpeed * -delta);			
			}
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !_leftBlocked) {
	            _camera.translate(_playerSpeed * -delta, 0);			
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !_rightBlocked) {
	            _camera.translate(_playerSpeed * delta, 0);			
			}
		}
	}

	private void connectToServer() {
        Socket socket;

		try {
			socket = new Socket("127.0.0.1", 12345);
	        _outputStream = new PrintWriter(socket.getOutputStream(), true);
	        _inputStream = new BufferedInputStream(socket.getInputStream());
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
	
	private void checkForCollision() {
        TiledMapTileLayer collisionObjectLayer = (TiledMapTileLayer)_tiledMap.getLayers().get("Collision");

        // get the player position
        int xPos = (int)(_camera.position.x / collisionObjectLayer.getTileWidth());
        int yPos = (int)(_camera.position.y / collisionObjectLayer.getTileHeight());                
        
        // check top
        _upBlocked = collisionObjectLayer.getCell(xPos, yPos+1) != null;
        _downBlocked = collisionObjectLayer.getCell(xPos, yPos-1) != null;
        _leftBlocked = collisionObjectLayer.getCell(xPos-1, yPos) != null;
        _rightBlocked = collisionObjectLayer.getCell(xPos+1, yPos) != null;
	}
	
	public void addNpc(int screenX, int screenY) {
		
	}

	public void setTile(int screenX, int screenY) {
        TiledMapTileLayer backgroundLayer = (TiledMapTileLayer)_tiledMap.getLayers().get(_selectedCellProvider.getLayerName());
   
        int[][] selectedCells = _selectedCellProvider.getSelectedCellIndexArray();
        int price = _selectedCellProvider.getSelectedCellPrice(selectedCells);
        
        if (_lord.pay(price)) {
            int width = selectedCells.length;
            int height = selectedCells[0].length;
                    
            for (int x = 0; x < width; x++) {
            	for (int y = 0; y < height; y++) {
                	int xCell = x + (int)((_xCursor + _camera.position.x - Gdx.graphics.getWidth() / 2) / backgroundLayer.getTileWidth());
                	int yCell = (int)((_yCursor + _camera.position.y - Gdx.graphics.getHeight() / 2) / backgroundLayer.getTileHeight()) - y;

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

	public void setCursorPosition(int screenX, int screenY) {
        _xCursor = screenX / 32 * 32;
        
        _yCursor = Gdx.graphics.getHeight() - screenY / 32 * 32;		
	}

	public void dispose() {
		writeCurrentMap();
		_playerImage.dispose();
		_tiledMap.dispose();		
	}

	private void writeCurrentMap() {
		FileWriter fileWriter;
		try {
			Path path = Paths.get(System.getProperty("user.home"), "Lords'n'Heroes", "maps", _currentMap + ".tmx");
			fileWriter = new FileWriter(path.toString(), false);
			TmxMapWriter tmxWriter = new TmxMapWriter(fileWriter);
			tmxWriter.tmx(_tiledMap, Format.Base64Zlib);
			tmxWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void enter() {
        MapLayer layer = (MapLayer)_tiledMap.getLayers().get("Buildings");

        MapObjects objects = layer.getObjects();
		
		for (int i = 0; i < objects.getCount(); i++) {
			RectangleMapObject mapObject = (RectangleMapObject)objects.get(i);
			
			if (Math.abs(mapObject.getRectangle().x -_camera.position.x) < 32f &&
				Math.abs(mapObject.getRectangle().y -_camera.position.y) < 32f) {
				_currentMap = (String)mapObject.getProperties().get("TargetMap");
				
				_camera.position.x = Integer.parseInt(mapObject.getProperties().get("StartX").toString());
				_camera.position.y = Integer.parseInt(mapObject.getProperties().get("StartY").toString());
				
				loadRemoteMap();		        
		        
				break;
			}
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
