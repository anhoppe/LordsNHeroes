package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.IPlayer;
import com.lordhero.game.model.INpc;
import com.lordhero.game.model.items.IItem;

public class PurchaseSheet extends UiPanel {
	
	IGameMode _gameMode;
	
	IPlayer _player;
	
	Label _selectedItem;
	List<String> _itemsList;
	Array<String> _itemsArray;
	
	INpc _npc;
	
	public PurchaseSheet() {
		super();
		
		_table.setPosition(500, 500);
		
		_selectedItem = new Label("", _skin);
		
		_table.add(_selectedItem);
		
		_table.row();
		
		_itemsList = new List<String>(_skin);
		_itemsArray = new Array<String>();
		
		_table.add(new ScrollPane(_itemsList)).size(200, 400);
		
		TextButton buyButton = new TextButton("buy", _skin);
		
		buyButton.addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				buyItem();
			}
		});
		
		_table.add(buyButton);
		
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
	
	public void setNpc(INpc npc) {
		_npc = npc;
		updateItems();
	}

	private void buyItem() {
		int index = _itemsList.getSelectedIndex();
		
		java.util.List<IItem> npcItems = _npc.getItems();
		IItem transferredItem = npcItems.remove(index);
		
		_player.addItem(transferredItem);
		
		updateItems();
	}	
	
	private void updateItems() {
		_itemsArray.clear();
		java.util.List<IItem> items = _npc.getItems();
		
		for (IItem item : items) {
			_itemsArray.add(item.getName());
		}
		
		_itemsList.setItems(_itemsArray);
	}
}
