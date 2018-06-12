package com.lordhero.game.view;

import java.util.LinkedList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.model.IPlayer;
import com.lordhero.game.model.items.IItem;

public class CharacterSheet extends UiPanel {
	
	private IPlayer _player;
	
	private IGameMode _gameMode;
	
	private List<String> _itemList;
	
    private java.util.List<SelectBox<String>> _slotSelections;
	
	private Label _hitPointsText;
	private Label _xpText;
	private Label _levelText;

	public CharacterSheet() {
		_table.setPosition(500,  500);
		
		_itemList = new List<String>(_skin);		
		
		addStatsTable();
		
		_table.row();
		
		// Items list view to the left
		addItemsTable();
					    
	    // at the bottom the exit button
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

	private void addStatsTable() {
		Table statsTable = new Table();
		_levelText = new Label("", _skin);
		statsTable.add(_levelText);
		_xpText = new Label("", _skin);
		statsTable.add(_xpText);
		_hitPointsText = new Label("", _skin);
		statsTable.add(_hitPointsText);
		_table.add(statsTable);
	}
	
	public void setPlayer(IPlayer player) {
		_player = player;
	}
	
	public void setGameMode(IGameMode gameMode) {
		_gameMode = gameMode;
	}
	
	public void update() {		
		java.util.List<IItem> items = _player.getItems();

		updateStats();
		updateItemsList(items);
	}
	
	private void updateStats() {
		_levelText.setText("Level: " + Integer.toString(_player.getLevel()));
		_xpText.setText("XP: " + Integer.toString(_player.getXp()));
		_hitPointsText.setText("Hit points: " + Integer.toString(_player.getHitPoints()));		
	}

	private void updateItemsList(java.util.List<IItem> items) {
		Array<String> itemArray = new Array<String>();
		
		for (IItem item : items) {
			if (item != null) {
				itemArray.add(item.getName());
			}
		}
				
		_itemList.setItems(itemArray);
		
		for (SelectBox<String> slotSelection : _slotSelections) {
			slotSelection.setItems(itemArray);
		}
	}
	
	private void addItemsTable() {
		Table itemsTable = new Table();
		_table.addActor(itemsTable);
		
		itemsTable.add(new ScrollPane(_itemList)).size(200, 500);
		
		_table.add(itemsTable);
		
		// applied items to the right
		Table appliedItemsTable = new Table();		
		_table.addActor(appliedItemsTable);

		// Item slot rows
		_slotSelections = new LinkedList<SelectBox<String>>();
		
		addItemSlot(appliedItemsTable, 0);
		addItemSlot(appliedItemsTable, 1);
		addItemSlot(appliedItemsTable, 2);
		
	    _table.add(appliedItemsTable);
	}
	
	private void addItemSlot(Table appliedItemsTable, int playerSlot) {
		appliedItemsTable.row();
		final SelectBox<String> slotSelection = new SelectBox<String>(_skin);
		final int playerSlotInternal = playerSlot;
		
		_slotSelections.add(slotSelection);
		appliedItemsTable.add(slotSelection);
		
		slotSelection.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int index = slotSelection.getSelectedIndex();
				_player.assignItemToSlot(playerSlotInternal, index);
			}
		});
	}
}
 