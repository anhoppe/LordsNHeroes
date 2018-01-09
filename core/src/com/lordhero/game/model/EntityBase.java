package com.lordhero.game.model;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class EntityBase implements IEntity, Serializable {

	private static final int InRangeDistance = 100;

	private static final int IsAtDistance = 32;

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
		return distanceTo(xPos, yPos) < IsAtDistance;
	}
	
	@Override 
	public boolean isInRange(int xPos, int yPos) {
		return distanceTo(xPos, yPos) < InRangeDistance;
	}

	private int distanceTo(int xPos, int yPos) {
		int xDist = (int) Math.abs(xPos - _xPos);
		int yDist = (int) Math.abs(yPos - _yPos);
			
		return Math.max(xDist,  yDist);
	}

}
