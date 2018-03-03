package com.lordhero.game.model.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface IMeleeWeapon extends IWeapon {

	void startAttack();

	TextureRegion getWeaponAnimation();

	boolean attacks();

}
