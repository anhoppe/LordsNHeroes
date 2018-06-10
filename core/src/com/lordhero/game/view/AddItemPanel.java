package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.lordhero.game.Consts;
import com.lordhero.game.ISelectedItemProvider;

public class AddItemPanel extends UiPanel implements ISelectedItemProvider {
	private SelectBox<String> _itemSelection;
	
	public AddItemPanel() {
		super();
				
		_table.setPosition(150, 500);		   

		_table.row();

		_itemSelection = new SelectBox<String>(_skin);
		
		String[] itemSelection = {Consts.ItemKey, Consts.ItemDoor};
		
		_itemSelection.setItems(itemSelection);
		_table.add(_itemSelection).size(200, 32);
	}

	@Override
	public String getSelectedItem() {
		return _itemSelection.getSelected();
	}
}
