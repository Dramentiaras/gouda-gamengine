package com.goudagames.engine.render.object;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.goudagames.engine.color.Color;

public abstract class RenderBase {

	private Matrix4f model;
	public float rotation;
	public Vector2f offset;
	public Vector2f position;
	public Color color;
	
	public RenderBase() {
		
		model = new Matrix4f();

		offset = new Vector2f(0f, 0f);
		position = new Vector2f();
		color = new Color();
	}
	
	public void applyTransformations() {
		
		translate(position);
		rotate(rotation);
		translate(offset);
	}
	
	public void translate(Vector2f pos) {
		
		model.translate(pos);
	}
	
	public void rotate(float angle) {
		
		rotation += angle;
		model.rotate(angle, new Vector3f(0f, 0f, 1f));
	}
	
	public float getRotation() {
		
		return rotation;
	}
	
	public void scale(Vector2f scale) {
		
		model.scale(new Vector3f(scale.x, scale.y, 1f));
	}
	
	public Matrix4f getModelMatrix() {
		
		return model;
	}
	
	public void setModelMatrix(Matrix4f model) {
		
		this.model = model;
	}
	
	public void setModelUniform(int loc) {
		
		FloatBuffer b = BufferUtils.createFloatBuffer(16);
		model.store(b); b.flip();
		GL20.glUniformMatrix4(loc, false, b);
	}
	
	public abstract void render();
}
