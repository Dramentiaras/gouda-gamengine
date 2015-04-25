package com.goudagames.engine.render.object;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.geometry.Rectangle;
import com.goudagames.engine.render.Program;
import com.goudagames.engine.system.GLSystem;
import com.goudagames.engine.util.Vertex;

public class RenderQuadOutline extends RenderBase {

	Vertex[] quad;
	public Vector2f size = new Vector2f(1f, 1f);
	public boolean view = true;
	
	boolean setArray = false;
	
	public RenderQuadOutline() {
		
		super();
		
		quad = new Vertex[4];
		
		quad[0] = new Vertex().setXY(-0.5f, 0.5f).setColor(new Color());
		quad[1] = new Vertex().setXY(-0.5f, -0.5f).setColor(new Color());
		quad[2] = new Vertex().setXY(0.5f, -0.5f).setColor(new Color());
		quad[3] = new Vertex().setXY(0.5f, 0.5f).setColor(new Color());
	}
	
	public RenderQuadOutline(Vertex[] quad) {
		
		super();
		
		this.quad = quad;
		setArray = true;
	}
	
	@Override
	public void render() {
		
		Program.BASIC.use();
		
		if (!setArray) {
			quad[0].setColor(color); quad[1].setColor(color); quad[2].setColor(color); quad[3].setColor(color);
		}
		
		position = new Vector2f(Math.round(position.x), Math.round(position.y));
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(quad.length * Vertex.elementCount);
		for (int i = 0; i < quad.length; i++) {
			vertexBuffer.put(quad[i].getElements());
		}
		vertexBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GLSystem.instance().getVBO());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		GL20.glVertexAttribPointer(2, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
		
		translate(position);
		rotate(rotation);
		scale(size);
		
		GLSystem.instance().setProjectionUniform(Program.BASIC.getUniformLocation("projection"));
		setModelUniform(Program.BASIC.getUniformLocation("model"));
		
		if (view) {
			
			GLSystem.instance().setCameraUniform(Program.TEXTURE.getUniformLocation("view"));
		}
		else {
			
			FloatBuffer b = BufferUtils.createFloatBuffer(16);
			new Matrix4f().store(b); b.flip();
			GL20.glUniformMatrix4(Program.TEXTURE.getUniformLocation("view"), false, b);
		}
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, 4);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		setModelMatrix(new Matrix4f());
	}
	
	public Rectangle getBounds() {
		
		return new Rectangle(position.x, position.y, size.x * (size.x < 0 ? -1:1), size.y * (size.y < 0 ? -1:1));
	}
}
