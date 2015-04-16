package com.goudagames.engine.shader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Level;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.goudagames.engine.logging.Log;
import com.goudagames.engine.util.ExceptionUtil;

public class ShaderLoader {

	private static HashMap<String, Integer> shaders = new HashMap<String, Integer>();
	
	private static int loadShader(String path, int type) {
		
		StringBuilder src = new StringBuilder();
		int shaderID = 0;
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(ClassLoader.getSystemResourceAsStream(path)));
			String line;
			while ((line = reader.readLine()) != null) {
				src.append(line).append("\n");
			}
			reader.close();
		}
		catch (Exception ex) {
			
			Log.log(Level.SEVERE, "Error loading shader!");
			Log.log(Level.SEVERE, ExceptionUtil.getStackTrace(ex));
			System.exit(-1);
		}
		
		shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, src);
		GL20.glCompileShader(shaderID);
		
		int l = GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS);
		if (l == GL11.GL_FALSE) {
			Log.log(Level.SEVERE, "Error compiling shader!");
			Log.log(Level.SEVERE, GL20.glGetShaderInfoLog(shaderID, GL20.glGetShaderi(shaderID, GL20.GL_INFO_LOG_LENGTH)));
		}
		
		return shaderID;
	}
	
	public static int get(String shader) {
		
		return shaders.get(shader);
	}
	
	public static void load(String path, int type) {
		
		int par0 = path.lastIndexOf("/");
		
		String name = par0 == -1 ? path:(path.substring(par0 + 1));
		name = name.substring(0, name.lastIndexOf("."));

		load(name, path, type);
	}
	
	public static void load(String name, String path, int type) {
		
		int shader = loadShader(path, type);
		
		shaders.put(name, shader);
		Log.log(Level.FINE, "Loaded shader: " + path + " with name: " + name);
	}
}
