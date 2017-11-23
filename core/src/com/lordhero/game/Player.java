package com.lordhero.game;

import java.util.LinkedList;

public class Player implements ILord {
	private int _money = 10000;
	
	private LinkedList<LordChangedListener> _lordChangedListeners;
	
	public Player() {
		_lordChangedListeners = new LinkedList<LordChangedListener>();
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
	public void registerChangeListener(LordChangedListener listener) {
		_lordChangedListeners.add(listener);
	}
	
	private void fireChangeEvent() {
		for (LordChangedListener listener : _lordChangedListeners) {
			listener.onChanged();
		}
	}
}
