package com.goudagames.engine.geometry;

import org.lwjgl.util.vector.Vector2f;

public class Line {
	
	public Vector2f v0, v1;
	
	public Line(Vector2f v0, Vector2f v1) {
		
		this.v0 = v0;
		this.v1 = v1;
	}
	
	public float length() {
		
		float x = v0.x - v1.x;
		float y = v0.y - v1.y;
		
		float length = (float)Math.sqrt(x * x + y * y);
		
		return length;
	}
}
