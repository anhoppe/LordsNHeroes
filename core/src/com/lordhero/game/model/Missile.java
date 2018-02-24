package com.lordhero.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.lordhero.game.IGameMode;

public class Missile extends EntityBase implements INonPlayer {
	private static final float Range = 500f;	
	private static final float Speed = 12f;

	private static Texture _arrowTileSet = new Texture(Gdx.files.internal("arrow.png"));

	private float _xStart;
	private float _yStart;
	
	private Vector2 _directionVec;

	private float _directionDeg;

	public Missile(float xPos, float yPos, float viewDirectionDeg, Dice damage) {
		super(xPos, yPos);
		_xStart = xPos;
		_yStart = yPos;
		
		_directionDeg = viewDirectionDeg;
		_directionVec = new Vector2(0f, 1f);
		_directionVec.rotate(viewDirectionDeg);
		_directionVec.scl(Speed);
		
		restore();
	}

	@Override
	public void update(IPlayer player, IGameMode gameMode) {
		_xPos += _directionVec.x;
		_yPos += _directionVec.y;
		_sprite.setCenter(_xPos, _yPos);
		_sprite.setRotation(_directionDeg);
	}

	@Override
	public void restore() {
		TextureRegion region = new TextureRegion(_arrowTileSet, 0, 0, 32, 32);
		 
		_sprite = new Sprite(region);
		 
		_sprite.setCenter((float)_xPos, (float)_yPos);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTerminated() {
		return ((_xPos-_xStart)*(_xPos-_xStart) + (_yPos-_yStart)*(_yPos-_yStart)) > Range*Range;
	}

}
