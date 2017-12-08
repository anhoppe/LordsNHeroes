package com.lordhero.game;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Player implements IPlayer {
    private static final float _playerSpeed = 150f;

    private int _money = 10000;
	
	private LinkedList<ChangeListener> _lordChangedListeners;
	
	private float _xPos;
	private float _yPos;
	
	private boolean _upBlocked;
	private boolean _downBlocked;
	private boolean _leftBlocked;
	private boolean _rightBlocked;
		
	public Player() {
		_lordChangedListeners = new LinkedList<ChangeListener>();
		
		_xPos = 30;
		_yPos = 30;
	}
	
	@Override
	public int getMoney() {
		return _money;
	}
	
	@Override
	public boolean pay(int price) {
		boolean hasPayed = false;
		
		if (_money > price) {
			_money -= price;
			hasPayed = true;
			
			fireChangeEvent();
		}
		
		return hasPayed;
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
	public float getVelocity() {
		return _playerSpeed;
	}

	@Override
	public void setPosition(float x, float y) {
		_xPos = x;
		_yPos = y;	
	}

	@Override
	public void setCollisions(boolean upBlocked, boolean downBlocked, boolean leftBlocked, boolean rightBlocked) {
		_upBlocked = upBlocked;
		_downBlocked = downBlocked;
		_leftBlocked = leftBlocked;
		_rightBlocked = rightBlocked;
	}

	@Override
	public void move(Direction direction) {
		// TODO Auto-generated method stub
		float delta = Gdx.graphics.getDeltaTime();

		if (direction == Direction.Up && !_upBlocked) {
            _yPos += getVelocity() * delta;
		}
		if (direction == Direction.Down && !_downBlocked) {
            _yPos -= getVelocity() * delta;
		}
		if (direction == Direction.Left && !_leftBlocked) {
            _xPos -= getVelocity() * delta;
		}
		if (direction == Direction.Right && !_rightBlocked) {
            _xPos += getVelocity() * delta;
		}
	}

	@Override
	public void registerChangeListener(ChangeListener listener) {
		_lordChangedListeners.add(listener);
	}
	
	private void fireChangeEvent() {
		for (ChangeListener listener : _lordChangedListeners) {
			listener.changed(null, null);
		}
	}
}
