package com.lordhero.game.model;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class EntityBase implements IEntity, Serializable {

	protected transient Sprite _sprite;
	
	protected double _xPos;
	protected double _yPos;

	@Override
	public boolean isTerminated() {
		return false;
	}

	@Override
	public Sprite getSprite() {		
		return _sprite;
	}

	@Override
	public boolean isAt(int xPos, int yPos) {
		if (Math.abs(xPos - _xPos) <= 32  &&
			Math.abs(yPos - _yPos) <= 32) {
			return true;
		}
		
		return false;
	}
}
