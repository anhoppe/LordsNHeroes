package com.lordhero.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.lordhero.game.Consts;
import com.lordhero.game.ISelectedCellProvider;

public class WorldEditor extends UiPanel implements ISelectedCellProvider {
	private Image _actor;
	
	private InputListener inputListener;
	
    int _xCursor;
    int _yCursor;
    
    int _xStartSelection = 1;
    int _yStartSelection = 1;
    int _xEndSelection = 1;
    int _yEndSelection = 1;
    
    SelectBox<String> _layerSelection;

	InputMultiplexer inputMultiplexer = new InputMultiplexer();

	public WorldEditor() 
	{
		super();
		
		_xCursor = 0;
		_yCursor = 0;
		
		_table.setPosition(30, 500);		   
        	    
		_table.row();
		
		_layerSelection = new SelectBox<String>(_skin);
		
		String[] layerSelection = {Consts.Background, Consts.Obstacles, Consts.Collision};
		
		_layerSelection.setItems(layerSelection);
		_table.add(_layerSelection).size(200, 32);
		
	    _table.row();

	    _actor = new Image(new Texture(Gdx.files.internal("MyTiles.png")));

	    _table.add(_actor).size(512, 512);
	    _table.setPosition(252, 500);
	    inputListener = new InputListener() {
	    	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
	    		_xStartSelection = (int)(x / 32f);
	    		_yStartSelection = (int)((512 - y) / 32f);
	    		
	    		return true;
	    	}
	    	
	    	public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	    		_xEndSelection = (int)(x / 32f);
	    		_yEndSelection = (int)((512 - y) / 32f);
	    				
	    	}	    
	    	
	    	public boolean mouseMoved(InputEvent event, float x, float y) {	    		
	    		return true;
	    	}	    	
	    };
	    
	    _actor.addListener(inputListener);
	    
		inputMultiplexer.addProcessor(super.getInputProcessor());
		inputMultiplexer.addProcessor(_actor.getStage());
	}
	
	@Override
	public int[][] getSelectedCellIndexArray() {
		int xStart = Math.min(_xStartSelection, _xEndSelection);
		int yStart = Math.min(_yStartSelection, _yEndSelection);
		int xEnd = Math.max(_xStartSelection, _xEndSelection);
		int yEnd = Math.max(_yStartSelection, _yEndSelection);
		
		int[][] selectedArray = new int[xEnd-xStart + 1][yEnd-yStart + 1];
		
		for (int x = xStart; x <= xEnd; x++) {
			for (int y = yStart; y <= yEnd; y++) {
				selectedArray[x-xStart][y-yStart] = y * (512/32) + x;
			}
		}
		
		return selectedArray;
	}
	
	@Override
	public int getSelectedCellPrice(int[][] selectedCells) {
		int price = 0;

		if (selectedCells != null) {

			int xStart = Math.min(_xStartSelection, _xEndSelection);
			int yStart = Math.min(_yStartSelection, _yEndSelection);
			int xEnd = Math.max(_xStartSelection, _xEndSelection);
			int yEnd = Math.max(_yStartSelection, _yEndSelection);

			for (int x = xStart; x <= xEnd; x++) {
				for (int y = yStart; y <= yEnd; y++) {
					price += 5;
				}
			}
		}
		
		return price;
	}
	
	@Override
	public String getLayerName() {
		return (String)_layerSelection.getSelected();
	}

	@Override
	public InputProcessor getInputProcessor()
	{		
		return inputMultiplexer;
	}
}
