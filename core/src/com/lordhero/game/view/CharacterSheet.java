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
import com.lordhero.game.model.items.MeleeWeapon;
import com.lordhero.game.model.items.Potion;
import com.lordhero.game.model.items.RangeWeapon;

public class CharacterSheet extends UiPanel {
	
	private IPlayer _player;
	
	private IGameMode _gameMode;
	
	private List<String> _itemList;
	
	private SelectBox<String> _meleeWeaponSelection;
	private java.util.List<MeleeWeapon> _meleeWeapons;
	
	private SelectBox<String> _rangedWeaponSelection;
	private java.util.List<RangeWeapon> _rangedWeapons;
	
	private Label _hitPointsText;
	private Label _xpText;
	private Label _levelText;

	private TextButton _quaffPotionButton;
	
	public CharacterSheet() {
		_table.setPosition(500,  500);
		
		_itemList = new List<String>(_skin);
		_meleeWeapons = new LinkedList<MeleeWeapon>();
		_rangedWeapons = new LinkedList<RangeWeapon>();		
		
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
		updateMeleeWeaponsList(items);
		updateRangedWeapongs(items);
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
	}

	private void updateMeleeWeaponsList(java.util.List<IItem> items) {
		Array<String> itemArray = new Array<String>();

		_meleeWeapons.clear();
		itemArray.add("None");
		_meleeWeapons.add(null);
		
		for (IItem item : items) {
			if (item instanceof MeleeWeapon) {
				itemArray.add(item.getName());
				_meleeWeapons.add((MeleeWeapon)item);
			}
		}
		
		_meleeWeaponSelection.setItems(itemArray);
	}

	private void updateRangedWeapongs(java.util.List<IItem> items) {
		Array<String> itemArray = new Array<String>();

		_rangedWeapons.clear();
		itemArray.add("None");
		_rangedWeapons.add(null);
		for (IItem item : items) {
			if (item instanceof RangeWeapon) {
				itemArray.add(item.getName());
				_rangedWeapons.add((RangeWeapon)item);
			}
		}
		
		_rangedWeaponSelection.setItems(itemArray);
	}
	
	private void addItemsTable() {
		Table itemsTable = new Table();
		_table.addActor(itemsTable);
		
		itemsTable.add(new ScrollPane(_itemList)).size(200, 500);
		
		_table.add(itemsTable);
		
		// applied items to the right
		Table appliedItemsTable = new Table();		
		_table.addActor(appliedItemsTable);

		// 1st row: quaff potion
		_quaffPotionButton = new TextButton("Quaff potion", _skin);
		_quaffPotionButton.addCaptureListener(new ClickListener() {
			@Override
			public void clicked(InputEvent input, float x, float y) {
				int index = _itemList.getSelectedIndex();
				
				if (index != -1) {
					IItem item = _player.getItems().get(index);
					if (item instanceof Potion) {
						_player.getItems().remove(index);
						((Potion)item).quaff(_player);
						update();
					}
				}
			}
		});
		appliedItemsTable.add(_quaffPotionButton);
		
		// 2nd row: melee weapon
		appliedItemsTable.row();
				
		appliedItemsTable.add(new Label("Melee weapon: ", _skin));

		_meleeWeaponSelection = new SelectBox<String>(_skin);
		_meleeWeaponSelection.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int index = _meleeWeaponSelection.getSelectedIndex();
				_player.setWeapon(_meleeWeapons.get(index));
				
			}				
		});
		appliedItemsTable.add(_meleeWeaponSelection);

		// 3rd row: ranged weapon
		appliedItemsTable.row();
		
		appliedItemsTable.add(new Label("Ranged weapon: ", _skin));
		
		_rangedWeaponSelection = new SelectBox<String>(_skin);
		_rangedWeaponSelection.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int index = _rangedWeaponSelection.getSelectedIndex();
				_player.setRangedWeapon(_rangedWeapons.get(index));
				
			}				
		});
		appliedItemsTable.add(_rangedWeaponSelection);

	    _table.add(appliedItemsTable);
	}
}
 