package com.goudagames.engine.input;

import java.util.ArrayList;

import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.util.Time;

public class Input {

	private static ArrayList<IKeyListener> listeners = new ArrayList<IKeyListener>();
	private static ArrayList<IKeyListener> removals = new ArrayList<IKeyListener>();
	
	private static boolean[] keys = new boolean[256];
	private static boolean[] buttons = new boolean[3];
	private static Controller controller;
	public static boolean useController;
	private static boolean controllerEnabled = true;
	private static int ticks = 0;
	
	public static void update() {
		
		for (int i = 0; i < keys.length; i++) {
			
			keys[i] = Keyboard.isKeyDown(i);
		}
		
		for (int i = 0; i < buttons.length; i++) {
			
			buttons[i] = Mouse.isButtonDown(i);
		}
		
		if (Mouse.next()) {
			
			setUseController(false);
		}
		
		while (Keyboard.next()) {
			
			KeyEvent e = new KeyEvent(Keyboard.getEventKey(), Keyboard.getEventCharacter(), Keyboard.getEventKeyState(), Time.getTime());
			dispatchEvent(e);
			setUseController(false);
		}
		
		for (IKeyListener l : removals) {
			
			listeners.remove(l);
		}
		
		if (controllerEnabled) {
		
			Controllers.poll();
			
			while (Controllers.next()) {
				
				controller = Controllers.getEventSource();
				setUseController(true);
				controller.setXAxisDeadZone(.15f);
				controller.setYAxisDeadZone(.15f);
				controller.setRXAxisDeadZone(.15f);
				controller.setRYAxisDeadZone(.15f);
				
				Controllers.clearEvents();
			}
		}
	}
	
	public static void registerKeyListener(IKeyListener listener) {
		
		listeners.add(listener);
	}
	
	public static void removeKeyListener(IKeyListener listener) {
		
		removals.add(listener);
	}
	
	private static void dispatchEvent(KeyEvent event) {
		
		for (IKeyListener l : listeners) {
			
			l.onEvent(event);
		}
	}
	
	public static void setControllerEnabled(boolean enabled) {
		
		controllerEnabled = enabled;
	}
	
	private static void setUseController(boolean bool) {
		
		useController = bool;
		Mouse.setGrabbed(bool);
	}
	
	public static boolean keyPressed(int key) {
		
		return Keyboard.isKeyDown(key) && !keys[key];
	}
	
	public static boolean keyReleased(int key) {
		
		return !Keyboard.isKeyDown(key) && keys[key];
	}
	
	public static boolean keyDown(int key) {
		
		return Keyboard.isKeyDown(key);
	}
	
	public static boolean buttonPressed(int button) {

		return Mouse.isButtonDown(button) && !buttons[button];
	}
	
	public static boolean buttonReleased(int button) {
		
		return !Mouse.isButtonDown(button) && buttons[button];
	}
	
	public static boolean buttonDown(int button) {
		
		return Mouse.isButtonDown(button);
	}
	
	public static int scrollWheel() {
		
		return Mouse.getDWheel();
	}
	
	public static Vector2f getMousePosition() {
		
		return new Vector2f(Mouse.getX(), Mouse.getY());
	}
	
	public static float axisX() {
		
		if (controller != null) {
			
			return controller.getXAxisValue();
		}
		
		return 0f;
	}
	
	public static float axisY() {
		
		if (controller != null) {
			
			return -controller.getYAxisValue();
		}
		
		return 0f;
	}
	
	public static float axisZ() {
		
		if (controller != null) {
			
			return controller.getZAxisValue();
		}
		
		return 0f;
	}
	
	public static float axisRX() {
		
		if (controller != null) {
			
			return controller.getRXAxisValue();
		}
		
		return 0f;
	}
	
	public static float axisRY() {
		
		if (controller != null) {
			
			return -controller.getRYAxisValue();
		}
		
		return 0f;
	}
	
	public static float axisRZ() {
		
		if (controller != null) {
			
			return controller.getRZAxisValue();
		}
		
		return 0f;
	}
	
	public static float povX() {
		
		if (controller != null) {
			
			return controller.getPovX();
		}
		
		return 0f;
	}
	
	public static float povY() {
		
		if (controller != null) {
			
			return controller.getPovY();
		}
		
		return 0f;
	}
	
	public static boolean controllerPressed(int button) {
		
		if (controller != null && ticks > 8) {
			
			if (controller.isButtonPressed(button)) {
				
				ticks = 0;
				return true;
			}
			else {
				
				return false;
			}
		}
		
		return false;
	}
}
