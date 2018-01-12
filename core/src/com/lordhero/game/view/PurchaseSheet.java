package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.IPlayer;

public class PurchaseSheet extends UiPanel {
	
	IGameMode _gameMode;
	
	IPlayer _player;
	
	public PurchaseSheet() {
		super();
		
		_table.setPosition(500, 500);
		_table.row();
		
		TextButton exitButton = new TextButton("Exit", _skin);
		_table.add(exitButton);
		
		exitButton.addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				_gameMode.set(GameMode.Play, null);
			}

		});
	}
	
	public void setGameMode(IGameMode gameMode) {
		_gameMode = gameMode;
	}
	
	public void setPlayer(IPlayer player) {
		_player = player;
	}
}
