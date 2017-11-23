package com.lordhero.game;

public interface ILord {
	int getMoney();
	
	boolean pay(int price);
	
	void registerChangeListener(LordChangedListener listener);
}
