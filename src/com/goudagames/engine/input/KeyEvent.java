package com.goudagames.engine.input;

public class KeyEvent {

	private int key;
	private char character;
	private boolean pressed;
	private long time;
	
	public KeyEvent(int key, char character, boolean pressed, long time) {
		
		this.key = key;
		this.character = character;
		this.pressed = pressed;
		this.time = time;
	}
	
	public int getKeyCode() {
		
		return key;
	}
	
	public char getKeyCharacter() {
		
		return character;
	}
	
	public boolean isPressed() {
		
		return pressed;
	}
	
	public long getTime() {
		
		return time;
	}
}
