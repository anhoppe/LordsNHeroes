package com.lordhero.game.model;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.lordhero.game.Consts;
import com.lordhero.game.IGameMode.SaveType;
import com.lordhero.game.IGameSourceProvider;

import net.dermetfan.gdx.maps.tiled.TmxMapWriter;
import net.dermetfan.gdx.maps.tiled.TmxMapWriter.Format;

public class MapInstance implements IMapInstance {
	private String _fileName;
    private TiledMap _tiledMap;
	private IGameSourceProvider _gameSourceProvider;
	
	private int _xEntry;
	private int _yEntry; 

    public static IMapInstance Create(String fileName, IGameSourceProvider gameSourceProvider) {
    	return new MapInstance(fileName, gameSourceProvider);
    }
    
    private MapInstance(String fileName, IGameSourceProvider gameSourceProvider) {
    	_fileName = fileName;
    	_gameSourceProvider = gameSourceProvider;
    }
    
    private MapInstance(String fileName, int xEntry, int yEntry, IGameSourceProvider gameSourceProvider) {
    	this(fileName, gameSourceProvider);
    	_xEntry = xEntry;
    	_yEntry = yEntry;
    }

	@Override
	public String getName() {
		return _fileName;
	}

	@Override
	public OrthogonalTiledMapRenderer load() {
        _tiledMap =  new TmxMapLoader().load(Consts.Map);
        return new OrthogonalTiledMapRenderer(_tiledMap);                		
	}

	@Override
	public IMapInstance getChildMap(float x, float y) {
		IMapInstance childMap = null;
		
		RectangleMapObject mapObject = getSiteLayerObjectAt(x, y);
		
		if (mapObject != null) {
			MapProperties mapProperties = mapObject.getProperties();
			String childMapName = (String)mapProperties.get(Consts.TargetMap);
			
			int xEntry = Integer.parseInt((String)mapProperties.get(Consts.ChildMapEntryX));
			int yEntry = Integer.parseInt((String)mapProperties.get(Consts.ChildMapEntryY));
			
			childMap = new MapInstance(childMapName, xEntry, yEntry, _gameSourceProvider);
		}
		
		return childMap;
	}

	@Override
	public float getXEntry() {
		return _xEntry;
	}

	@Override
	public float getYEntry() {
		return _yEntry;
	}

	@Override
	public void dispose() {
		write();
		if (_tiledMap != null) {
			_tiledMap.dispose();
		}
		
	}

	@Override
	public void write() {
		FileWriter fileWriter;
		try {
			Path path = Paths.get(_gameSourceProvider.getSaveFolder(SaveType.Map).toString(), _fileName + ".tmx");
			fileWriter = new FileWriter(path.toString(), false);
			TmxMapWriter tmxWriter = new TmxMapWriter(fileWriter);
			tmxWriter.tmx(_tiledMap, Format.Base64Zlib);
			tmxWriter.flush();
			tmxWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public int getPrice() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void setTileArray(int xPos, int yPos, String layerName, int[][] cells) {
        TiledMapTileLayer tileLayer = (TiledMapTileLayer)_tiledMap.getLayers().get(layerName);

        int width = cells.length;
        int height = cells[0].length;
                
        for (int x = 0; x < width; x++) {
        	for (int y = 0; y < height; y++) {
            	int xCell = x + (int)(xPos / tileLayer.getTileWidth());
            	int yCell = (int)(yPos / tileLayer.getTileHeight()) - y;

            	TiledMapTileLayer.Cell prevCell = tileLayer.getCell(xCell, yCell); 
            	
            	if (prevCell == null)
            	{
            		prevCell = new TiledMapTileLayer.Cell();
            		tileLayer.setCell(xCell,  yCell, prevCell);
            	}
            	prevCell.setTile(getSelectedTile(cells[x][y]));
        	}
        }         
	}

	@Override
	public boolean canBuildAt(int xPos, int yPos) {
		RectangleMapObject mapObject = getSiteLayerObjectAt(xPos, yPos);
		        
        return mapObject == null;
	}

	@Override
	public void addSubSite(int xPos, int yPos, IMapInstance sitePattern, int newMapsExitXCell, int newMapsExitYCell) {
		int xCell = xPos / Consts.TileWidth;
		int yCell = yPos / Consts.TileHeight;
		
        MapLayer siteLayer = (MapLayer)_tiledMap.getLayers().get(Consts.SiteLayer);        
        RectangleMapObject mapObject = new RectangleMapObject(xPos, yPos, Consts.TileWidth, Consts.TileHeight);	        
		mapObject.getProperties().put(Consts.TargetMap, sitePattern.getName());
		mapObject.getProperties().put("x", Integer.toString(xPos));
		mapObject.getProperties().put("y", Integer.toString(yPos));
		mapObject.getProperties().put(Consts.ChildMapEntryX, Integer.toString(newMapsExitXCell * Consts.TileWidth));
		mapObject.getProperties().put(Consts.ChildMapEntryY, Integer.toString(newMapsExitYCell * Consts.TileHeight));
        MapObjects objects = siteLayer.getObjects();
        objects.add(mapObject);
        
        TiledMapTileLayer obstacleLayer = (TiledMapTileLayer)_tiledMap.getLayers().get(Consts.Obstacles);
        TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
        cell.setTile(getTileFromTileSet("Building", 212));
        obstacleLayer.setCell(xCell, yCell, cell);        
	}	

	@Override
	public int getCollisions(float xPx, float yPx) {
		int collisions = 0;
		
		TiledMapTileLayer collisionObjectLayer = (TiledMapTileLayer)_tiledMap.getLayers().get(Consts.Collision);

        // get the player position
        int xPos = (int)(xPx / collisionObjectLayer.getTileWidth());
        int yPos = (int)(yPx / collisionObjectLayer.getTileHeight());
        
        if (collisionObjectLayer.getCell(xPos, yPos+1) != null) {
        	collisions |= Consts.Up;
        }
        
        if (collisionObjectLayer.getCell(xPos, yPos-1) != null) {
        	collisions |= Consts.Down;
        }        
        
        if (collisionObjectLayer.getCell(xPos-1, yPos) != null) {
        	collisions |= Consts.Left;
        }
        
        if (collisionObjectLayer.getCell(xPos+1, yPos) != null) {
        	collisions |= Consts.Right;
        }        
		
        return collisions;
	}

	private TiledMapTile getTileFromTileSet(String tileSetName, int index) {
		TiledMapTile selectedTile = null;
		
		TiledMapTileSets tileSets = _tiledMap.getTileSets();
        Iterator<TiledMapTileSet> it = tileSets.iterator();

        int count = 0;
        while(it.hasNext()) {
        	TiledMapTileSet tileSet = it.next();
        	
        	if (!tileSetName.equals(tileSet.getName())) {
        		continue;
        	}
        	
        	Iterator<TiledMapTile> tileIt = tileSet.iterator();
        	
        	while(tileIt.hasNext()) {
        		selectedTile = tileIt.next();
        		
        		if (count == index) {
        			break;
        		}
        		count++;
        	}

        	if (count == index) {
    			break;
    		}
        }
		
        return selectedTile;
	}

	private TiledMapTile getSelectedTile(int selectedCellIndex) {
		TiledMapTile selectedTile = null;
		
		TiledMapTileSets tileSets = _tiledMap.getTileSets();
        Iterator<TiledMapTileSet> it = tileSets.iterator();

        int count = 0;
        while(it.hasNext()) {
        	TiledMapTileSet tileSet = it.next();
        	Iterator<TiledMapTile> tileIt = tileSet.iterator();
        	
        	while(tileIt.hasNext()) {
        		selectedTile = tileIt.next();
        		
        		if (count == selectedCellIndex) {
        			break;
        		}
        		count++;
        	}

        	if (count == selectedCellIndex) {
    			break;
    		}
        }
		
        return selectedTile;
	}

	private RectangleMapObject getSiteLayerObjectAt(float x, float y) {
		RectangleMapObject mapObject = null;
		
        MapLayer layer = (MapLayer)_tiledMap.getLayers().get(Consts.SiteLayer);

        MapObjects objects = layer.getObjects();
		
		for (int i = 0; i < objects.getCount(); i++) {
			RectangleMapObject mapObjectIt = (RectangleMapObject)objects.get(i);
			
			if (Math.abs(mapObjectIt.getRectangle().x - x) < Consts.TileWidth &&
				Math.abs(mapObjectIt.getRectangle().y - y) < Consts.TileHeight) {
				
				mapObject = mapObjectIt;
				break;
			}
		}
		
		return mapObject;
	}
}
