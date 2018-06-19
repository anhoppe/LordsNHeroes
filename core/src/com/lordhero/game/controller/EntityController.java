package com.lordhero.game.controller;

import java.util.Hashtable;

import com.lordhero.game.Consts;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.model.IEntities;
import com.lordhero.game.model.INpc;
import com.lordhero.game.model.IPlayer;
import com.lordhero.game.model.items.IGenericItem;
import com.lordhero.game.model.items.IItem;
import com.lordhero.game.model.items.logic.IItemLogic;

public class EntityController implements IController {
	
	IEntities _entities;
	
	IGameMode _gameMode;
	
	IPlayer _player;
	
	Hashtable<String, IItemLogic> _itemLogic;
	
	public EntityController() {
	}
		
	public void setEntities(IEntities entities) {
		_entities = entities;
	}
	
	public void setGameMode(IGameMode gameMode) {
		_gameMode = gameMode;
	}
	
	public void setPlayer(IPlayer player) {
		_player = player;
	}
	
	public void setItemLogic(Hashtable<String, IItemLogic> itemLogic) {
		_itemLogic = itemLogic;
	}
  	
	@Override
	public void update() {
		_entities.update(_player, _gameMode);
	}

	@Override
	public boolean processKeyUp(int keyCode) {		
		if (_gameMode.is(GameMode.Play)) {
			if (keyCode == Consts.KeyCodeTalkToNpc) {
				INpc npc = _entities.getNpcInRange((int)_player.getX(), (int)_player.getY());				
				if (npc != null) {
					_gameMode.set(GameMode.Purchase, npc);
					return true;					
				}
			}
			if (keyCode == Consts.KeyCodePickUpItem) {
				IItem item = _entities.getItemInRange((int)_player.getX(), (int)_player.getY());
				if (_player.addItem(item, false)) {
					_entities.remove(item);					
				}
			}
			if (keyCode == Consts.KeyCodeOpen) {
				IItem item = _entities.getItemInRange((int)_player.getX(), (int)_player.getY());
				if (item instanceof IGenericItem) {
 					IGenericItem genericItem = (IGenericItem)item;
					if (genericItem.is(Consts.ItemDoor)) {
						IItemLogic itemLogic = _itemLogic.get(Consts.ItemDoor);
						itemLogic.use(genericItem, (int)_player.getX(), (int)_player.getY());
					}
				}
			}
		}
		
		return false;
	}

	@Override
	public boolean processMouseDown(int xScreen, int yScreen, int xCursor, int yCursor) {
		// TODO Auto-generated method stub
		return false;
	}	

	@Override
	public boolean processMouseUp(int xScreen, int yScreen, int xCursor, int yCursor) {
		if (_gameMode.is(IGameMode.GameMode.AddNpc)) {
			_entities.addNpc(xCursor, yCursor);
			return true;
		}
		else if (_gameMode.is(IGameMode.GameMode.SelectMapItems)) {
			_entities.selectEntity(xCursor, yCursor);
			return true;
		}
		else if (_gameMode.is(GameMode.MonsterPit)) {
			_entities.addMonsterPit(xCursor, yCursor);
		}
		else if (_gameMode.is(GameMode.AddItem)) {
			_entities.addItem(xCursor - Consts.TileWidth / 2, yCursor - Consts.TileHeight / 2);
		}

		return false;
	}

	@Override
	public boolean processMouseMove(int xPos, int yPos) {
		// TODO Auto-generated method stub
		return false;
	}
}
