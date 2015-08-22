package com.goudagames.engine.render.object;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.geometry.Rectangle;
import com.goudagames.engine.render.Program;
import com.goudagames.engine.util.Vertex;

public class RenderQuad extends RenderObject {

	Vertex[] quad;
	public Vector2f size = new Vector2f(1f, 1f);
	static int vboi = -1;
	
	boolean setArray = false;
	
	public RenderQuad() {
		
		super();
		
		program = Program.BASIC;
		
		quad = new Vertex[4];
		
		quad[0] = new Vertex().setXY(-0.5f, 0.5f).setColor(new Color());
		quad[1] = new Vertex().setXY(-0.5f, -0.5f).setColor(new Color());
		quad[2] = new Vertex().setXY(0.5f, -0.5f).setColor(new Color());
		quad[3] = new Vertex().setXY(0.5f, 0.5f).setColor(new Color());
		
		if (vboi == -1) {
			
			byte[] indices = {
					0, 1, 2,
					2, 3, 0
				};
				
			ByteBuffer indexBuffer = BufferUtils.createByteBuffer(indices.length);
			indexBuffer.put(indices);
			indexBuffer.flip();
				
			vboi = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
	}
	
	public RenderQuad(Vertex[] quad) {
		
		super();
		
		program = Program.BASIC;
		
		this.quad = quad;
		setArray = true;
		
		if (vboi == -1) {
			
			byte[] indices = {
					0, 1, 2,
					2, 3, 0
				};
				
			ByteBuffer indexBuffer = BufferUtils.createByteBuffer(indices.length);
			indexBuffer.put(indices);
			indexBuffer.flip();
				
			vboi = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL15.GL_STATIC_DRAW);
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
	}
	
	@Override
	public void render() {
		
		super.render();
		
		if (!setArray) {
			quad[0].setColor(color); quad[1].setColor(color); quad[2].setColor(color); quad[3].setColor(color);
		}
		
		applyTransformations();
		scale(size);
		
		setup(quad);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		setModelMatrix(new Matrix4f());
	}
	
	public Rectangle getBounds() {
		
		return new Rectangle(getAbsolutePosition().x, getAbsolutePosition().y, size.x * (size.x < 0 ? -1:1), size.y * (size.y < 0 ? -1:1));
	}
}
