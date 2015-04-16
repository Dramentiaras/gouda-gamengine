package com.goudagames.engine.render;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.goudagames.engine.assets.Texture;
import com.goudagames.engine.color.Color;

public class FrameBufferObject {
	
	int id;
	public int size;
	Texture texture;
	
	public Color color;
	
	public FrameBufferObject(int size) {
		
		this.size = size;
		
		id = GL30.glGenFramebuffers();
		texture = new Texture(GL11.glGenTextures(), size, size);
		
		color = new Color();
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, size, size, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, texture.getTextureID(), 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	IntBuffer viewport;
	Matrix4f proj;
	FrameBufferObject prevBuffer;
	
	public void push() {
		
		proj = RenderEngine.instance().getProjection();
		viewport = BufferUtils.createIntBuffer(16);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
		
		prevBuffer = RenderEngine.instance().getCurrentFramebuffer();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		RenderEngine.instance().bindFramebuffer(this);
		
		GL11.glClearColor(color.red, color.green, color.blue, color.alpha);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		GL11.glViewport(0, 0, (int)size, (int)size);
		RenderEngine.instance().ortho(0, size, 0, size, -1, 1);
	}
	
	public void pop() {
		
		RenderEngine.instance().bindFramebuffer(prevBuffer);
		
		GL11.glViewport(viewport.get(0), viewport.get(1), viewport.get(2), viewport.get(3));
		RenderEngine.instance().setProjection(proj);
	}
	
	public Texture getTexture() {
		
		return texture;
	}
}
