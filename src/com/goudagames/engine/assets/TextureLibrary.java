package com.goudagames.engine.assets;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.logging.Level;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.goudagames.engine.logging.Log;
import com.goudagames.engine.util.ExceptionUtil;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureLibrary {

	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	
	public static void add(String name, Texture texture) {
		
		textures.put(name, texture);
		Log.log(Level.FINE, "Added texture with name: " + name);
	}
	
	public static void load(String path, String name) {
		
		load(path, name, GL11.GL_NEAREST, GL11.GL_NEAREST);
	}
	
	public static void load(String path, String name, int magFilter, int minFilter) {
		
		Texture texture = loadTexture(path, magFilter, minFilter);
		
		textures.put(name, texture);
		Log.log(Level.FINE, "Loaded texture with name: " + name);
	}
	
	public static void load(String path) {
		
		load(path, GL11.GL_NEAREST, GL11.GL_NEAREST);
	}
	
	public static void load(String path, int magFilter, int minFilter) {
		
		Texture texture = loadTexture(path, magFilter, minFilter);
		
		int par0 = path.lastIndexOf("/");
		
		String name = par0 == -1 ? path:(path.substring(par0 + 1));
		name = name.substring(0, name.lastIndexOf("."));
		
		textures.put(name, texture);
		Log.log(Level.FINE, "Loaded texture with name: " + name);
	}
	
	private static Texture loadTexture(String path, int magFilter, int minFilter) {
		
		ByteBuffer buf = null;
		int width = 0;
		int height = 0;
		Texture texture;
		
		try {
			
			InputStream in = ClassLoader.getSystemResourceAsStream(path);
			PNGDecoder decoder = new PNGDecoder(in);
			
			width = decoder.getWidth();
			height = decoder.getHeight();
			
			Format format = decoder.decideTextureFormat(Format.RGBA);
			
			buf = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buf, decoder.getWidth() * 4, format);
			buf.flip();
			
			in.close();
		}
		catch (Exception ex) {
			
			Log.log(Level.SEVERE, "Error loading texture: " + path);
			Log.log(Level.SEVERE, ExceptionUtil.getStackTrace(ex));
		}
		
		int id = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, magFilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, minFilter);
		
		texture = new Texture(id, width, height);
		
		return texture;
	}
	
	public static Texture get(String name) {
		
		Texture tex = textures.get(name);
		
		if (tex == null) {
			
			Log.log(Level.SEVERE, "Unable to find texture with name: " + name);
		}
		
		return tex;
	}
	
	public static void destroy() {
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		for (String s : textures.keySet()) {
			
			GL11.glDeleteTextures(textures.get(s).getTextureID());
		}
	}
}
