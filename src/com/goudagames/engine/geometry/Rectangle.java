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
	
	public Line[] getSides() {
		
		Line[] sides = new Line[] {new Line(new Vector2f(right(), down()), new Vector2f(right(), up())),
				new Line(new Vector2f(right(), up()), new Vector2f(left(), up())),
				new Line(new Vector2f(left(), up()), new Vector2f(left(), down())),
				new Line(new Vector2f(right(), down()), new Vector2f(left(), down()))};
		
		return sides;
	}
	
	public boolean isIntersecting(Rectangle r) {
		
		float xd = x - r.x;
		float yd = y - r.y;
		
		if (xd < 0) xd *= -1f;
		if (yd < 0) yd *= -1f;
		
		return (xd < (width / 2f + r.width / 2f)) &&
				(yd < (height / 2f + r.height / 2f));
	}
	
	public boolean isPointInside(Vector2f pos) {
		
		if (pos.x > left() && pos.x < right() && pos.y > down() && pos.y < up()) {
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		
		return "(x: " + x + ", y: " + y + ", w: " + width + ", h: " + height +")";
	}
}
