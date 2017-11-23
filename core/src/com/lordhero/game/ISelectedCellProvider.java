package com.lordhero.game;

public interface ISelectedCellProvider {
	int[][] getSelectedCellIndexArray();
	int getSelectedCellPrice(int[][] selectedCells);
	
	String getLayerName();
	
}
