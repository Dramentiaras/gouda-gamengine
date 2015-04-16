package com.goudagames.engine.state;

import com.goudagames.engine.gui.GuiScreen;



public abstract class GameState {
	
	public abstract void init();
	public abstract void destroy();
	public abstract void update(float delta);
	public abstract void render();
	
	public void switchGuiScreen(GuiScreen screen) {
		
	}
}
