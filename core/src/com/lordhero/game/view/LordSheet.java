package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.lordhero.game.IMenuSelector;
import com.lordhero.game.IPlayer;

public class LordSheet extends UiPanel {
	
	IPlayer _lord;
	
	TextField _moneyText;
    SelectBox<String> _menuSelection;
		
    IMenuSelector _menuSelector;
    
	public LordSheet() {
		super();
		
		_table.setPosition(100, 900);
		
		_table.row();
		_table.add(new Label("Money", _skin));
		
		_moneyText = new TextField("", _skin);
		_table.add(_moneyText);
		
		_table.row();
		_table.add(new Label("Menu: ", _skin));
		
	    _menuSelection = new SelectBox<String>(_skin);
	    _menuSelection.setItems(new String[] {"Editor map", "Add npc", "Select"});
	    _menuSelection.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				_menuSelector.setSelection(_menuSelection.getSelected());
			}});
	    
	    _table.add(_menuSelection).size(120, 25);
	}
	
	public void setLord(IPlayer lord) {
		_lord = lord;
		
		_lord.registerChangeListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				updateSheet();				
			}
		});
		
		updateSheet();
	}
	
	public void setMenuSelector(IMenuSelector menuSelector) {
		_menuSelector = menuSelector;
	}
	
	private void updateSheet() {
		_moneyText.setText(Integer.toString(_lord.getMoney()));
	}
}
