package com.lordhero.game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public interface ISelectedCellProvider {
	int[][] getSelectedCellIndexArray();
	
	String getLayerName();
	
//	Image getSelectedCellImage();
//	
//	void decSelectedCellIndex();
//
//	void incSelectedCellIndex();
}
