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
import com.lordhero.game.Consts;
import com.lordhero.game.IGameMode;
import com.lordhero.game.INetwork;
import com.lordhero.game.ISelectedItemProvider;
import com.lordhero.game.ISelectedNpcProvider;
import com.lordhero.game.model.items.GenericItem;
import com.lordhero.game.model.items.IItem;
import com.lordhero.game.model.items.IItemFactory;
import com.lordhero.game.model.items.IWeapon;
import com.lordhero.game.model.items.logic.IItemLogic;
import com.lordhero.game.view.INpcSelectionReceiver;

import net.dermetfan.utils.Pair;

public class Entities implements IEntities, IEntityFactory {
	private static IItemFactory ItemFactory = new com.lordhero.game.model.items.ItemFactory();

	private Hashtable<String, List<IEntity>> _entities;
	private Hashtable<String, List<IItem>> _items;
	
	private Hashtable<String, IItemLogic> _itemLogic;
	
	private IMapInfo _mapInfo;
	private List<Pair<String, IEntity>> _createdEntitiesBuffer;
	
	private INpcSelectionReceiver _npcSelectionReceiver;
	private ISelectedNpcProvider _selectedNpcProvider;
	private ISelectedItemProvider _selectedItemProvider;
	
	public Entities() {
		_entities = new Hashtable<String, List<IEntity>>();
		_createdEntitiesBuffer = new LinkedList<Pair<String, IEntity>>();
		
		_items = new Hashtable<String, List<IItem>>();
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
	
	public void setSelectedItemProvider(ISelectedItemProvider selectedItemProvider) {
		_selectedItemProvider = selectedItemProvider;
	}
	
	public void setItemLogic(Hashtable<String, IItemLogic> itemLogic) {
		_itemLogic = itemLogic;
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
	public List<IItem> getItemsOnSite() {
		return _items.get(_mapInfo.getCurrentMap());
	}

	@Override
	public void addNpc(int xPos, int yPos) {
		String site = _mapInfo.getCurrentMap();
		
		addEntityToSite(site, new Npc(_selectedNpcProvider.get(), xPos, yPos));
	}

	@Override
	public void addItem(int xCursorCell, int yCursorCell) {
		String site = _mapInfo.getCurrentMap();

		IItem item = ItemFactory.produce(_selectedItemProvider.getSelectedItem(), xCursorCell, yCursorCell);
		addItemToSite(site, item);
	}
	
	@Override
	public void remove(IItem item) {
		List<IItem> itemsOnSite = _items.get(_mapInfo.getCurrentMap());
		
		itemsOnSite.remove(item);
	}
	
	@Override
	public IItem getItemInRange(int xPosPx, int yPosPx) {
		IItem itemInRange = null;
		
		List<IItem> itemsOnSite = _items.get(_mapInfo.getCurrentMap());
		
		for (IItem item : itemsOnSite) {
			if (item.isAt(xPosPx, yPosPx)) {
				itemInRange = item;
				break;
			}
		}
		
		return itemInRange;
	}
	
	@Override
	public int getCollisions(int xPosPx, int yPosPx) {
		int collisions = 0;
		
		List<IItem> itemsOnSite = _items.get(_mapInfo.getCurrentMap());
		
		if (itemsOnSite == null) {
			return 0;
		}
		
		for (IItem item : itemsOnSite) {
			if (item instanceof GenericItem) {
				GenericItem genericItem = (GenericItem)item;
				IItemLogic logic = _itemLogic.get(genericItem.getName());
				if (logic != null)
				{
					if (logic.blocksMovement(genericItem)) {
						if (item.isAt(xPosPx, yPosPx + Consts.TileHeight)) {
				        	collisions |= Consts.Up;
						}
						if (item.isAt(xPosPx, yPosPx - Consts.TileHeight)) {
				        	collisions |= Consts.Down;
						}
						if (item.isAt(xPosPx - Consts.TileWidth, yPosPx)) {
				        	collisions |= Consts.Left;
						}
						if (item.isAt(xPosPx + Consts.TileWidth, yPosPx)) {
				        	collisions |= Consts.Right;
						}
					}					
				}
			}
		}		
		
		return collisions;
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

	@Override
	public void createEnemy(String site, float xPos, float yPos) {
		_createdEntitiesBuffer.add(new Pair<String, IEntity>(site, new Enemy(xPos, yPos)));
	}

	@Override
	public Missile createMissile(float xPos, float yPos, float viewDirectionDeg, Dice damage) {
		Missile missile = new Missile(xPos, yPos, viewDirectionDeg, damage);
		
		String site = _mapInfo.getCurrentMap();
		
		_createdEntitiesBuffer.add(new Pair<String, IEntity>(site, missile));
		
		return missile;
	}
	
	@Override
	public void save(XmlWriter writer) throws IOException {
		writer.element("Entities");
		
		writer.element("Characters");
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
        
        writer.element("Items");
		Set<String> itemKeys = _items.keySet();
        for(String key : itemKeys) {
        	writer.element("site").attribute("Name",  key);
        	
        	List<IItem> items = _items.get(key);
        	for (IItem item : items) {
        		item.write(writer);
        	}
        	writer.pop();
        }		
        writer.pop();
        
        writer.pop();
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
                
        XmlReader.Element characterNode = root.getChildByName("Characters");
        Array<XmlReader.Element> sites = characterNode.getChildrenByName("site");
        
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
        
        XmlReader.Element itemsNode = root.getChildByName("Items");
        sites = itemsNode.getChildrenByName("site");

        for (XmlReader.Element siteNode : sites) {
        	String siteName = siteNode.getAttribute("Name");
        	for (int index = 0; index < siteNode.getChildCount(); index++) {
        		XmlReader.Element itemNode = siteNode.getChild(index);
        	        		
        		IItem item = ItemFactory.produce(itemNode);        		
        		
        		if (item != null) {
            		addItemToSite(siteName, item);        			
        		}
        	}
        }        
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

	private void addItemToSite(String site, IItem item) {
		List<IItem> itemsOnSite;

		if (!_items.containsKey(site)) {
			itemsOnSite = new LinkedList<IItem>();
			_items.put(site,  itemsOnSite);
		}
		else {
			itemsOnSite = _items.get(site);
		}				
		
		itemsOnSite.add(item);
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
		for (Pair<String, IEntity> createdEntity : _createdEntitiesBuffer) {
			addEntityToSite(createdEntity.getKey(), createdEntity.getValue());
		}
		
		_createdEntitiesBuffer.clear();
	}
}
