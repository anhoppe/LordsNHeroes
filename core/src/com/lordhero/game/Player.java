package com.lordhero.game;

import java.util.LinkedList;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Player implements ILord {
	private int _money = 10000;
	
	private LinkedList<ChangeListener> _lordChangedListeners;
	
	public Player() {
		_lordChangedListeners = new LinkedList<ChangeListener>();
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
	public void registerChangeListener(ChangeListener listener) {
		_lordChangedListeners.add(listener);
	}
	
	private void fireChangeEvent() {
		for (ChangeListener listener : _lordChangedListeners) {
			listener.changed(null, null);
		}
	}
}
