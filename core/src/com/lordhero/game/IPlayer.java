package com.lordhero.game;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.lordhero.game.model.IEntities;
import com.lordhero.game.model.IMovable;
import com.lordhero.game.model.items.IItem;
import com.lordhero.game.model.items.IWeapon;
import com.lordhero.game.model.items.RangeWeapon;
import com.lordhero.game.model.items.Weapon;

public interface IPlayer extends IMovable {
	public enum Direction {
		None,
		Up,
		Right,
		Down,
		Left
	}

	float getX();
	
	float getY();
	
	float getVelocity();
	
	void setCollisions(boolean upBlocked, boolean downBlocked, boolean leftBlocked, boolean rightBlocked);

	void setViewAngle(float angleDeg);
	
	void moveAbsolute(Direction direction);
	
	void setPosition(float x, float y);
	
	int getMoney();
	
	boolean pay(int price);
	
	void registerChangeListener(ChangeListener listener);
	
	void addItem(IItem item);
	
	List<IItem> getItems();

	IWeapon getWeapon();

	void setWeapon(Weapon weapon);

	void setRangedWeapon(RangeWeapon rangeWeapon);

	void moveDirection(Direction up);

	void startAttack();

	void evaluateAttack(IEntities _entities);

	TextureRegion getWeaponAnimationFrame();

}
