package com.lordhero.game;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.lordhero.game.model.INpc;
import com.lordhero.game.model.items.IItem;
import com.lordhero.game.model.items.IWeapon;
import com.lordhero.game.model.items.RangeWeapon;
import com.lordhero.game.model.items.Weapon;

public class Player implements IPlayer {
    private static final float PlayerSpeed = 150f;
    private static final float LordSpeed = 300;
    
    private int _money = 10000;
	
	private LinkedList<ChangeListener> _lordChangedListeners;
	
	private float _xPos;
	private float _yPos;
		
	private boolean _upBlocked;
	private boolean _downBlocked;
	private boolean _leftBlocked;
	private boolean _rightBlocked;
	
	private float _viewAngleDeg;
	
	private List<IItem> _items;

	private Weapon _weapon;

	private RangeWeapon _rangedWeapon;
		
	public Player() {
		_lordChangedListeners = new LinkedList<ChangeListener>();
		_items = new LinkedList<IItem>();
		
		_xPos = 30;
		_yPos = 30;
		
		_weapon = Weapon.Create(0);
		_rangedWeapon = null;
		
		_items.add(_weapon);
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
		return PlayerSpeed;
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
	public void setViewAngle(float viewAngle) {
		_viewAngleDeg = viewAngle;
	}

	@Override
	public void moveAbsolute(Direction direction) {
		float delta = Gdx.graphics.getDeltaTime();

		if (direction == Direction.Up && !_upBlocked) {
            _yPos += LordSpeed * delta;
		}
		if (direction == Direction.Down && !_downBlocked) {
            _yPos -= LordSpeed * delta;
		}
		if (direction == Direction.Left && !_leftBlocked) {
            _xPos -= LordSpeed * delta;
		}
		if (direction == Direction.Right && !_rightBlocked) {
            _xPos += LordSpeed * delta;
		}
	}

	@Override
	public void moveDirection(Direction direction) {
		Matrix3 a_rotMatrix = new Matrix3();
		a_rotMatrix.idt();
		
		float delta = Gdx.graphics.getDeltaTime();

		Vector2 a_velocityVec = new Vector2(0f, 1f);
		
		float viewAngleDeg = _viewAngleDeg;
		
		if (direction == Direction.Down) {
			viewAngleDeg += 180f;
		}
		if (direction == Direction.Left) {
			viewAngleDeg += 90f;
		}
		if (direction == Direction.Right) {
			viewAngleDeg += 270f;
		}

		a_rotMatrix.setToRotation(viewAngleDeg);			
		a_velocityVec.mul(a_rotMatrix);
		a_velocityVec.scl(getVelocity()*delta);
		
		if (a_velocityVec.x > 0 && _rightBlocked) {
			a_velocityVec.x = 0;
		}
		if (a_velocityVec.x < 0 && _leftBlocked) {
			a_velocityVec.x = 0;
		}		
		if (a_velocityVec.y > 0 && _upBlocked) {
			a_velocityVec.y = 0;
		}
		if (a_velocityVec.y < 0 && _downBlocked) {
			a_velocityVec.y = 0;
		}

		_xPos += a_velocityVec.x;
		_yPos += a_velocityVec.y;
	}

	@Override
	public void registerChangeListener(ChangeListener listener) {
		_lordChangedListeners.add(listener);
	}

	@Override
	public void addItem(IItem item) {
		_money -= item.getPrice();
		_items.add(item);
	}

	@Override
	public List<IItem> getItems() {
		return _items;
	}

	@Override
	public void setWeapon(Weapon weapon) {
		_weapon = weapon;	
	}
	
	@Override 
	public IWeapon getWeapon() {
		return _weapon;
	}

	@Override
	public void setRangedWeapon(RangeWeapon rangeWeapon) {
		_rangedWeapon = rangeWeapon;
		
	}

	private void fireChangeEvent() {
		for (ChangeListener listener : _lordChangedListeners) {
			listener.changed(null, null);
		}
	}

	@Override
	public float getRotation() {
		return _viewAngleDeg;
	}

}
