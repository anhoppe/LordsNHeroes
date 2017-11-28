package com.lordhero.game;

public class NpcInfo {
	private String _name;
	
	private int _price;
	
	public NpcInfo(String name, int price) {
		_name = name;
		_price = price;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getPrice() {
		return _price;
	}
}
