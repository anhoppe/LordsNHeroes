package com.lordhero.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy {
	private static Texture _monsterTileSet = new Texture(Gdx.files.internal("data/tileset/Monsters.png"));
	private Sprite _enemySprite;
	
	private int _xPos;
	private int _yPos;
	
	public Enemy() {
		 TextureRegion region = new TextureRegion(_monsterTileSet, 0, 0, 32, 32);
		 
		 _enemySprite = new Sprite(region);
		 
		 _xPos = (int) (64 * 32 - Math.random() * 128 * 32);
		 _yPos = (int) (64 * 32 - Math.random() * 128 * 32);
		 
		 _enemySprite.setCenter(_xPos,  _yPos);
	}
	
	public void render(SpriteBatch spriteBatch) {
		_enemySprite.draw(spriteBatch);
	}
}
