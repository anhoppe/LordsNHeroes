package com.lordhero.game.model;

import java.io.IOException;
import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.model.items.Weapon;

public abstract class EntityBase implements IEntity {

	private static final int InRangeDistance = 100;

	private static final int IsAtDistance = 32;

	protected Sprite _sprite;
	
	protected float _xPos;
	protected float _yPos;

	protected float _viewDirectionDeg;

	protected Weapon _weapon = Weapon.Create(0);

	public EntityBase() {
		
	}
	
	public EntityBase(Element entityBaseNode) {
		read(entityBaseNode);
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
	public boolean isAt(int xPos, int yPos) {
		return distanceTo(xPos, yPos) < IsAtDistance;
	}
	
	@Override 
	public boolean isInRange(int xPos, int yPos) {
		return distanceTo(xPos, yPos) < InRangeDistance;
	}	

	@Override
	public TextureRegion getWeaponAnimationFrame() {
		TextureRegion weaponAnimation = null;
		
		if (_weapon != null && _weapon.attacks()) {
			weaponAnimation = _weapon.getWeaponAnimation();
		}
		
		return weaponAnimation;
	}
	
	@Override
	public float getRotation() {
		return _viewDirectionDeg;
	}
	
	@Override
	public void write(XmlWriter writer) throws IOException {
		writer.element("EntityBase").
		attribute("XPos", Float.toString(_xPos)).
		attribute("YPos", Float.toString(_yPos)).
		attribute("ViewDirection", Float.toString(_viewDirectionDeg));
		writer.pop();
	}	
	
	protected Vector2 getHitPosition() {
		Vector2 hitPosition = null;
		
		if (_weapon != null && _weapon.attacks()) {
			Vector2 vec = new Vector2(0, 1);
			Matrix3 rotMatrix = new Matrix3();
			rotMatrix.idt();
			
			rotMatrix.setToRotation(_viewDirectionDeg);
			vec.mul(rotMatrix);
			vec.scl(_weapon.getRange());
			
			hitPosition = new Vector2(_xPos + vec.x, _yPos + vec.y);
		}		
		
		return hitPosition;
	}

	private int distanceTo(int xPos, int yPos) {
		int xDist = (int) Math.abs(xPos - _xPos);
		int yDist = (int) Math.abs(yPos - _yPos);
			
		return Math.max(xDist,  yDist);
	}

	public void read(Element entityBaseNode) {
		if (entityBaseNode != null) {
			_xPos = entityBaseNode.getFloatAttribute("XPos");
			_yPos = entityBaseNode.getFloatAttribute("YPos");
			_viewDirectionDeg = entityBaseNode.getFloatAttribute("ViewDirection");
		}		
	}
}
