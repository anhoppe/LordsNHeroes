package com.lordhero.game.model;

import java.io.IOException;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.model.items.Weapon;

public abstract class CreatureBase extends EntityBase implements ICreature {

	private static final int InRangeDistance = 100;

	protected float _viewDirectionDeg;

	protected Weapon _weapon = Weapon.Create(0);

	public CreatureBase() {
		
	}
	
	public CreatureBase(XmlReader.Element creatureBaseNode) {
		super(creatureBaseNode.getChildByName("EntityBase"));
		readCreatureBase(creatureBaseNode);
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
		writer.element("CreatureBase").
		attribute("ViewDirection", Float.toString(_viewDirectionDeg));
		super.write(writer);
		writer.pop();
	}	
	
	public void readCreatureBase(Element creatureBaseNode) {
		super.readEntityBase(creatureBaseNode.getChildByName("EntityBase"));
		_viewDirectionDeg = creatureBaseNode.getFloatAttribute("ViewDirection");
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
}
