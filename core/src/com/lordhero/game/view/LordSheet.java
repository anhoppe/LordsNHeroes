package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lordhero.game.Consts;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.IGameSourceProvider;
import com.lordhero.game.model.IPlayer;

public class LordSheet extends UiPanel {
	
	IPlayer _lord;
	
	TextField _moneyText;
    SelectBox<String> _menuSelection;
		
    IGameMode _gameMode;
    
    IGameSourceProvider _gameSourceProvider;
    
	public LordSheet() {
		super();
		
		_table.setPosition(100, 900);
		
		_table.row();
		
		TextButton visitWorld = new TextButton("Visit world", _skin);		

		_table.add(visitWorld).size(90, 25);
		visitWorld.addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				_gameSourceProvider.visitWorld();
			}
		});
		
		_table.row();
		_table.add(new Label("Money", _skin));
		
		_moneyText = new TextField("", _skin);
		_table.add(_moneyText);
		
		_table.row();
		_table.add(new Label("Menu: ", _skin));
		
	    _menuSelection = new SelectBox<String>(_skin);
	    _menuSelection.setItems(new String[] {
	    		Consts.EditMapMode, 
	    		Consts.AddNpcMode, 
	    		Consts.AddSiteMode, 
	    		Consts.AddItemMode,
	    		Consts.MonsterPit, 
	    		Consts.SelectMode});
	    _menuSelection.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				_gameMode.set(getModeFromString(_menuSelection.getSelected()), null);
			}});
	    
	    _table.add(_menuSelection).size(120, 25);
	}
	
	private GameMode getModeFromString(String selected) {
		if (selected == Consts.EditMapMode) {
			return GameMode.BuyTiles;			
		}
		else if (selected == Consts.AddNpcMode) {
			return GameMode.AddNpc;
		}
		else if (selected == Consts.AddSiteMode) {
			return GameMode.AddSite;
		}
		else if (selected == Consts.AddItemMode) {
			return GameMode.AddItem;
		}
		else if (selected == Consts.MonsterPit) {
			return GameMode.MonsterPit;
		}
		else if (selected == Consts.SelectMode) {
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
	
	public void setGameSourceProvider(IGameSourceProvider gameSourceProvider) {
		_gameSourceProvider = gameSourceProvider;
	}
	
	private void updateSheet() {
		_moneyText.setText(Integer.toString(_lord.getMoney()));
	}
}
