package com.lordhero.game.view;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.lordhero.game.IPlayer;
import com.lordhero.game.controller.EntityController;
import com.lordhero.game.controller.MapController;
import com.lordhero.game.model.IEntities;
import com.lordhero.game.model.IEntity;
import com.lordhero.game.model.IMap;

public class WorldMap {

    private Texture _cursorImage;
    private Sprite _cursorSprite;
    
	private Texture _playerImage;
	private Sprite _playerSprite;

    private SpriteBatch _spriteBatch;
        
    private IPlayer _player;
    private IMap _map;
    private IEntities _entities;
    
	private OrthographicCamera _camera;

    public WorldMap(int width, int height) {
        _camera = new OrthographicCamera();
        _camera.setToOrtho(false, width, height);
        _camera.update();
        _camera.translate(new Vector2(200, 0));        

        _spriteBatch = new SpriteBatch();
		
		_playerImage = new Texture(Gdx.files.internal("My1stHero.png"));
		_playerSprite = new Sprite(_playerImage);
		
		_cursorImage = new Texture(Gdx.files.internal("cursor.png"));
		_cursorSprite = new Sprite(_cursorImage);		
    }
    
    public void setPlayer(IPlayer player) {
    	_player = player;
    }
    
    public void setMap(IMap map) {
    	_map = map;
    }

    public void setEntities(IEntities entities) {
    	_entities = entities;
    }
    
	public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float xCamera = _player.getX();
        float yCamera = _player.getY();
        int xCursor = _map.getCursorX();
        int yCursor = _map.getCursorY();

        _camera.position.x = xCamera;
        _camera.position.y = yCamera;

        _camera.update();
        TiledMapRenderer tiledMapRenderer = _map.getTiledMapRenderer();        
        tiledMapRenderer.setView(_camera);
        tiledMapRenderer.render();    
           
        // render
        _spriteBatch.setProjectionMatrix(_camera.combined);
        _spriteBatch.begin();

		List<IEntity> entitiesOnSite = _entities.getEntitiesOnSite();
		
		if (entitiesOnSite != null) {
			for (IEntity entity : entitiesOnSite) {					
				Sprite sprite = entity.getSprite();
				sprite.draw(_spriteBatch);
			}		
		}

//      _cursorSprite.setCenter(xCursor + xCamera - Gdx.graphics.getWidth() / 2, yCursor + yCamera - Gdx.graphics.getHeight() / 2);
      _cursorSprite.setCenter(xCursor, yCursor);
		
        _cursorSprite.draw(_spriteBatch);

        _playerSprite.setCenter(xCamera, yCamera);
        _playerSprite.draw(_spriteBatch);
        _spriteBatch.end();        
	}

	public void dispose() {
		_playerImage.dispose();
	}
}
