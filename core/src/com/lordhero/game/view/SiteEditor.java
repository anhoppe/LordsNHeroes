package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.lordhero.game.ISelectedSiteProvider;

public class SiteEditor extends UiPanel implements ISelectedSiteProvider {

	SelectBox<String> _siteSelection;
	
	public SiteEditor() {
		super();
		
		_table.setPosition(150, 500);		   

		_table.row();

		_siteSelection = new SelectBox<String>(_skin);
		
		String[] siteSelection = {"Dungeon", "Town"};
		
		_siteSelection.setItems(siteSelection);
		_table.add(_siteSelection).size(200, 32);
	}
	
	@Override
	public String getSelectedSiteFileName() {
		return "dungeon_tmeplate";
	}
}
