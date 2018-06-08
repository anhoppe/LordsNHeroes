package com.lordhero.game.controller;

import com.badlogic.gdx.Input;
import com.lordhero.game.Consts;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;
import com.lordhero.game.model.IEntities;
import com.lordhero.game.model.INpc;
import com.lordhero.game.model.IPlayer;
import com.lordhero.game.model.items.IItem;

public class EntityController implements IController {
	
	IEntities _entities;
	
	IGameMode _gameMode;
	
	IPlayer _player;
	
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
  	
	@Override
	public void update() {
		_entities.update(_player, _gameMode);
	}

	@Override
	public boolean processKeyUp(int keyCode) {		
		if (_gameMode.is(GameMode.Play)) {
			if (keyCode == Consts.TalkToNpcKey) {
				INpc npc = _entities.getNpcInRange((int)_player.getX(), (int)_player.getY());				
				if (npc != null) {
					_gameMode.set(GameMode.Purchase, npc);
					return true;					
				}
			}
			if (keyCode == Consts.PickUpItemKey) {
				IItem item = _entities.getItemInRange((int)_player.getX(), (int)_player.getY());
				if (_player.addItem(item, false)) {
					_entities.remove(item);					
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
		if (_gameMode.get() == IGameMode.GameMode.AddNpc) {
			_entities.addNpc(xCursor, yCursor);
			return true;
		}
		else if (_gameMode.get() == IGameMode.GameMode.SelectMapItems) {
			_entities.selectEntity(xCursor, yCursor);
			return true;
		}
		else if (_gameMode.is(GameMode.MonsterPit)) {
			_entities.addMonsterPit(xCursor, yCursor);
		}
		else if (_gameMode.is(GameMode.AddItem)) {
			_entities.addItem(xCursor, yCursor);
		}

		return false;
	}

	@Override
	public boolean processMouseMove(int xPos, int yPos) {
		// TODO Auto-generated method stub
		return false;
	}
}
