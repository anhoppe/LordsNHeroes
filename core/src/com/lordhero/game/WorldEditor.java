package com.lordhero.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class WorldEditor extends UiPanel implements ISelectedCellProvider {
	private Cell<?> _selectedTileCell;
	private Cell<?> _backgroundTiles;
		
	private Image _actor;
	
	private InputListener inputListener;
	
    private SpriteBatch _spriteBatch;

    private Texture _cursorImage;
    private Sprite _cursorSprite;
    private Image _cursorImage2;
    
    int _xCursor;
    int _yCursor;
    
    int _xStartSelection = 1;
    int _yStartSelection = 1;
    int _xEndSelection = 1;
    int _yEndSelection = 1;
    
    SelectBox _layerSelection;

	public WorldEditor() 
	{
		super();
		
		_spriteBatch = new SpriteBatch();

		_cursorImage2 = new Image(new Texture(Gdx.files.internal("cursor.png")));
		_cursorImage = new Texture(Gdx.files.internal("cursor.png"));
		_cursorSprite = new Sprite(_cursorImage);
		_xCursor = 0;
		_yCursor = 0;
		
		_table.setPosition(30, 500);		   
        	    
		_table.row();
		
		_layerSelection = new SelectBox(_skin);
		
		String[] layerSelection = {"Background", "Obstacles", "Collision"};
		
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
	    
		//_stage.addActor(_cursorImage2);
		
		//	    
////	    _backgroundTiles = _table.add().size(512, 512);
//		Image image = new Image(new Texture(Gdx.files.internal("MyTiles.png")));
//		_backgroundTiles.setActor(image);	    
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
	public String getLayerName() {
		return (String)_layerSelection.getSelected();
	}

	@Override
	public InputProcessor getInputProcessor()
	{
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(super.getInputProcessor());
		inputMultiplexer.addProcessor(_actor.getStage());
		
		return inputMultiplexer;
	}
	
}
