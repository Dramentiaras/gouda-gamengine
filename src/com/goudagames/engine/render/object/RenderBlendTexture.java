package com.goudagames.engine.render.object;

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
import com.goudagames.engine.render.BlendMode;
import com.goudagames.engine.render.Program;
import com.goudagames.engine.system.GLSystem;
import com.goudagames.engine.util.Vertex;

public class RenderBlendTexture extends RenderObject {

	Vertex[] quad;
	Vector2f[] texCoords;
	public Vector2f size = new Vector2f();
	static int vboi = -1;
	
	boolean ignoreAlpha = false;
	
	private Texture texture0, texture1;
	private Program program;
	private int mode;
	
	public RenderBlendTexture(Texture texture0, Texture texture1, int mode) {
		
		this.texture0 = texture0;
		this.texture1 = texture1;
		
		this.mode = mode;
		setProgram(mode);
		
		quad = new Vertex[4];
		texCoords = new Vector2f[4];
		
		quad[0] = new Vertex().setXY(-0.5f, 0.5f).setColor(new Color()).setUV(0f, 0f);
		quad[1] = new Vertex().setXY(-0.5f, -0.5f).setColor(new Color()).setUV(0f, 1f);
		quad[2] = new Vertex().setXY(0.5f, -0.5f).setColor(new Color()).setUV(1f, 1f);
		quad[3] = new Vertex().setXY(0.5f, 0.5f).setColor(new Color()).setUV(1f, 0f);
		
		texCoords[0] = new Vector2f(0f, 0f);
		texCoords[1] = new Vector2f(0f, 1f);
		texCoords[2] = new Vector2f(1f, 1f);
		texCoords[3] = new Vector2f(1f, 0f);
		
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
	
	public void shouldIgnoreAlpha(boolean mult) {
		
		ignoreAlpha = mult;
	}
	
	public void setUV1(Vector2f pos, Vector2f size, boolean normalize) {
		
		if (normalize) {
			
			pos.x /= texture0.getTextureWidth();
			pos.y /= texture0.getTextureHeight();
			size.x /= texture0.getTextureWidth();
			size.y /= texture0.getTextureHeight();
		}
		
		texCoords[0] = new Vector2f(pos.x, pos.y);
		texCoords[1] = new Vector2f(pos.x, pos.y + size.y);
		texCoords[2] = new Vector2f(pos.x + size.x, pos.y + size.y);
		texCoords[3] = new Vector2f(pos.x + size.x, pos.y);
	}
	
	public void setUV0(Vector2f pos, Vector2f size, boolean normalize) {
		
		if (normalize) {
			
			pos.x /= texture0.getTextureWidth();
			pos.y /= texture0.getTextureHeight();
			size.x /= texture0.getTextureWidth();
			size.y /= texture0.getTextureHeight();
		}
		
		quad[0].setUV(pos.x, pos.y);
		quad[1].setUV(pos.x, pos.y + size.y);
		quad[2].setUV(pos.x + size.x, pos.y + size.y);
		quad[3].setUV(pos.x + size.x, pos.y);
	}
	
	public void setTexture1(Texture texture) {
		
		this.texture1 = texture;
	}
	
	public Texture getTexture1() {
		
		return texture1;
	}
	
	public void setTexture0(Texture texture) {
		
		this.texture0 = texture;
	}
	
	public Texture getTexture0() {
		
		return texture0;
	}
	
	public void setMode(int mode) {
		
		if (this.mode != mode) {
			
			this.mode = mode;
			setProgram(mode);
		}
	}
	
	private void setProgram(int mode) {
		
		switch (mode) {
		
			case BlendMode.MULTIPLY: {
				program = Program.MULTIPLY;
				return;
			}
			case BlendMode.ADD: {
				program = Program.ADD;
				return;
			}
			default: {
				program = Program.MULTIPLY;
				return;
			}
		}
	}
	
	@Override
	public void render() {
		
		super.render();
		
		program.use();
		
		quad[0].setColor(color); quad[1].setColor(color); quad[2].setColor(color); quad[3].setColor(color);
		
		position = new Vector2f(Math.round(position.x), Math.round(position.y));
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(quad.length * (Vertex.elementCount + Vertex.textureElementCount));
		for (int i = 0; i < quad.length; i++) {
			vertexBuffer.put(quad[i].getElements());
			vertexBuffer.put(new float[] {texCoords[i].x, texCoords[i].y});
		}
		vertexBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GLSystem.instance().getVBO());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride + Vertex.textureByteCount, Vertex.positionByteOffset);
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride + Vertex.textureByteCount, Vertex.colorByteOffset);
		GL20.glVertexAttribPointer(2, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride + Vertex.textureByteCount, Vertex.textureByteOffset);
		GL20.glVertexAttribPointer(3, Vertex.textureElementCount, GL11.GL_FLOAT, false, 
				Vertex.stride +  + Vertex.textureByteCount, Vertex.textureByteOffset + Vertex.textureByteCount);
		
		GL20.glEnableVertexAttribArray(3);
		
		translate(position);
		scale(size);
		
		GLSystem.instance().setProjectionUniform(program.getUniformLocation("projection"));
		setModelUniform(program.getUniformLocation("model"));
		
		if (view) {
			
			GLSystem.instance().setCameraUniform(Program.TEXTURE.getUniformLocation("view"));
		}
		else {
			
			FloatBuffer b = BufferUtils.createFloatBuffer(16);
			new Matrix4f().store(b); b.flip();
			GL20.glUniformMatrix4(Program.TEXTURE.getUniformLocation("view"), false, b);
		}
		
		GL20.glUniform1i(program.getUniformLocation("texture0_diffuse"), 0);
		GL20.glUniform1i(program.getUniformLocation("texture1_diffuse"), 1);
		
		GL20.glUniform1i(program.getUniformLocation("ignoreAlpha"), ignoreAlpha ? 1:0);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture0.getTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture1.getTextureID());
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboi);
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_BYTE, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
		GL20.glDisableVertexAttribArray(3);
		
		setModelMatrix(new Matrix4f());
	}
}
