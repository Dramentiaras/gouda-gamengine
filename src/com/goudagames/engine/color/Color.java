package com.goudagames.engine.color;

import com.goudagames.engine.util.MathUtil;

public class Color {

	public float red, green, blue, alpha;
	
	public Color() {
		
		red = green = blue = alpha = 1f;
	}
	
	public Color(float r, float g, float b, float a) {
		
		this.red = r;
		this.green = g;
		this.blue = b;
		this.alpha = a;
		
		clamp();
	}
	
	@Override
	public String toString() {
		
		return "{" + red  + ", " + green + ", " + blue + ", " + alpha + "}";
	}
	
	public Color(String hex) {
		
		if (hex.startsWith("#")) {
			
			hex = hex.substring(1);
		}
		
		switch (hex.length()) {
			
			case 6: {
				this.red = Integer.parseInt(hex.substring(0, 2), 16) / 255f;
				this.green = Integer.parseInt(hex.substring(2, 4), 16) / 255f;
				this.blue = Integer.parseInt(hex.substring(4, 6), 16) / 255f;
				this.alpha = 1f;
				break;
			}
			case 3: {
				this.red = new Integer(hex.charAt(0)) / 255f;
				this.green = new Integer(hex.charAt(1)) / 255f;
				this.blue = new Integer(hex.charAt(2)) / 255f;
				this.alpha = 1f;
				break;
			}
			case 8: {
				this.alpha = Integer.parseInt(hex.substring(0, 2), 16) / 255f;
				this.red = Integer.parseInt(hex.substring(2, 4), 16) / 255f;
				this.green = Integer.parseInt(hex.substring(4, 6), 16) / 255f;
				this.blue = Integer.parseInt(hex.substring(6, 8), 16) / 255f;
				break;
			}
			default: {
				System.err.println("Invalid hex color format: " + hex + ". Valid formats are #RGB, #RRGGBB and #AARRGGBB.");
				red = green = blue = alpha = 1f;
			}
		}
	}
	
	public void clamp() {
		
		red = MathUtil.clamp(red, 0f, 1f);
		green = MathUtil.clamp(green, 0f, 1f);
		blue = MathUtil.clamp(blue, 0f, 1f);
		alpha = MathUtil.clamp(alpha, 0f, 1f);
	}
	
	public Color add(Color c) {
		
		Color result = new Color();
		result.red = c.red + red;
		result.green = c.green + green;
		result.blue = c.blue + blue;
		result.alpha = c.alpha + alpha;
		result.clamp();
		
		return result;
	}
	
	public Color multiply(Color c) {
		
		Color result = new Color();
		
		result.red = c.red * red;
		result.green = c.green * green;
		result.blue = c.blue * blue;
		result.alpha = c.alpha * alpha;
		result.clamp();
		
		return result;
	}
}
