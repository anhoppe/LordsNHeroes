package com.lordhero.game.model;

public interface IEntityFactory {
	void createEnemy(String site, float _xPos, float _yPos);

	Missile createMissile(float xPos, float yPos, float _viewDirectionDeg, Dice damage);
}
