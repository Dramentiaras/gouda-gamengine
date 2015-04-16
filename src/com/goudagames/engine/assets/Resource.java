package com.goudagames.engine.assets;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

public class Resource {

	public static final int TEXTURE = 0;
	
	public static final int NAME = 0, MAG_FILTER = 1, MIN_FILTER = 2;

	private HashMap<Integer, Object> parameters = new HashMap<Integer, Object>();
	
	private int type;
	private String path;
	private boolean loaded = false;
	
	public Resource(String path, int type) {
		
		this.path = path;
		this.type = type;
		
		int par0 = path.lastIndexOf("/");
		
		String name = par0 == -1 ? path:(path.substring(par0 + 1));
		name = name.substring(0, name.lastIndexOf("."));
		
		set(NAME, name);
		set(MAG_FILTER, GL11.GL_NEAREST);
		set(MIN_FILTER, GL11.GL_NEAREST);
	}
	
	public Resource set(int key, Object value) {
		
		parameters.put(key, value);
		return this;
	}
	
	public Object get(int key) {
		
		return parameters.get(key);
	}
	
	public void load() {
		
		switch(type) {
		
			case TEXTURE: {
				TextureLibrary.load(path, (String)get(NAME), 
						(int)get(MAG_FILTER), (int)get(MIN_FILTER));
				loaded = true;
				break;
			}
		}
	}
	
	public boolean isLoaded() {
		
		return loaded;
	}
}
