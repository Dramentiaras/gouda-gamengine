package com.goudagames.engine.start;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

import com.goudagames.engine.assets.AssetLoader;
import com.goudagames.engine.handler.gui.GuiHandler;
import com.goudagames.engine.logging.Log;
import com.goudagames.engine.loop.GameLoop;
import com.goudagames.engine.loop.Loop;
import com.goudagames.engine.state.GameState;
import com.goudagames.engine.system.ALSystem;
import com.goudagames.engine.system.GLSystem;
import com.goudagames.engine.util.ExceptionUtil;
import com.goudagames.engine.util.Time;

public class Engine {

	private static GameState state;
	private static String curState = "";
	private static Loop loop;
	private static float r, g, b, a;
	
	private static ArrayList<AssetLoader> assetLoaders = new ArrayList<AssetLoader>();
	
	private static HashMap<String, GameState> states = new HashMap<String, GameState>();
	private static boolean fixedDelta = true;
	
	public static void useFixedDelta(boolean fixed) {
		
		fixedDelta = fixed;
	}
	
	public static void loadState(String name, GameState state) {
		
		states.put(name, state);
	}
	
	public static void addAssetLoader(AssetLoader loader) {
		
		assetLoaders.add(loader);
	}
	
	public static void stop() {
		
		loop.stop();
	}
	
	public static void start(String s, int width, int height) {
		
		start(s, s, width, height);
	}
	
	public static void start(String s, String title, int width, int height) {
		
		start(s, title, new GameLoop(), width, height);
	}
	
	public static void start(String s, String title, Loop l, int width, int height) {
		
		Log.init();
		
		Log.log("Starting engine...");
		Log.log("System.getProperty(\"os.name\") == " + System.getProperty("os.name"));
		Log.log("System.getProperty(\"os.version\") == " + System.getProperty("os.version"));
		Log.log("System.getProperty(\"os.arch\") == " + System.getProperty("os.arch"));
		Log.log("System.getProperty(\"java.version\") == " + System.getProperty("java.version"));
		Log.log("System.getProperty(\"java.vendor\") == " + System.getProperty("java.vendor"));
		
		loop = l;
		
		try {
			
			PixelFormat format = new PixelFormat();
			ContextAttribs contextAttribs = new ContextAttribs(3, 2)
					.withForwardCompatible(true).withProfileCore(true);
			
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle(title);
			Display.setVSyncEnabled(true);
			Display.create(format, contextAttribs);
			Mouse.create();
			Keyboard.create();
			Controllers.create();
		
			GLSystem.init();
			GuiHandler.init();
			
			for (AssetLoader loader : assetLoaders) {
				
				loader.load(true);
			}
			
			ALSystem.initAL();
			
			setState(s);
			
			Time.init();
			loop.start();
		
		}catch (Exception ex) {
			
			Log.log(Level.SEVERE, ExceptionUtil.getStackTrace(ex));
			Log.saveLog();
		}
	}
	
	public static void update() {
		
		GuiHandler.INSTANCE.update();
		
		if (!GuiHandler.INSTANCE.isStatePaused()) {
			
			if (fixedDelta) {
				state.update(1f/loop.getFPS());
			}
			else {
				state.update(Time.delta());	
			}
		}
		
		renderState();
	}
	
	private static void renderState() {
		
		GL11.glClearColor(r, g, b, a);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		state.render();
		GuiHandler.INSTANCE.render();
	}
	
	public static void destroy() {
		
		if (state != null) {
			state.destroy();
		}
		GLSystem.instance().destroy();
		ALSystem.destroy();
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
		Controllers.destroy();
		
		Log.destroy();
	}
	
	public static void setState(String s) {
		
		if (state != null) {
			state.destroy();
		}
		state = states.get(s);
		curState = s;
		state.init();
	}
	
	public static GameState getState(String s) {
		
		return states.get(s);
	}
	
	public static String getCurrentStateName() {
		
		return curState;
	}
	
	public static void setClearColor(float red, float green, float blue, float alpha) {
		
		r = red;
		g = green;
		b = blue;
		a = alpha;
	}
}