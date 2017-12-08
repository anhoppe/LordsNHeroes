package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.lordhero.game.UiPanel;

public class MainPanel extends UiPanel {
	
	private Cell<?> _backroundCell;

	public MainPanel() {
		super();		
		
	    _table.setPosition(0, 0);		   
        
	    _backroundCell = _table.add().size(512, 1024);
	    
	    _backroundCell.setActor(new Window("Menu of the lord", _skin));
	}
}
