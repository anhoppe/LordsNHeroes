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

public class MeleeWeapon extends ItemBase implements IMeleeWeapon {
	
	Dice _dice = new Dice(1, 6, 2);

	public static MeleeWeapon[] MeleeWeaponTemplates = new MeleeWeapon[] {
		new MeleeWeapon("Knife", Type.Knife, 100, 25),
		new MeleeWeapon("Dagger", Type.Dagger, 150, 25),
		new MeleeWeapon("Axe", Type.Axe, 1200, 25),
		new MeleeWeapon("Saber", Type.Saber, 1500, 25),
		new MeleeWeapon("Sword", Type.Sword, 3500, 25),
		new MeleeWeapon("TwoHander", Type.TwoHander, 5000, 25)
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
	
	public MeleeWeapon(String name, Type type, int price, int range) {
		_name = name;
		_type = type;
		_price = price;
		_range = range;
	}
	
	public MeleeWeapon(MeleeWeapon weapon) {
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

	public static MeleeWeapon Create() {
		return Create(ThreadLocalRandom.current().nextInt(0, MeleeWeaponTemplates.length - 1));		
	}
	
	public static MeleeWeapon Create(int index) {
		return new MeleeWeapon(MeleeWeaponTemplates[index]);
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
	public int getDamage() {
		int damage = 0;
		if (!_hasHit) {
			damage = _dice.roll();
		}
		
		return damage;
	}
	
	@Override
	public void write(XmlWriter writer) throws IOException {
		writer.element("Weapon").attribute("Type", _type).attribute("Range", _range);
		super.write(writer);
		writer.pop();
	}

	@Override
	public void setHasHit() {
		_hasHit = true;
	}
}
