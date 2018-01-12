package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.lordhero.game.IPlayer;
import com.lordhero.game.controller.IMapController;
import com.lordhero.game.model.INpc;
import com.lordhero.game.model.items.IItem;

public class HeroSheet extends UiPanel {
	
	IMapController _mapController;
	
	private IPlayer _hero;

	public HeroSheet() {
		super();

		_table.setPosition(200, 500);
		
		_table.row();
		
		TextButton goHomeButton = new TextButton("Go home", _skin);
	
		_table.add(goHomeButton).size(90, 25);
		goHomeButton.addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				_mapController.goHome();
			}
		});
		
	}
	
	public void setMapController(IMapController mapController) {
		_mapController = mapController;
	}

	public void setHero(IPlayer hero) {
		_hero = hero;
		
		_hero.registerChangeListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				updateSheet();				
			}
		});		
	}
	
	private void updateSheet() {
	}
}
