package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.IPlayer;
import com.lordhero.game.controller.IMapController;

public class LordSheet extends UiPanel {
	private static final String EditMapMode = "Editor map";
	private static final String AddNpcMode = "Add npc";
	private static final String SelectMode = "Select";
	
	IPlayer _lord;
	
	TextField _moneyText;
    SelectBox<String> _menuSelection;
		
    IGameMode _gameMode;
    
    IMapController _mapController;
    
	public LordSheet() {
		super();
		
		_table.setPosition(100, 900);
		
		_table.row();
		
		TextButton visitWorld = new TextButton("Visit world", _skin);

		_table.add(visitWorld).size(90, 25);
		visitWorld.addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				_mapController.visitWorld();
			}
		});
		
		_table.row();
		_table.add(new Label("Money", _skin));
		
		_moneyText = new TextField("", _skin);
		_table.add(_moneyText);
		
		_table.row();
		_table.add(new Label("Menu: ", _skin));
		
	    _menuSelection = new SelectBox<String>(_skin);
	    _menuSelection.setItems(new String[] {EditMapMode, AddNpcMode, SelectMode});
	    _menuSelection.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				_gameMode.set(getModeFromString(_menuSelection.getSelected()));
			}});
	    
	    _table.add(_menuSelection).size(120, 25);
	}
	
	private GameMode getModeFromString(String selected) {
		if (selected == EditMapMode) {
			return GameMode.BuyTiles;			
		}
		else if (selected == AddNpcMode) {
			return GameMode.AddNpc;
		}
		else if (selected == SelectMode) {
			return GameMode.SelectMapItems;
		}
		
		return GameMode.None;
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
	
	public void setGameMode(IGameMode gameMode) {
		_gameMode = gameMode;
	}
	
	public void setMapController(IMapController mapController) {
		_mapController = mapController;
	}
	
	private void updateSheet() {
		_moneyText.setText(Integer.toString(_lord.getMoney()));
	}
}
