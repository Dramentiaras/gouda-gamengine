package com.goudagames.engine.loop;

public class GameLoop extends Loop {

	public GameLoop(int fps) {
		
		super(fps);
	}
	
	public GameLoop() {
		
		this(60);
	}
}
