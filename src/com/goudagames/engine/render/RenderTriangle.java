package com.goudagames.engine.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.util.Vertex;

public class RenderTriangle extends RenderBase {

	Vertex[] tri;
	public Vector2f size = new Vector2f(1f, 1f);
	public boolean useCamera = true;
	
	private boolean setArray = false;
	
	public RenderTriangle() {
		
		super();
		
		tri = new Vertex[3];
		
		tri[0] = new Vertex().setXY(-0.5f, -0.5f).setColor(new Color());
		tri[1] = new Vertex().setXY(0f, 0.5f).setColor(new Color());
		tri[2] = new Vertex().setXY(0.5f, -0.5f).setColor(new Color());
	}

	public RenderTriangle(Vertex[] tri) {
		
		super();
		
		this.tri = tri;
		setArray = true;
	}
	
	@Override
	public void render() {
		
		Program.BASIC.use();
		
		if (!setArray) {
			tri[0].setColor(color); tri[1].setColor(color); tri[2].setColor(color);;
		}
		
		position = new Vector2f(Math.round(position.x), Math.round(position.y));
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(tri.length * Vertex.elementCount);
		for (int i = 0; i < tri.length; i++) {
			vertexBuffer.put(tri[i].getElements());
		}
		vertexBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, RenderEngine.instance().getVBO());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		GL20.glVertexAttribPointer(2, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
		
		translate(position);
		rotate(rotation);
		scale(size);
		
		RenderEngine.instance().setProjectionUniform(Program.BASIC.getUniformLocation("projection"));
		setModelUniform(Program.BASIC.getUniformLocation("model"));
		
		if (useCamera) {
			
			RenderEngine.instance().setCameraUniform(Program.TEXTURE.getUniformLocation("view"));
		}
		else {
			
			FloatBuffer b = BufferUtils.createFloatBuffer(16);
			new Matrix4f().store(b); b.flip();
			GL20.glUniformMatrix4(Program.TEXTURE.getUniformLocation("view"), false, b);
		}
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		setModelMatrix(new Matrix4f());
	}
}
