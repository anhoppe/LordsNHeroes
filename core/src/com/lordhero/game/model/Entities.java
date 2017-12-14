package com.lordhero.game.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.lordhero.game.ISelectedNpcProvider;
import com.lordhero.game.model.Enemy;
import com.lordhero.game.view.INpcSelectionReceiver;

public class Entities implements IEntities {
	private Hashtable<String, List<IEntity>> _entities;
	private IMapInfo _mapInfo;
	private ISelectedNpcProvider _selectedNpcProvider;
	
	private INpcSelectionReceiver _npcSelectionReceiver;
	
	public Entities() {
		_entities = new Hashtable<String, List<IEntity>>();
	}
	
	public void setMapInfo(IMapInfo mapInfo) {
		_mapInfo = mapInfo;
	}
	
	public void setSelectedNpcProvider(ISelectedNpcProvider selectedNpcProvider) {
		_selectedNpcProvider = selectedNpcProvider;
	}
	
	public void setNpcSelectionReceiver(INpcSelectionReceiver receiver) {
		_npcSelectionReceiver = receiver;
	}
	
	public void update() {			
		create();
		
		updateEntities();
		
		delete();
	}

	@Override
	public List<IEntity> getEntitiesOnSite() {
		return _entities.get(_mapInfo.getCurrentMap());
	}

	@Override
	public void addNpc(int xPos, int yPos) {
		String site = _mapInfo.getCurrentMap();
		
		List<IEntity> entitiesOnSite;
		if (!_entities.containsKey(site)) {
			entitiesOnSite = new LinkedList<IEntity>();
			_entities.put(site,  entitiesOnSite);
		}
		else {
			entitiesOnSite = _entities.get(site);
		}				
		
		entitiesOnSite.add(new Npc(_selectedNpcProvider.get(), xPos, yPos));		
	}

	@Override
	public void selectEntity(int xPos, int yPos) {
		List<IEntity> entitiesOnSite = _entities.get(_mapInfo.getCurrentMap());

		for (IEntity entity : entitiesOnSite) {
			if (entity.isAt(xPos, yPos)) {
				if (entity instanceof Npc) {
					_npcSelectionReceiver.select((Npc)entity);
					break;
				}
			}
		}
	}

	private void create() {
		String site = null;
		List<IEntity> entitiesOnSite;
		
		if (Math.random() < 0.01) {
			site = "baseMap";		
			if (!_entities.containsKey(site)) {
				entitiesOnSite = new LinkedList<IEntity>();
				_entities.put(site,  entitiesOnSite);
			}
			else {
				entitiesOnSite = _entities.get(site);
			}				
			
			entitiesOnSite.add(new Enemy());
		}		
	}
	
	private void delete() {
		Enumeration<String> enumKey = _entities.keys();
		while(enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    
		    List<IEntity> entitiesOnSite = _entities.get(key);
			boolean removedEnemy;

		    do {
		    	removedEnemy = false;
				for (IEntity entity : entitiesOnSite) {
					if (entity.isTerminated()) {
						entitiesOnSite.remove(entity);
						removedEnemy = true;
						break;
					}
			    }
		    	
		    } while (removedEnemy);
		}			
	}
	
	private void updateEntities() {
		Enumeration<String> enumKey = _entities.keys();
		while(enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    
		    List<IEntity> entitiesOnSite = _entities.get(key);

			for (IEntity entity : entitiesOnSite) {
				entity.update();
		    }
		}			
	}
}
