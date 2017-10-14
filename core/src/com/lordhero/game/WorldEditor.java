package com.lordhero.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;

public class WorldEditor extends UiPanel implements ICellSelector {
	private Selection _selection;

	private Image _selectedMap;
	private Texture _tiles;
	
	private int _tilesWidth;
	private int _tilesHeight;
	
	private int _selectedTileIndex = 1;
	
	private Cell<?> _selectedTileCell;
	
	private TextureRegion _selectedCellTextureRegion;
	
	public WorldEditor(Selection selection) 
	{
		super();
		
		this._selection = selection;
	
	    _table.setPosition(20, 500);		   
	
	    _tiles = new Texture("MyTiles.png");
	    
	    _tilesWidth = _tiles.getWidth();
	    _tilesHeight = _tiles.getHeight();	        
        
	    _selectedTileCell = _table.add().size(32,32);

        updateSelectedTile();
	    _table.row();
	    final TextButton prev = new TextButton("<<", _skin);
	    prev.addCaptureListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				_selectedTileIndex = Math.max(0, _selectedTileIndex-1);
				updateSelectedTile();
				
				event.cancel();
			}	    	
	    });
	    
	    _table.add(prev).size(24, 16);
	    
	    final TextButton next = new TextButton(">>", _skin);
	    next.addCaptureListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				_selectedTileIndex++;
				updateSelectedTile();				

				event.cancel();
			}	    	
	    });

	    _table.add(next).size(24, 16);
	}

	@Override
	public TextureRegion getSelectedCellTextureRegion() {		
		return _selectedCellTextureRegion;
	}
	
	private void updateSelectedTile()
	{
        int xPos = _selectedTileIndex % (_tilesWidth / 32);
        int yPos = _selectedTileIndex / (_tilesWidth / 32);
        
        _selectedCellTextureRegion = new TextureRegion(_tiles, xPos*32, yPos*32, 32, 32); 
	    _selectedMap = new Image(_selectedCellTextureRegion);
	    
	    _selectedTileCell.setActor(_selectedMap);
	}
}
