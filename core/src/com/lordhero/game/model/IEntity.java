package com.lordhero.game.model;

import com.badlogic.gdx.graphics.g2d.Sprite;

public interface IEntity {

	void update();

	boolean isTerminated();

	Sprite getSprite();
}
