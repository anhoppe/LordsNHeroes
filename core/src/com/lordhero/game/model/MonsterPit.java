package com.lordhero.game.model;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class MonsterPit extends EntityBase {

	private Texture _image;
	
	public MonsterPit(int xPos, int yPos) {
		super(xPos, yPos);
		restore();
	}

	public MonsterPit(Element entityNode) {
		super(entityNode.getChildByName("EntityBase"));
		restore();
	}
	
	@Override
	public void write(XmlWriter writer) throws IOException {		
		writer.element("MonsterPit");
		super.write(writer);
		writer.pop();
	}
	
	@Override
	public void restore() {
		_image = new Texture(Gdx.files.internal("data/tileset/MonsterPit.png"));
		_sprite = new Sprite(_image);
		_sprite.setCenter(_xPos, _yPos);		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
