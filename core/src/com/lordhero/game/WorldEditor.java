package com.lordhero.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Selection;

public class WorldEditor extends UiPanel {
	private Selection _selection;

	private Texture _tiles;
	
	private Cell<?> _selectedTileCell;
	
	private TextureRegion _selectedCellTextureRegion;
	
	private ISelectedCellProvider _selectedCellProvider;
	
	public WorldEditor(Selection selection) 
	{
		super();
		
		this._selection = selection;
	
	    _table.setPosition(20, 500);		   
        
	    _selectedTileCell = _table.add().size(32,32);

	    _table.row();
	    final TextButton prev = new TextButton("<<", _skin);
	    prev.addCaptureListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {
				_selectedCellProvider.decSelectedCellIndex();
				updateSelectedTile();
				
				event.cancel();
			}	    	
	    });
	    
	    _table.add(prev).size(24, 16);
	    
	    final TextButton next = new TextButton(">>", _skin);
	    next.addCaptureListener(new ClickListener() {

			public void clicked(InputEvent event, float x, float y) {

				_selectedCellProvider.incSelectedCellIndex();
				updateSelectedTile();				

				event.cancel();
			}	    	
	    });

	    _table.add(next).size(24, 16);
	}
	
	public void setSelectedCellProvider(ISelectedCellProvider selectedCellProvider) {
		_selectedCellProvider = selectedCellProvider;
		updateSelectedTile();
	}
	
	private void updateSelectedTile()
	{
		_selectedTileCell.setActor(_selectedCellProvider.getSelectedCellImage());
	}
}
