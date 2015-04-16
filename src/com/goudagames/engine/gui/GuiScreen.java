package com.goudagames.engine.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.input.Input;
import com.goudagames.engine.render.RenderQuad;

public class GuiScreen extends GuiBase {

	boolean renderDefaultBackground = true;
	ArrayList<GuiObject> objects = new ArrayList<GuiObject>();
	
	public GuiScreen(boolean defaultBG) {
		
		super();
		renderDefaultBackground = defaultBG;
	}
	
	public void init() {
		
	}
	
	public void destroy() {
		
	}
	
	public boolean shouldPauseState() {
		
		return false;
	}
	
	public void addGuiObject(GuiObject o) {
		
		objects.add(o);
	}
	
	public void removeGuiObject(GuiObject o) {
		
		objects.remove(o);
	}
	
	public boolean shouldRenderDefaultBackground() {
		
		return renderDefaultBackground;
	}
	
	public void setRenderDefaultBackground(boolean b) {
		
		this.renderDefaultBackground = b;
	}
	
	private void renderDefaultBackground() {
		
		RenderQuad quad = new RenderQuad();
		
		quad.position = new Vector2f(Display.getWidth() / 2f, Display.getHeight() / 2f);
		quad.size = new Vector2f(Display.getWidth(), Display.getHeight());
		quad.color = new Color(0f, 0f, 0f, 0.5f);
		
		quad.render();
	}
	
	public void renderBackground() {
		
	}
	
	public void renderForeground() {
		
		for (GuiObject o : objects) {
			
			o.render();
		}
	}
	
	@Override
	public void render() {
		
		if (this.renderDefaultBackground) {
			
			renderDefaultBackground();
		}
		else {
			
			renderBackground();
		}
		
		renderForeground();
	}
	
	public void update() {
		
		for (GuiObject o : objects) {
			
			o.update();
			
			if (Input.buttonPressed(0)) {
				
				Vector2f mPos = Input.mousePos();
				
				if (o.getBounds().isPointInside(mPos)) {
				
					o.onClick(mPos);
				}
			}
		}
	}
}
