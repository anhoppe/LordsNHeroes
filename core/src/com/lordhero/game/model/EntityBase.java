package com.lordhero.game.model;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public abstract class EntityBase implements IEntity {

	private static final int IsAtDistance = 32;

	protected Sprite _sprite;
	
	protected float _xPos;
	protected float _yPos;


	public EntityBase() {
		
	}
	
	public EntityBase(Element entityBaseNode) {
		readEntityBase(entityBaseNode);
	}

	public EntityBase(float xPos, float yPos) {
		_xPos = xPos;
		_yPos = yPos;
	}

	@Override
	public Sprite getSprite() {		
		return _sprite;
	}
	
	@Override
	public float getX() {
		return _xPos;
	}
	
	@Override
	public float getY() {
		return _yPos;
	}

	@Override
	public boolean isAt(float xPos, float yPos) {
		return distanceTo(xPos, yPos) < IsAtDistance;
	}

	@Override
	public void write(XmlWriter writer) throws IOException {
		writer.element("EntityBase").
		attribute("XPos", Float.toString(_xPos)).
		attribute("YPos", Float.toString(_yPos));
		writer.pop();
	}	
	
	protected float distanceTo(float xPos, float yPos) {
		float xDist = Math.abs(xPos - _xPos);
		float yDist = Math.abs(yPos - _yPos);
			
		return Math.max(xDist,  yDist);
	}

	public void readEntityBase(Element entityBaseNode) {
		if (entityBaseNode != null) {
			_xPos = entityBaseNode.getFloatAttribute("XPos");
			_yPos = entityBaseNode.getFloatAttribute("YPos");
		}		
	}
}
