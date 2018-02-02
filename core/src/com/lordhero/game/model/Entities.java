package com.lordhero.game.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.ISelectedNpcProvider;
import com.lordhero.game.model.items.IWeapon;
import com.lordhero.game.view.INpcSelectionReceiver;

public class Entities implements IEntities {
	private Hashtable<String, List<IEntity>> _entities;
	private IMapInfo _mapInfo;
	private ISelectedNpcProvider _selectedNpcProvider;
	
	private INpcSelectionReceiver _npcSelectionReceiver;
	
	public Entities() {
		_entities = new Hashtable<String, List<IEntity>>();
		
		load();
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

	@Override
	public void update(IPlayer player) {			
		create();
		
		updateEntities(player);
		
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
			_entities.put(site, entitiesOnSite);
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

	@Override
	public void hitEntity(int xPos, int yPos, IWeapon hitWeapon) {
		List<IEntity> entitiesOnSite = _entities.get(_mapInfo.getCurrentMap());

		for (IEntity entity : entitiesOnSite) {
			if (entity.isAt(xPos, yPos)) {
				if (entity instanceof Enemy) {
					Enemy enemy = (Enemy)entity;
					enemy.hit(hitWeapon.hit());
				}
			}
		}		
	}
	
	@Override
	public INpc getNpcInRange(int xPos, int yPos) {
		INpc npcInRange = null;
		
		List<IEntity> entitiesOnSite = _entities.get(_mapInfo.getCurrentMap());

		for (IEntity entity : entitiesOnSite) {
			if (entity.isInRange(xPos, yPos)) {
				if (entity instanceof Npc) {
					npcInRange = (INpc)entity;
					break;
				}
			}
		}
		
		return npcInRange;
	}
	
	@Override
	public void load() {
		String path = "c:/temp/teest";

		if (!Files.exists(Paths.get(path))) {
		  return;
		}
		try {
	         FileInputStream fis = new FileInputStream(path);
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         _entities = (Hashtable<String, List<IEntity>>) ois.readObject();
	         ois.close();
	         fis.close();
	    }
	    catch(IOException ioe) {
	    	ioe.printStackTrace();
	        return;
	    }
	    catch(ClassNotFoundException c) {
	    	System.out.println("Class not found");
	        c.printStackTrace();
	        return;
	    }
		
		// call restore method on all npc's
		Set<String> keys = _entities.keySet();
		for (String key : keys) {
			List<IEntity> entityList = _entities.get(key);
			
			for (IEntity entity : entityList) {
				entity.restore();
			}
		}
	}
	
	@Override
	public void save(XmlWriter writer) throws IOException {
		Set<String> keys = _entities.keySet();
        for(String key : keys) {
        	writer.element(key);
        	
        	List<IEntity> entities = _entities.get(key);
        	for (IEntity entity : entities) {
        		entity.write(writer);
        	}
        	writer.pop();
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
					if (entity instanceof INonPlayer) {
						if (((INonPlayer)entity).isTerminated()) {
							entitiesOnSite.remove(entity);
							entity.dispose();
							removedEnemy = true;
							break;
						}						
					}
			    }		    	
		    } while (removedEnemy);
		}			
	}
	
	private void updateEntities(IPlayer player) {
		Enumeration<String> enumKey = _entities.keys();
		while(enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    
		    List<IEntity> entitiesOnSite = _entities.get(key);

			for (IEntity entity : entitiesOnSite) {
				if (entity instanceof INonPlayer) {
					((INonPlayer)entity).update(player);
				}
		    }
		}			
	}
}
