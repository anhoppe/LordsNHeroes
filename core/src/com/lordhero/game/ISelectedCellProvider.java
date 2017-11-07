package com.lordhero.game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public interface ISelectedCellProvider {
	Image getSelectedCellImage();
	
	void decSelectedCellIndex();

	void incSelectedCellIndex();
}
