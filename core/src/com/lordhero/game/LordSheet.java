package com.lordhero.game;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class LordSheet extends UiPanel {
	
	ILord _lord;
	
	TextField _moneyText;
	
	public LordSheet() {
		super();
		
		_table.setPosition(120, 800);
		
		_table.row();
		_table.add(new Label("Money", _skin));
		
		_moneyText = new TextField("", _skin);
		_table.add(_moneyText);

	}
	
	public void setLord(ILord lord) {
		_lord = lord;
		
		_lord.registerChangeListener(new LordChangedListener() {
			@Override
			public void onChanged() {
				updateSheet();
			}
		});
		
		updateSheet();
	}
	
	private void updateSheet() {
		_moneyText.setText(Integer.toString(_lord.getMoney()));
	}
}
