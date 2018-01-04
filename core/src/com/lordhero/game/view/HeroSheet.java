package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lordhero.game.controller.IMapController;

public class HeroSheet extends UiPanel {
	
	IMapController _mapController;
	
	public HeroSheet() {
		super();

		_table.setPosition(100, 900);
		
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
}
