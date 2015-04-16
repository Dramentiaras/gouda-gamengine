package com.goudagames.engine.util;

public class Time {

	private static long prevTime, time;
	private static float delta;
	
	public static long getTime() {
		
		return System.currentTimeMillis();
	}
	
	public static void init() {
		
		prevTime = time = getTime();
	}
	
	public static void update() {
		
		prevTime = time;
		time = getTime();
		delta = (time - prevTime) / 1000f;
	}
	
	public static float delta() {
		
		return delta;
	}
}
