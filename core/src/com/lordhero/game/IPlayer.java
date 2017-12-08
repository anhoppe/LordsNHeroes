package com.lordhero.game;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public interface IPlayer {
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
	void move(Direction direction);
	void setPosition(float x, float y);
	
	int getMoney();
	
	boolean pay(int price);
	
	void registerChangeListener(ChangeListener listener);

}
