package com.lordhero.game.model;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.lordhero.game.IGameMode;
import com.lordhero.game.IGameMode.GameMode;

public class MonsterPit extends EntityBase implements IUpdateable {

	private Texture _image;
	
	private float _monstersPropabilityPerSecond = 0.1f;
	
	private float _elapsedTime = 0;
	
	private IEntityFactory _entityFactory;
	
	private String _site;
	
	public MonsterPit(int xPos, int yPos, IEntityFactory entityFactory, String site) {
		super(xPos, yPos);
		_entityFactory = entityFactory;
		_site = site;
		restore();
	}

	public MonsterPit(Element monsterPitNode, IEntityFactory entityFactory) {
		super(monsterPitNode.getChildByName("EntityBase"));
		_site = monsterPitNode.getAttribute("Site");
		_entityFactory = entityFactory;
		restore();
	}
	
	@Override
	public void write(XmlWriter writer) throws IOException {		
		writer.element("MonsterPit").attribute("Site", _site);
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

	@Override
	public void update(IPlayer player, IGameMode gameMode) {		
		if (gameMode.is(GameMode.Play)) {
			_elapsedTime += Gdx.graphics.getDeltaTime();
			
			if (_elapsedTime > 1) {
				_elapsedTime = 0;
				if (Math.random() <= _monstersPropabilityPerSecond) {
					_entityFactory.createEnemy(_site, _xPos, _yPos);
				}
			}		
		}
	}
}
