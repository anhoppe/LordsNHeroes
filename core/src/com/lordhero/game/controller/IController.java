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
	boolean processMouseUp(int xScreen, int yScreen, int xCursor, int yCursor);
	boolean processMouseDown(int xScreen, int yScreen, int xCursor, int yCursor);
	boolean processMouseMove(int xPos, int yPos);
}
