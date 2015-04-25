package com.goudagames.engine.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.input.Input;
import com.goudagames.engine.render.object.RenderQuad;

public class GuiScreen extends GuiBase {

	boolean renderDefaultBackground = true;
	ArrayList<GuiObject> objects = new ArrayList<GuiObject>();
	ArrayList<GuiObject> additions = new ArrayList<GuiObject>();
	ArrayList<GuiObject> removals = new ArrayList<GuiObject>();
	
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
		
		additions.add(o);
	}
	
	public void removeGuiObject(GuiObject o) {
		
		removals.add(o);
	}
	
	public boolean shouldRenderDefaultBackground() {
		
		return renderDefaultBackground;
	}
	
	public void setRenderDefaultBackground(boolean b) {
		
		this.renderDefaultBackground = b;
	}
	
	private void renderDefaultBackground() {
		
		RenderQuad quad = new RenderQuad();
		
		quad.view = false;
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
			
			Vector2f mPos = Input.mousePos();
			
			if (o.getBounds().isPointInside(mPos)) {
			
				o.hover = true;
				
				if (Input.buttonPressed(0)) {
					o.onClick(mPos);
				}
			}
			else {
				
				o.hover = false;
			}
		}
		
		for (GuiObject o : additions) {
			
			objects.add(o);
		}
		
		for (GuiObject o : removals) {
			
			objects.remove(o);
		}
		
		additions.clear();
		removals.clear();
	}
}
