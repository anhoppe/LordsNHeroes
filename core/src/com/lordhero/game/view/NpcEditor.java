package com.lordhero.game.view;

import java.util.Arrays;
import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lordhero.game.UiPanel;
import com.lordhero.game.model.Npc;

public class NpcEditor extends UiPanel {

	private static final LinkedList<Npc> Npcs = new LinkedList<Npc>(Arrays.asList(new Npc("Hobo", 100), 
			new Npc("Blacksmith", 3500),
			new Npc("Bowyer", 2800),
			new Npc("Knight", 5000),
			new Npc("Town guard", 1000),
			new Npc("King", 10000),
			new Npc("Healer", 6000),
			new Npc("Landlord", 1500)));
		
	private Cell<?> _currentNpcTile;
	private Image _currentNpcImage;
	private int _currentNpcIndex = 0;
	
	private Texture _npcTexture;
	
	private TextField _classText;
	private TextField _priceText;	
			
	public NpcEditor() {
		super();
		
		_table.setPosition(150, 500);		   

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
		
		_table.add(_currentNpcImage).size(32, 32);

		final TextButton next = new TextButton(">>", _skin); 		
		_table.add(next);
		next.addCaptureListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (_currentNpcIndex < (Npcs.size() - 1)) {
					_currentNpcIndex++;
				}
				updateCurrentNpc();
				event.cancel();
			}
		});
		
		_table.row();
		_table.add(new Label("Class: ", _skin));		
		_classText = new TextField("", _skin);
		_table.add(_classText).size(100, 32);
		
		_table.row();
		_table.add(new Label("Class: ", _skin));
		_priceText = new TextField("", _skin);
		_table.add(_priceText).size(100, 32);
		
		updateCurrentNpc();
	}

	private void updateCurrentNpc() {
		_currentNpcTile.setActor(new Image(new TextureRegion(_npcTexture, _currentNpcIndex*32, 0, 32, 32)));
		Npc info = Npcs.get(_currentNpcIndex);
		
		_classText.setText(info.getName());
		_priceText.setText(Integer.toString(info.getPrice()));
	}
}
