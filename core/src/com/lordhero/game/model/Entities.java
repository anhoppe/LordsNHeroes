package com.lordhero.game.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import com.lordhero.game.model.Enemy;

public class Entities implements IEntities {
	private Hashtable<String, List<IEntity>> _entities;
	
	public Entities() {
		_entities = new Hashtable<String, List<IEntity>>();
	}
	
	public void update() {			
		create();
		
		updateEntities();
		
		delete();
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

	@Override
	public List<IEntity> getEntitiesOnSite(String site) {
		return _entities.get(site);
	}
}
