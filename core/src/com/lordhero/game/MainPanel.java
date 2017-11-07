package com.lordhero.game;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class MainPanel extends UiPanel {
	
	private Cell<?> _selectedTileCell;

	public MainPanel() {
		super();
		
	    _table.setPosition(0, 0);		   
        
	    _selectedTileCell = _table.add().size(400, 1024);
	    
	    _selectedTileCell.setActor(new Window("Menu of the lord", _skin));
	}
}
