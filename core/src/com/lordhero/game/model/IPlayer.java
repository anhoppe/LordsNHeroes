package com.lordhero.game.model;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.lordhero.game.model.items.IItem;
import com.lordhero.game.model.items.IMeleeWeapon;
import com.lordhero.game.model.items.IWeapon;
import com.lordhero.game.model.items.MeleeWeapon;
import com.lordhero.game.model.items.RangeWeapon;

public interface IPlayer extends ICreature {
	public enum Direction {
		None,
		Up,
		Right,
		Down,
		Left
	}

	float getVelocity();
	
	void setCollisions(boolean upBlocked, boolean downBlocked, boolean leftBlocked, boolean rightBlocked);

	void setViewAngle(float angleDeg);
	
	void moveAbsolute(Direction direction);
	
	void setPosition(float x, float y);

	int getHitPoints();
	
	int getMoney();
	
	boolean pay(int price);
	
	void registerChangeListener(ChangeListener listener);
	
	boolean addItem(IItem item, boolean playerHasToBuy);

	void moveDirection(Direction up);

	void startAttack(IEntityFactory entityFactory);

	void evaluateAttack(IEntities _entities);

	TextureRegion getWeaponAnimationFrame();

	void hit(int x, int y, IWeapon weapon);

	void addXp(int xp);

	int getXp();

	int getLevel();

	void assignItemToSlot(int slotIndex, int itemindex);

	void useItemInSlot(int slotIndex);
}
