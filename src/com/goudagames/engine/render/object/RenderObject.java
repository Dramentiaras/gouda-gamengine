package com.goudagames.engine.render.object;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.render.Program;
import com.goudagames.engine.system.GLSystem;
import com.goudagames.engine.util.Vertex;

public class RenderObject extends RenderObjectContainer {
	
	private Matrix4f model;
	public Color color;
	
	protected Program program;
	
	public RenderObject() {
		
		model = new Matrix4f();

		offset = new Vector2f(0f, 0f);
		setPosition(new Vector2f());
		color = new Color();
	}
	
	public void applyTransformations() {
		
		translate(getAbsolutePosition());
		rotate(rotation);
		translate(offset);
	}
	
	public void translate(Vector2f pos) {
		
		model.translate(pos);
	}
	
	public void rotate(float angle) {
		
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
	
	protected void setup(Vertex[] verticies) {
		
		program.use();
		setPosition(new Vector2f(Math.round(getPosition().x), Math.round(getPosition().y)));
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(verticies.length * Vertex.elementCount);
		for (int i = 0; i < verticies.length; i++) {
			vertexBuffer.put(verticies[i].getElements());
		}
		vertexBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GLSystem.instance().getVBO());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		GL20.glVertexAttribPointer(2, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
		
		GLSystem.instance().setProjectionUniform(program.getUniformLocation("projection"));
		setModelUniform(program.getUniformLocation("model"));
		
		if (view) {
			
			GLSystem.instance().setCameraUniform(program.getUniformLocation("view"));
		}
		else {
			
			FloatBuffer b = BufferUtils.createFloatBuffer(16);
			new Matrix4f().store(b); b.flip();
			GL20.glUniformMatrix4(program.getUniformLocation("view"), false, b);
		}
	}
	
	public void setModelUniform(int loc) {
		
		FloatBuffer b = BufferUtils.createFloatBuffer(16);
		model.store(b); b.flip();
		GL20.glUniformMatrix4(loc, false, b);
	}
	
	public void render() {
		
		super.render();
	}
}
