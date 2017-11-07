package com.lordhero.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WorldEditor extends UiPanel {
	private Cell<?> _selectedTileCell;
	
	private ISelectedCellProvider _selectedCellProvider;
	
	public WorldEditor() 
	{
		super();
		
	    _table.setPosition(50, 500);		   
        
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
