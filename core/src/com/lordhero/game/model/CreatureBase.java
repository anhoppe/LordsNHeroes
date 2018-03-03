package com.lordhero.game.model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.model.items.RangeWeapon;
import com.lordhero.game.model.items.MeleeWeapon;

public abstract class CreatureBase extends EntityBase implements ICreature {

	private static final int InRangeDistance = 100;
	
	private List<Missile> _activeMissiles = new LinkedList<Missile>();

	protected float _viewDirectionDeg;

	protected MeleeWeapon _meleeWeapon = MeleeWeapon.Create(0);

	protected RangeWeapon _rangedWeapon;

	protected boolean _isMeleeActiveWeapon = true;

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
		
		if (_meleeWeapon != null && _meleeWeapon.attacks()) {
			weaponAnimation = _meleeWeapon.getWeaponAnimation();
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
	
	protected void addActiveMissile(Missile missile) {
		_activeMissiles.add(missile);
	}
	
	protected List<HitInfo> getHitInfos() {
		List<HitInfo> hitInfos = new LinkedList<HitInfo>();
		
		if (_meleeWeapon != null && _meleeWeapon.attacks()) {
			Vector2 vec = new Vector2(0, 1);
			Matrix3 rotMatrix = new Matrix3();
			rotMatrix.idt();
			
			rotMatrix.setToRotation(_viewDirectionDeg);
			vec.mul(rotMatrix);
			vec.scl(_meleeWeapon.getRange());
			
			hitInfos.add(new HitInfo((int)(_xPos + vec.x), (int)(_yPos + vec.y), _meleeWeapon));			
		}		
		
		boolean removedMissile = false;
		
		do {
			removedMissile = false;
			for (Missile missile : _activeMissiles) {
				if (missile.isTerminated()) {
					_activeMissiles.remove(missile);
					removedMissile = true;
					break;
				}
			}			
		} while(removedMissile);
				
		for (Missile missile : _activeMissiles) {
			hitInfos.add(new HitInfo((int)missile.getX(), (int)missile.getY(), missile));
		}
		
		return hitInfos;
	}
}
