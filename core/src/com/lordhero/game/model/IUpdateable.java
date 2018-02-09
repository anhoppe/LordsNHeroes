package com.lordhero.game.model;

import com.lordhero.game.IGameMode;

public interface IUpdateable extends IEntity {
	void update(IPlayer player, IGameMode gameMode);
}
