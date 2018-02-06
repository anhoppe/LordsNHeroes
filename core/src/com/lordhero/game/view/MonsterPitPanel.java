package com.lordhero.game.view;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;

public class MonsterPitPanel extends UiPanel {
	public MonsterPitPanel() {
		super();
		
		_table.setPosition(150, 500);

		_table.row();

		_table.add(new CheckBox("Add monster pit", _skin));
	}
}
