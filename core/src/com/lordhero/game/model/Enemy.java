package com.lordhero.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends EntityBase {
	private static final double MinTargetDistance = 5.0;

	private static final double EnemySpeed = 2.5;

	private static Texture _monsterTileSet = new Texture(Gdx.files.internal("data/tileset/Monsters.png"));
	
	private int _xEndPos;
	private int _yEndPos;
	
	private boolean _remove = false;
	
	public Enemy() {
		 TextureRegion region = new TextureRegion(_monsterTileSet, 0, 0, 32, 32);
		 
		 _sprite = new Sprite(region);
		 
		 _xPos = getRandomStartPosition();
		 _yPos = getRandomStartPosition();
		 
		 _xEndPos = getRandomStartPosition();
		 _yEndPos = getRandomStartPosition();
		 
		 _sprite.setCenter((float)_xPos, (float)_yPos);
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
		
		_sprite.setCenter((float)_xPos, (float)_yPos);
	}
	
	/*public void render(SpriteBatch spriteBatch) {
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
		
		_sprite.setCenter((float)_xPos, (float)_yPos);		
		_sprite.draw(spriteBatch);
	}*/

	public boolean isTerminated() {
		return _remove;		
	}

	private int getRandomStartPosition() {
		return (int) (Math.random() * 128 * 32);
	}
}
