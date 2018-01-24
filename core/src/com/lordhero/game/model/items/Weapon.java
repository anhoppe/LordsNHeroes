package com.lordhero.game.model.items;

import java.util.concurrent.ThreadLocalRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class Weapon extends ItemBase implements IWeapon {

	public static Weapon[] WeaponTemplates = new Weapon[] {
		new Weapon("Knife", Type.Knife, 100),
		new Weapon("Dagger", Type.Dagger, 150),
		new Weapon("Axe", Type.Axe, 1200),
		new Weapon("Saber", Type.Saber, 1500),
		new Weapon("Sword", Type.Sword, 3500),
		new Weapon("TwoHander", Type.TwoHander, 5000)
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
	
	private static final float HitFrameRate = 1f / 5f; 
	private static final float HitFrameCount = 5;
	
    float _elapsedTime;

	private Type _type;	
	
	private Texture _thrustImage;	
	private Animation<TextureRegion> _hitAnimation;
	
	private boolean _isAttacking;
	
	public Weapon(String name, Type type, int price) {
		_name = name;
		_type = type;
		_price = price;
	}
	
	public Weapon(Weapon weapon) {
		_name = weapon._name;
		_type = weapon._type;
		_price = weapon._price;
		
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
	
	@Override
	public void startAttack() {
		_isAttacking = true;
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

}
