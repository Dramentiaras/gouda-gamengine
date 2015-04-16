package com.goudagames.engine.shader;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;

public class ShaderProgram {

	public static ShaderProgram currentProgram = null;
	
	private HashMap<String, Integer> uniformLocs = new HashMap<String, Integer>();
	public int id, vs, fs;
	private boolean linked = false;
	
	public ShaderProgram(String vsSource, String fsSource) {
		
		//this(ShaderLoader.loadShader(vsSource, GL20.GL_VERTEX_SHADER), ShaderLoader.loadShader(fsSource, GL20.GL_VERTEX_SHADER));
	}
	
	public boolean isLinked() {
		
		return linked;
	}
	
	public ShaderProgram(int vs, int fs) {
	
		this.vs = vs;
		this.fs = fs;
		
		id = GL20.glCreateProgram();
		GL20.glAttachShader(id, this.vs);
		GL20.glAttachShader(id, this.fs);
	}
	
	public void bindAttribLocation(int i, String name) {
		
		GL20.glBindAttribLocation(id, i, name);
	}
	
	public void use() {
		
		GL20.glUseProgram(id);
		currentProgram = this;
	}
	
	public void link() {
		
		GL20.glLinkProgram(id);
		GL20.glValidateProgram(id);
		
		int error = GL20.glGetProgrami(id, GL20.GL_LINK_STATUS);
		if (error == GL11.GL_FALSE) {
			linked = false;
			System.err.println("Error linking program!");
			System.err.println(GL20.glGetProgramInfoLog(id, GL20.GL_INFO_LOG_LENGTH));
			return;
		}
		
		linked = true;
	}
	
	public void delete() {
		
		GL20.glDeleteProgram(id);
	}
	
	public int getUniformLocation(String uniform) {
		
		return uniformLocs.get(uniform);
	}
	
	public void findUniformLocation(String uniform) {
		
		if (!linked) return;
		
		int loc = GL20.glGetUniformLocation(id, uniform);
		
		int error = GL11.glGetError();
		if (error != GL11.GL_NO_ERROR) {
			
			System.err.println("Error getting uniform location " + uniform);
			System.err.println(GLU.gluErrorString(error));
			return;
		}
		
		System.out.println("Found uniform location for " + uniform + " at " + loc);
		uniformLocs.put(uniform, loc);
	}
}
