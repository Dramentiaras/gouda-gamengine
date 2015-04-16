package com.goudagames.engine.loop;

import org.lwjgl.opengl.Display;

import com.goudagames.engine.input.Input;
import com.goudagames.engine.logging.Log;
import com.goudagames.engine.start.Engine;
import com.goudagames.engine.util.Time;

public class Loop {

	int fps;
	boolean running;
	
	public Loop(int fps) {
		
		this.fps = fps;
	}
	
	public void start() {
	
		running = true;
		
		pre();
		while(running) {
			
			update();
			Display.sync(fps);
		}
		post();
	}
	
	public void stop() {
		
		Log.log("Stopping loop...");
		running = false;
	}
	
	public void pre() {

	}
	
	public void update() {
		
		if (Display.isCloseRequested()) {
			
			stop();
		}
		
		Engine.update();
		Input.update();
		
		Display.update();
		Time.update();
	}
	
	public void post() {
		
		Engine.destroy();
	}
}
