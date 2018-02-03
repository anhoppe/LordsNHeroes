package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lordhero.game.IGameSourceProvider;
import com.lordhero.game.model.IPlayer;

public class HeroSheet extends UiPanel {
	
	IGameSourceProvider _gameSourceProvider;
	
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
				_gameSourceProvider.goHome();
			}
		});
		
	}
	
	public void setGameSourceProvider(IGameSourceProvider gameSourceProvider) {
		_gameSourceProvider = gameSourceProvider;
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
