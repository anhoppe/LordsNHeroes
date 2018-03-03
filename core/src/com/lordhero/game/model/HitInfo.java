package com.lordhero.game.model;

import com.lordhero.game.model.items.IWeapon;

public class HitInfo {
	private int _x;
	private int _y;
	private IWeapon _weapon;
	
	public HitInfo(int x, int y, IWeapon weapon) {
		_x = x;
		_y = y;
		_weapon = weapon;
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
	
	public IWeapon getWeapon() {
		return _weapon;
	}
}
