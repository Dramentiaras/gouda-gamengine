package com.goudagames.engine.assets;

public class Texture {

	private int id, width, height;
	
	public Texture(int id, int width, int height) {
		
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public int getTextureID() {
		
		return id;
	}
	
	public int getTextureWidth() {
		
		return width;
	}
	
	public int getTextureHeight() {
		
		return height;
	}
}
