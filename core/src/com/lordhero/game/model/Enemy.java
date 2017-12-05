package com.lordhero.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy implements IEntity {
	private static final double MinTargetDistance = 5.0;

	private static final double EnemySpeed = 2.5;

	private static Texture _monsterTileSet = new Texture(Gdx.files.internal("data/tileset/Monsters.png"));
		
	private Sprite _enemySprite;
	
	private double _xPos;
	private double _yPos;
	
	private int _xEndPos;
	private int _yEndPos;
	
	private boolean _remove = false;
	
	public Enemy() {
		 TextureRegion region = new TextureRegion(_monsterTileSet, 0, 0, 32, 32);
		 
		 _enemySprite = new Sprite(region);
		 
		 _xPos = getRandomStartPosition();
		 _yPos = getRandomStartPosition();
		 
		 _xEndPos = getRandomStartPosition();
		 _yEndPos = getRandomStartPosition();
		 
		 _enemySprite.setCenter((float)_xPos, (float)_yPos);
	}
	
	@Override
	public void update() {
		if (Math.abs(_xPos - _xEndPos) < Math.abs(_yPos - _yEndPos)) {
			if (_yPos < _yEndPos) {
				_yPos += EnemySpeed;
			}
			else {
				_yPos -= EnemySpeed;
			}
		}
		
		else {
			if (_xPos < _xEndPos) {
				_xPos += EnemySpeed;
			}
			else {
				_xPos -= EnemySpeed;
			}
		}
		
		if ((Math.abs(_xPos - _xEndPos) * Math.abs(_xPos - _xEndPos) + Math.abs(_yPos - _yEndPos) * Math.abs(_yPos - _yEndPos)) < MinTargetDistance) {
			_remove = true;
		}
		
		_enemySprite.setCenter((float)_xPos, (float)_yPos);
	}
	
	public void render(SpriteBatch spriteBatch) {
		if (Math.abs(_xPos - _xEndPos) < Math.abs(_yPos - _yEndPos)) {
			if (_yPos < _yEndPos) {
				_yPos += EnemySpeed;
			}
			else {
				_yPos -= EnemySpeed;
			}
		}
		
		else {
			if (_xPos < _xEndPos) {
				_xPos += EnemySpeed;
			}
			else {
				_xPos -= EnemySpeed;
			}
		}
		
		if ((Math.abs(_xPos - _xEndPos) * Math.abs(_xPos - _xEndPos) + Math.abs(_yPos - _yEndPos) * Math.abs(_yPos - _yEndPos)) < MinTargetDistance) {
			_remove = true;
		}
		
		_enemySprite.setCenter((float)_xPos, (float)_yPos);		
		_enemySprite.draw(spriteBatch);
	}

	public boolean isTerminated() {
		return _remove;		
	}

	private int getRandomStartPosition() {
		return (int) (Math.random() * 128 * 32);
	}

	@Override
	public Sprite getSprite() {
		// TODO Auto-generated method stub
		return _enemySprite;
	}
}
