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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.lordhero.game.IGameMode;
import com.lordhero.game.model.IPlayer;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.model.items.IItem;
import com.lordhero.game.model.items.RangeWeapon;
import com.lordhero.game.model.items.Weapon;

public class CharacterSheet extends UiPanel {
	
	private IPlayer _player;
	
	private IGameMode _gameMode;
	
	private List<String> _itemList;
	
	private SelectBox<String> _meleeWeaponSelection;
	private java.util.List<Weapon> _meleeWeapons;
	
	private SelectBox<String> _rangedWeaponSelection;
	private java.util.List<RangeWeapon> _rangedWeapons;
	
	private TextField _hitPointsText;
	
	public CharacterSheet() {
		_table.setPosition(500,  500);
		
		_itemList = new List<String>(_skin);
		_meleeWeapons = new LinkedList<Weapon>();
		_rangedWeapons = new LinkedList<RangeWeapon>();		
		
		Table statsTable = new Table();
		statsTable.add(new Label("Hit points", _skin));
		_hitPointsText = new TextField("", _skin);
		statsTable.add(_hitPointsText);
		_table.add(statsTable);
		
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
	
	public void setPlayer(IPlayer player) {
		_player = player;
	}
	
	public void setGameMode(IGameMode gameMode) {
		_gameMode = gameMode;
	}
	
	public void update() {		
		java.util.List<IItem> items = _player.getItems();

		_hitPointsText.setText(Integer.toString(_player.getHitPoints()));
		updateItemsList(items);
		updateMeleeWeaponsList(items);
		updateRangedWeapongs(items);
	}

	private void updateItemsList(java.util.List<IItem> items) {		
		Array<String> itemArray = new Array<String>();
		
		for (IItem item : items) {
			itemArray.add(item.getName());
		}
		
		_itemList.setItems(itemArray);
	}

	private void updateMeleeWeaponsList(java.util.List<IItem> items) {
		Array<String> itemArray = new Array<String>();

		itemArray.add("None");
		_meleeWeapons.add(null);
		
		for (IItem item : items) {
			if (item instanceof Weapon) {
				itemArray.add(item.getName());
				_meleeWeapons.add((Weapon)item);
			}
		}
		
		_meleeWeaponSelection.setItems(itemArray);
	}

	private void updateRangedWeapongs(java.util.List<IItem> items) {
		Array<String> itemArray = new Array<String>();

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
 