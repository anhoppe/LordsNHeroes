package com.lordhero.game;

import java.util.Vector;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
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
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

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
    
    private boolean _collision = false;

    ICellSelector _cellSelector;
    
    public WorldMap(int xPos, int yPos, int width, int height) {
		_spriteBatch = new SpriteBatch();
		_playerImage = new Texture(Gdx.files.internal("My1stHero.png"));
		_playerSprite = new Sprite(_playerImage);
		
		_cursorImage = new Texture(Gdx.files.internal("cursor.png"));
		_cursorSprite = new Sprite(_cursorImage);
		
		_xCursor = 0;
		_yCursor = 0;
		
        _camera = new OrthographicCamera();
        _camera.setToOrtho(false, width, height);
        _camera.update();
        _camera.translate(new Vector2(200, 0));
        _tiledMap = new TmxMapLoader().load("baseMap.tmx");
        
        _tiledMapRenderer = new OrthogonalTiledMapRenderer(_tiledMap);                
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
        float teest = Gdx.graphics.getWidth();
        _cursorSprite.setCenter(_xCursor + _camera.position.x - Gdx.graphics.getWidth() / 2, _yCursor + _camera.position.y - Gdx.graphics.getHeight() / 2);
        
        // render
        _spriteBatch.setProjectionMatrix(_camera.combined);
        _spriteBatch.begin();
        _cursorSprite.draw(_spriteBatch);
        _playerSprite.draw(_spriteBatch);
        _spriteBatch.end();
        
        checkForCollision();                
	}
	
	public void setCellSelector(ICellSelector cellSelector) {
		_cellSelector = cellSelector;
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

	public void setTile(int screenX, int screenY) {
        TiledMapTileLayer backgroundLayer = (TiledMapTileLayer)_tiledMap.getLayers().get("Background");
        
    	int x = (int)((_xCursor + _camera.position.x - Gdx.graphics.getWidth() / 2) / backgroundLayer.getTileWidth());
    	int y = (int)((_yCursor + _camera.position.y - Gdx.graphics.getHeight() / 2) / backgroundLayer.getTileHeight());
        
    	TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
    	cell.setTile(new StaticTiledMapTile(_cellSelector.getSelectedCellTextureRegion()));
    	backgroundLayer.setCell(x, y, cell);
	}

	public void setCursorPosition(int screenX, int screenY) {
        _xCursor = screenX / 32 * 32;
        
        _yCursor = Gdx.graphics.getHeight() - screenY / 32 * 32;		
	}

	public void dispose() {
		_playerImage.dispose();
		_tiledMap.dispose();		
	}

	public void enter() {
        MapLayer layer = (MapLayer)_tiledMap.getLayers().get("Buildings");

        int xPos = (int)(_camera.position.x / 32);
        int yPos = (int)(_camera.position.y / 32);                

		MapObjects objects = layer.getObjects();
		
		for (int i = 0; i < objects.getCount(); i++) {
			RectangleMapObject mapObject = (RectangleMapObject)objects.get(i);
			
			if (Math.abs(mapObject.getRectangle().x -_camera.position.x) < 32f &&
				Math.abs(mapObject.getRectangle().y -_camera.position.y) < 32f) {
				String targetMap = (String)mapObject.getProperties().get("TargetMap");
				
				_camera.position.x = (Integer)mapObject.getProperties().get("StartX");
				_camera.position.y = (Integer)mapObject.getProperties().get("StartY");
				
		        _tiledMap = new TmxMapLoader().load(targetMap + ".tmx");
		        
		        _tiledMapRenderer = new OrthogonalTiledMapRenderer(_tiledMap);                
				break;
			}
		}
	}
}
