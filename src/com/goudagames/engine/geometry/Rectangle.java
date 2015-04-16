package com.goudagames.engine.geometry;

import org.lwjgl.util.vector.Vector2f;

public class Rectangle {

	public float x, y, width, height;
	
	public Rectangle(float x, float y, float width, float height) {
		
		this.x = x; 
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public float up() {
		
		return y + height / 2f;
	}
	
	public float right() {
		
		return x + width / 2f;
	}
	
	public float down() {
		
		return y - height / 2f;
	}
	
	public float left() {
		
		return x - width / 2f;
	}
	
	public boolean isIntersecting(Rectangle r) {
		
		if (left() > r.left() && left() < r.right() || right() > r.left() && right() < r.right()) {
			if (up() > r.down() && up() < r.up() || down() > r.down() && down() < r.up()) {
				System.out.println(true);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean isPointInside(Vector2f pos) {
		
		if (pos.x > left() && pos.x < right() && pos.y > down() && pos.y < up()) {
			
			return true;
		}
		
		return false;
	}
}
