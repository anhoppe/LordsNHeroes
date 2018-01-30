package com.lordhero.game.model.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface IWeapon {

	void startAttack();

	TextureRegion getWeaponAnimation();

	boolean attacks();

	int hit();

}
