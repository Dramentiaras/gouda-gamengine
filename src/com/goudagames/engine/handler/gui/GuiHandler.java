package com.goudagames.engine.handler.gui;

import com.goudagames.engine.gui.GuiScreen;

public class GuiHandler {

	public static GuiHandler INSTANCE;
	
	public GuiScreen mainScreen;
	
	public static void init() {
		
		new GuiHandler();
	}
	
	public GuiHandler() {
		
		INSTANCE = this;
	}
	
	public void render() {
		
	 	if (mainScreen != null) {
	 		
			mainScreen.render();
	 	}
	}
	
	public void update() {
		
		if (mainScreen != null) {
			
			mainScreen.update();
		}
	}
	
	public void switchMainScreen(GuiScreen screen) {
		
		if (mainScreen != null) {
			
			mainScreen.destroy();
		}
 		this.mainScreen = screen;
 		if (mainScreen != null) {
 			
 			mainScreen.init();
 		}
	}
	
	public boolean isStatePaused() {
		
		return mainScreen != null && mainScreen.shouldPauseState();
	}
}
