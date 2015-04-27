package com.goudagames.engine.render.object;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.render.Program;
import com.goudagames.engine.util.Vertex;

public class RenderTriangleOutline extends RenderObject {
	
	Vertex[] tri;
	public Vector2f size = new Vector2f(1f, 1f);
	
	private static int vboi = -1;
	
	private boolean setArray = false;
	
	public RenderTriangleOutline() {
		
		super();
		
		program = Program.BASIC;
		
		tri = new Vertex[3];
		
		tri[0] = new Vertex().setXY(-0.5f, -0.5f).setColor(new Color());
		tri[1] = new Vertex().setXY(0f, 0.5f).setColor(new Color());
		tri[2] = new Vertex().setXY(0.5f, -0.5f).setColor(new Color());
		
		if (vboi == -1) {
			
			byte[] indices = {
					0, 1, 2, 0
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

	public RenderTriangleOutline(Vertex[] tri) {
		
		super();
		
		program = Program.BASIC;
		
		this.tri = tri;
		setArray = true;
		
		if (vboi == -1) {
			
			byte[] indices = {
					0, 1, 2, 0
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
			tri[0].setColor(color); tri[1].setColor(color); tri[2].setColor(color);;
		}
		
		applyTransformations();
		scale(size);
		
		setup(tri);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
		
		GL11.glDrawElements(GL11.GL_LINE_STRIP, 4, GL11.GL_UNSIGNED_BYTE, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		setModelMatrix(new Matrix4f());
	}
}
