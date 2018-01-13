package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IPlayer;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.model.items.IItem;

public class CharacterSheet extends UiPanel {
	
	private IPlayer _player;
	
	private IGameMode _gameMode;
	
	private List<String> _itemList;
	
	public CharacterSheet() {
		_table.setPosition(500,  500);
		
		_itemList = new List<String>(_skin);
		
		_table.add(new ScrollPane(_itemList)).size(200, 500);
		
		_table.row();
		
		TextButton exitButton = new TextButton("Exit", _skin);
		
		exitButton.addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				_gameMode.set(GameMode.Play, null);
			}
		});
		
		_table.add(exitButton);
	}
	
	public void setPlayer(IPlayer player) {
		_player = player;
	}
	
	public void setGameMode(IGameMode gameMode) {
		_gameMode = gameMode;
	}
	
	public void update() {
		java.util.List<IItem> items = _player.getItems();
		
		Array<String> itemArray = new Array<String>();
		
		for (IItem item : items) {
			itemArray.add(item.getName());
		}
		
		_itemList.setItems(itemArray);
	}
}
 