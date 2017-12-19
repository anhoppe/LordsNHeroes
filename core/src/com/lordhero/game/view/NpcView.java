package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;
import com.lordhero.game.model.Npc;
import com.lordhero.game.model.items.IItem;

public class NpcView extends UiPanel implements INpcSelectionReceiver {
	private TextField _nameText;
	
	private List<String> _itemsList;
	
	public NpcView() {
		_table.setPosition(150, 500);		   

		_table.row();
	
		_table.add(new Label("Name:", _skin));
		
		_nameText = new TextField("", _skin);
		_table.add(_nameText).size(80, 32);
		_table.setBounds(100,  550, 100, 25);
		
		_table.row();
		_itemsList = new List<String>(_skin);
		
		_table.add(new ScrollPane(_itemsList)).size(200, 300);
	}

	@Override
	public void select(Npc npc) {
		_nameText.setText("");
		_itemsList.clear();

		if (npc != null) {
			_nameText.setText(npc.getName());
			
			java.util.List<IItem> items = npc.getItems();
			
			if (items != null) {
				Array<String> itemArray = new Array<String>();
				for (IItem item : items) {
					itemArray.add(item.getName());
				}		
				
				_itemsList.setItems(itemArray);
				
			}
		}
	}	
}
