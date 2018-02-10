package com.lordhero.game.model;

import java.util.concurrent.ThreadLocalRandom;

public class Dice {
	private int _rollCount;
	private int _spots;
	private int _constant;
	
	public Dice(int rollCount, int spots, int constant) {
		_rollCount = rollCount;
		_spots = spots;
		_constant = constant;
	}
	
	public int roll() {
		int result = 0;
		for (int i = 0; i < _rollCount; i++) {
			result += ThreadLocalRandom.current().nextInt(1, _spots + 1);
		}
		
		return result +_constant;
	}
}
