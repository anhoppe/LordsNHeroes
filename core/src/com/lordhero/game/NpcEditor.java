package com.lordhero.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NpcEditor extends UiPanel {
	
	private static final int NpcCount = 9;	
	
	private Cell<?> _currentNpcTile;
	private Image _currentNpcImage;
	private int _currentNpcIndex = 0;
	
	private Texture _npcTexture;
	
	public NpcEditor() {
		super();
		
		_table.setPosition(50, 500);		   

		_table.row();
		
		final TextButton prev = new TextButton("<<", _skin);
		_table.add(prev);
		prev.addCaptureListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {

				if (_currentNpcIndex > 0) {
					_currentNpcIndex--;
				}
				updateCurrentNpc();
				event.cancel();
			}	   
		});
	
		_npcTexture = new Texture(Gdx.files.internal("Npcs.png"));
		
		_currentNpcTile = _table.add().size(32, 32);
		updateCurrentNpc();
		
		_table.add(_currentNpcImage).size(32, 32);

		final TextButton next = new TextButton(">>", _skin); 		
		_table.add(next);
		next.addCaptureListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {

				if (_currentNpcIndex < NpcCount) {
					_currentNpcIndex++;
				}
				updateCurrentNpc();
				event.cancel();
			}	   
		});
		
	}

	private void updateCurrentNpc() {
		_currentNpcTile.setActor(new Image(new TextureRegion(_npcTexture,  _currentNpcIndex*32, 0, 32, 32)));
	}
}
