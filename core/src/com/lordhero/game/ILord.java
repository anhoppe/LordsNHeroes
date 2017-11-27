package com.lordhero.game;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public interface ILord {
	int getMoney();
	
	boolean pay(int price);
	
	void registerChangeListener(ChangeListener listener);
}
