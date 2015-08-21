package com.goudagames.engine.util;

public class MathUtil {

	public static final float PI = (float)Math.PI;
	
	public static float roundf(float value) {
		
		float rest = value % 1f;
		
		if (rest >= 0.5f) {
			
			value += 1f - rest;
		}
		else {
			
			value -= rest;
		}
		
		return value;
	}
	
	/**
	 * Returns wether or not the value is a power of two.
	 * @param value
	 * @returns boolean
	 */
	public static boolean isPowerOfTwo(int value) {
		
		return (value != 0) && ((value & (value - 1)) == 0);
	}
	
	/**
	 * Ceils the given value to the closest number that's is a power
	 * of two.
	 * @param value
	 * @returns closest number that's a power of two
	 */
	public static int ceilToPowerOfTwo(float value) {
		int size = 2;
		
		while (size < value) {
			size *= 2;
		}
		
		return size;
	}

	/**
	 * Rounds the given number down to an int.
	 * @param float
	 * @returns the floored value
	 */
	public static int floor(float f) {
			
		return (int)(f - f % 1f);
	}
	
	/**
	 * Rounds the given number up to an int.
	 * @param float
	 * @returns the ceiled value
	 */
	public static int ceil(float f) {
		
		return (int)(f + (1f - f % 1f));
	}

	/**
	 * Rounds the given value so that it's bigger or 
	 * equal to min and smaller or equal to max.
	 * @param float
	 * @param min
	 * @param max
	 * @returns the clamped value as a float
	 */
	public static float clamp(float f, float min, float max) {
		
		float r = f;
		
		if (r < min) r = min;
		if (r > max) r = max;
		
		return r;
	}
	
	/**
	 * Rounds the given value so that it's bigger or 
	 * equal to min and smaller or equal to max.
	 * @param int
	 * @param min
	 * @param max
	 * @returns the clamped value as an int
	 */
	public static int clamp(int i, int min, int max) {
		
		int r = i;
		
		if (r < min) r = min;
		if (r > max) r = max;
		
		return r;
	}
	
	/**
	 * Uses Math.tan, but returns the result as a float.
	 * @param angle
	 * @returns the tangent of angle 
	 */
	public static float tanf(float f) {
		
		return (float)Math.tan(f);
	}
	
	/**
	 * Uses Math.atan, but returns the result as a float.
	 * @param the tangent of an angle
	 * @returns the angle
	 */
	public static float atanf(float f) {
		
		return (float)Math.atan(f);
	}
	
	/**
	 * Uses Math.atan2, but returns the result as a float.
	 * @params y, x
	 * @returns the angle
	 */
	public static float atan2f(float y, float x) {
		
		return (float)Math.atan2(y, x);
	}
	
	/**
	 * Uses Math.sin, but returns the result as a float.
	 * @param angle
	 * @returns the sinus of angle 
	 */
	public static float sinf(float f) {
		
		return (float)Math.sin(f);
	}
	
	/**
	 * Uses Math.cos, but returns the result as a float.
	 * @param angle
	 * @returns the cosinus of angle 
	 */
	public static float cosf(float f) {
		
		return (float)Math.cos(f);
	}
}
