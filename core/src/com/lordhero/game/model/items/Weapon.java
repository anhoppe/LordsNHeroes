package com.lordhero.game.model.items;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.model.Dice;

public class Weapon extends ItemBase implements IWeapon {
	
	Dice _dice = new Dice(1, 6, 2);

	public static Weapon[] WeaponTemplates = new Weapon[] {
		new Weapon("Knife", Type.Knife, 100, 25),
		new Weapon("Dagger", Type.Dagger, 150, 25),
		new Weapon("Axe", Type.Axe, 1200, 25),
		new Weapon("Saber", Type.Saber, 1500, 25),
		new Weapon("Sword", Type.Sword, 3500, 25),
		new Weapon("TwoHander", Type.TwoHander, 5000, 25)
	};
	
	enum Type {
		None,
		Knife,
		Dagger,
		Axe,
		Saber,
		Sword,
		TwoHander
	}
	
	private static final float HitFrameRate = 1f / 8f; 

	private Type _type;	
		
	private int _range;

	private Texture _thrustImage;	
	private Animation<TextureRegion> _hitAnimation;	
	private boolean _isAttacking;
	private boolean _hasHit;
	
    private float _elapsedTime;
	
	public Weapon(String name, Type type, int price, int range) {
		_name = name;
		_type = type;
		_price = price;
		_range = range;
	}
	
	public Weapon(Weapon weapon) {
		_name = weapon._name;
		_type = weapon._type;
		_price = weapon._price;
		_range = weapon._range;
		
		// Create the weapons animation object		
		_thrustImage = new Texture(Gdx.files.internal("Thrust.png"));
			 
		TextureRegion[][] tmpFrames = TextureRegion.split(_thrustImage, 32, 32);
		Array<TextureRegion> animationArray = new Array<TextureRegion>();
		for (int x = 0; x < tmpFrames.length; x++) {
			for (int y = 0; y < tmpFrames[x].length; y++) {
				animationArray.add(tmpFrames[x][y]);
			}
		}
		
		_hitAnimation = new Animation<TextureRegion>(HitFrameRate, animationArray);		
	}

	public static Weapon Create() {
		return Create(ThreadLocalRandom.current().nextInt(0, WeaponTemplates.length - 1));		
	}
	
	public static Weapon Create(int index) {
		return new Weapon(WeaponTemplates[index]);
	}

	@Override
	public String getName() {
		return _name;
	}

	public float getRange() {
		return _range;
	}
	
	@Override
	public void startAttack() {
		_isAttacking = true;
		_hasHit = false;
		_elapsedTime = 0f;
	}
	
	@Override
	public TextureRegion getWeaponAnimation() {
		_elapsedTime += Gdx.graphics.getDeltaTime();
				
		if (_hitAnimation.isAnimationFinished(_elapsedTime)) {
			_isAttacking = false;
		}
		
		return _hitAnimation.getKeyFrame(_elapsedTime);
	}

	@Override
	public boolean attacks() {		
		return _isAttacking;
	}

	@Override
	public int hit() {
		int damage = 0;
		if (!_hasHit) {
			damage = _dice.roll();
			_hasHit = true;
		}
		
		return damage;
	}
	
	@Override
	public void write(XmlWriter writer) throws IOException {
		writer.element("Weapon").attribute("Type", _type).attribute("Range", _range);
		super.write(writer);
		writer.pop();
	}
}
