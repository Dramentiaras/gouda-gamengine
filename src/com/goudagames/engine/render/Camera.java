package com.goudagames.engine.render;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	public float x, y, scale;
	public float rotation;
	
	public Camera(float x, float y) {
		
		this.x = x;
		this.y = y;
		this.scale = 1f;
		this.rotation = 0f;
	}
	
	public Matrix4f getMatrix() {
		
		Matrix4f result = new Matrix4f();
		
		result.translate(new Vector2f(x, y));
		result.rotate(rotation, new Vector3f(0f, 0f, 1f));
		result.scale(new Vector3f(scale, scale, 1f));
		
		return result;
	}
}
