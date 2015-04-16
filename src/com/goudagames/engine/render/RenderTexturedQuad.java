package com.goudagames.engine.render;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.assets.Texture;
import com.goudagames.engine.color.Color;
import com.goudagames.engine.geometry.Rectangle;
import com.goudagames.engine.util.Vertex;

public class RenderTexturedQuad extends RenderBase {

	Vertex[] quad;
	public Vector2f size = new Vector2f();
	public boolean useCamera = true;
	static int vboi = -1;
	
	private Texture texture;
	
	private boolean setArray = false;
	
	public RenderTexturedQuad(Texture texture) {
		
		super();
		
		this.texture = texture;
		
		quad = new Vertex[4];
		
		quad[0] = new Vertex().setXY(-0.5f, 0.5f).setColor(new Color()).setUV(0f, 0f);
		quad[1] = new Vertex().setXY(-0.5f, -0.5f).setColor(new Color()).setUV(0f, 1f);
		quad[2] = new Vertex().setXY(0.5f, -0.5f).setColor(new Color()).setUV(1f, 1f);
		quad[3] = new Vertex().setXY(0.5f, 0.5f).setColor(new Color()).setUV(1f, 0f);
		
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
	
	public RenderTexturedQuad(Texture texture, Vertex[] quad) {
		
		super();
		
		this.texture = texture;
		
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
	
	public void setUV(Vector2f pos, Vector2f size, boolean normalize) {
		
		if (normalize) {
			
			pos.x /= texture.getTextureWidth();
			pos.y /= texture.getTextureHeight();
			size.x /= texture.getTextureWidth();
			size.y /= texture.getTextureHeight();
		}
		
		quad[0].setUV(pos.x, pos.y);
		quad[1].setUV(pos.x, pos.y + size.y);
		quad[2].setUV(pos.x + size.x, pos.y + size.y);
		quad[3].setUV(pos.x + size.x, pos.y);
	}
	
	public void setTexture(Texture texture) {
		
		this.texture = texture;
	}
	
	public Texture getTexture() {
		
		return texture;
	}
	
	@Override
	public void render() {
		
		Program.TEXTURE.use();
		
		if (!setArray) {
			quad[0].setColor(color); quad[1].setColor(color); quad[2].setColor(color); quad[3].setColor(color);
		}
		
		position = new Vector2f(Math.round(position.x), Math.round(position.y));
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(quad.length * Vertex.elementCount);
		for (int i = 0; i < quad.length; i++) {
			vertexBuffer.put(quad[i].getElements());
		}
		vertexBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, RenderEngine.instance().getVBO());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		GL20.glVertexAttribPointer(2, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
		
		applyTransformations();
		scale(size);
		
		RenderEngine.instance().setProjectionUniform(Program.TEXTURE.getUniformLocation("projection"));
		setModelUniform(Program.TEXTURE.getUniformLocation("model"));
		
		if (useCamera) {
			
			RenderEngine.instance().setCameraUniform(Program.TEXTURE.getUniformLocation("view"));
		}
		else {
			
			FloatBuffer b = BufferUtils.createFloatBuffer(16);
			new Matrix4f().store(b); b.flip();
			GL20.glUniformMatrix4(Program.TEXTURE.getUniformLocation("view"), false, b);
		}
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		setModelMatrix(new Matrix4f());
	}
	
	public Rectangle getBounds() {
		
		return new Rectangle(position.x, position.y, size.x * (size.x < 0 ? -1:1), size.y * (size.y < 0 ? -1:1));
	}
}
