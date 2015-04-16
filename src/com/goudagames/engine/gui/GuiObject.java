package com.goudagames.engine.gui;

import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.geometry.Rectangle;

public class GuiObject extends GuiBase {

	public float x, y, width, height;
	public GuiScreen parent;
	
	public GuiObject(float x, float y, float width, float height, GuiScreen parent) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.parent = parent;
	}
	
	@Override
	public void onClick(Vector2f pos) {
		
		super.onClick(pos);
	}
	
	public Rectangle getBounds() {
		
		return new Rectangle(x, y, width, height);
	}
	
	public void update() {
		
	}
}
