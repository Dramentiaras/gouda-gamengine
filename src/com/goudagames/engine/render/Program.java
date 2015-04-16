package com.goudagames.engine.render;

import java.util.HashMap;

import org.lwjgl.opengl.GL20;

import com.goudagames.engine.shader.ShaderLoader;

public class Program {

	public static Program BASIC, TEXTURE, MULTIPLY, ADD;
	
	HashMap<String, Integer> uniforms = new HashMap<String, Integer>();
	
	static Program currentProgram = null;
	int id;
	boolean linked = false;
	
	public Program(String vs, String fs) {
		
		id = GL20.glCreateProgram();
		
		GL20.glAttachShader(id, ShaderLoader.get(vs));
		GL20.glAttachShader(id, ShaderLoader.get(fs));
	}
	
	public void bindAttribLocation(int index, String name) {
		
		GL20.glBindAttribLocation(id, index, name);
	}
	
	public void storeUniformLocation(String name) {
		
		int u = GL20.glGetUniformLocation(id, name);
		uniforms.put(name, u);
	}
	
	public int getUniformLocation(String name) {
		
		return uniforms.get(name);
	}
	
	public void link() {
		
		GL20.glLinkProgram(id);
		GL20.glValidateProgram(id);
		
		linked = true;
	}
	
	public void use() {
		
		if (!linked) return;
		
		if (currentProgram != this) {
			
			GL20.glUseProgram(id);
			currentProgram = this;
		}
	}
}
