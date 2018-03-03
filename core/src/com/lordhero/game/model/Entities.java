package com.lordhero.game.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.IGameMode;
import com.lordhero.game.INetwork;
import com.lordhero.game.ISelectedNpcProvider;
import com.lordhero.game.model.items.IWeapon;
import com.lordhero.game.view.INpcSelectionReceiver;

import net.dermetfan.utils.Pair;

public class Entities implements IEntities, IEntityFactory {
	private Hashtable<String, List<IEntity>> _entities;
	private IMapInfo _mapInfo;
	private ISelectedNpcProvider _selectedNpcProvider;
	private List<Pair<String, IEntity>> _createdEntities;
	
	private INpcSelectionReceiver _npcSelectionReceiver;
	
	public Entities() {
		_entities = new Hashtable<String, List<IEntity>>();
		_createdEntities = new LinkedList<Pair<String, IEntity>>();
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
	public void update(IPlayer player, IGameMode gameMode) {			
		updateEntities(player, gameMode);
		
		addCreatedEntities();
		
		deleteTerminatedEntites();
	}

	@Override
	public List<IEntity> getEntitiesOnSite() {
		return _entities.get(_mapInfo.getCurrentMap());
	}

	@Override
	public void addNpc(int xPos, int yPos) {
		String site = _mapInfo.getCurrentMap();
		
		addEntityToSite(site, new Npc(_selectedNpcProvider.get(), xPos, yPos));
	}

	@Override
	public void addMonsterPit(int xPos, int yPos) {
		String site = _mapInfo.getCurrentMap();
		
		addEntityToSite(site, new MonsterPit(xPos, yPos, this, _mapInfo.getCurrentMap()));		
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
	public void hitEntity(int xPos, int yPos, IPlayer player, IWeapon weapon) {
		List<IEntity> entitiesOnSite = _entities.get(_mapInfo.getCurrentMap());

		for (IEntity entity : entitiesOnSite) {
			if (entity.isAt(xPos, yPos)) {
				if (entity instanceof Enemy) {
					Enemy enemy = (Enemy)entity;
					if (enemy.hit(weapon.getDamage())) {
						weapon.setHasHit();
						player.addXp(enemy.getXp());
					}					
				}
			}
		}		
	}
	
	@Override
	public INpc getNpcInRange(int xPos, int yPos) {
		INpc npcInRange = null;
		
		List<IEntity> entitiesOnSite = _entities.get(_mapInfo.getCurrentMap());

		for (IEntity entity : entitiesOnSite) {
			if (entity instanceof Npc) {
				Npc npc = (Npc)entity;
				if (npc.isInRange(xPos, yPos)) {
					npcInRange = npc;
					break;
				}
			}
		}
		
		return npcInRange;
	}	

	public void loadFromRemote(INetwork network) throws IOException {
        _entities.clear();

        byte[] fileAsArray = network.requestEntities();
		
		if (fileAsArray == null) {
			return;
		}
		
        try {
	        FileOutputStream fos = new FileOutputStream("entities");
	        fos.write(fileAsArray);
	        fos.flush();
	        fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		FileInputStream inputStream = new FileInputStream("entities");        
        XmlReader xmlReader = new XmlReader();
        XmlReader.Element root = xmlReader.parse(inputStream);
        
        
        Array<XmlReader.Element> sites = root.getChildrenByName("site");
        
        for (XmlReader.Element siteNode : sites) {
        	String siteName = siteNode.getAttribute("Name");
        	for (int index = 0; index < siteNode.getChildCount(); index++) {
        		XmlReader.Element entityNode = siteNode.getChild(index);
        		
        		IEntity entity = null;
        		if (entityNode.getName().equals("Npc")) {
        			entity = new Npc(entityNode);
        		}
        		else if (entityNode.getName().equals("Enemy")) {
        			entity = new Enemy(entityNode);
        		}
        		else if (entityNode.getName().equals("MonsterPit")) {
        			entity = new MonsterPit(entityNode, this);
        		}
        		
        		addEntityToSite(siteName, entity);
        	}
        }
	}
	
	@Override
	public void save(XmlWriter writer) throws IOException {
		writer.element("Entities");
		
		Set<String> keys = _entities.keySet();
        for(String key : keys) {
        	writer.element("site").attribute("Name",  key);
        	
        	List<IEntity> entities = _entities.get(key);
        	for (IEntity entity : entities) {
        		entity.write(writer);
        	}
        	writer.pop();
        }		
        writer.pop();
	}

	@Override
	public void createEnemy(String site, float xPos, float yPos) {
		_createdEntities.add(new Pair<String, IEntity>(site, new Enemy(xPos, yPos)));
	}

	@Override
	public Missile createMissile(float xPos, float yPos, float viewDirectionDeg, Dice damage) {
		Missile missile = new Missile(xPos, yPos, viewDirectionDeg, damage);
		
		String site = _mapInfo.getCurrentMap();
		
		_createdEntities.add(new Pair<String, IEntity>(site, missile));
		
		return missile;
	}
	
	private void addEntityToSite(String site, IEntity entity) {
		List<IEntity> entitiesOnSite;

		if (!_entities.containsKey(site)) {
			entitiesOnSite = new LinkedList<IEntity>();
			_entities.put(site,  entitiesOnSite);
		}
		else {
			entitiesOnSite = _entities.get(site);
		}				
		
		entitiesOnSite.add(entity);
	}
	
	private void deleteTerminatedEntites() {
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
	
	private void updateEntities(IPlayer player, IGameMode gameMode) {
		Enumeration<String> enumKey = _entities.keys();
		while(enumKey.hasMoreElements()) {
		    String key = enumKey.nextElement();
		    
		    List<IEntity> entitiesOnSite = _entities.get(key);

			for (IEntity entity : entitiesOnSite) {
				if (entity instanceof IUpdateable) {
					((IUpdateable)entity).update(player, gameMode);
				}
		    }
		}			
	}
	
	private void addCreatedEntities() {
		for (Pair<String, IEntity> createdEntity : _createdEntities) {
			addEntityToSite(createdEntity.getKey(), createdEntity.getValue());
		}
		
		_createdEntities.clear();
	}
}
