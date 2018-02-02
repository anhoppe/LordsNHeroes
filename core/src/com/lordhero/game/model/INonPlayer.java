package com.lordhero.game.model;

public interface INonPlayer extends IEntity {
	void update(IPlayer player);

	boolean isTerminated();
}
