package com.lordhero.game.controller;

public interface IController {
	public enum InputEvent {
		None,
		KeyUp,
		MouseUp,
		MouseMove
	}
	void update();
	
	boolean processKeyUp(int keyCode);
	boolean processMouseUp(int xPos, int yPos);
	boolean processMouseMove(int xPos, int yPos);
}
