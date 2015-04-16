package com.goudagames.engine.render;

import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.assets.TextureLibrary;
import com.goudagames.engine.color.Color;


public class FontRenderer {

	private static Color color = new Color();
	private static float pt = 8f;
	private static float padding = 0f;
	private static float iconHeight = 8f;
	private static float iconWidth = 8f;
	
	public static void setColor(Color c) {
		
		color = c;
	}
	
	public static void setSize(float size) {
		
		pt = size;
	}
	
	public static void setPadding(float p) {
		
		padding = p;
	}
	
	public static float getStringHeight() {
		
		return getStringHeight(pt);
	}
	
	public static float getStringHeight(float size) {
		
		float scale = size / iconHeight;
		return scale * iconHeight;
	}
	
	public static float getStringWidth(String text) {
		
		return getStringWidth(text, pt);
	}
	
	public static float getStringWidth(String text, float size) {
		
		float scale = size / iconHeight;
		return scale * iconWidth * text.length() - padding * (text.length() - 1);
	}
	
	public static void renderString(float x, float y, String text) {
		
		float scale = pt / 8f;
		float width = iconWidth;
		float height = iconHeight;
		
		for (int i = 0; i < text.length(); i++) {
			
			int c = text.codePointAt(i);
			
			if (c >= 0 && c < 128) {
				
				float u = (float) ((c % 16f) * width);
				float v = (float) (Math.floor(c / 16f) * height) - 0.5f;
				float mod = padding * i;
				
				RenderTexturedQuad quad = new RenderTexturedQuad(TextureLibrary.get("font"));
				
				quad.translate(new Vector2f(x + width * scale / 2f + i * width * scale + mod, y + height * scale / 2f));
				quad.size = new Vector2f(width * scale, height * scale);
				quad.color = color;
				quad.setUV(new Vector2f(u, v), new Vector2f(width, height), true);
				
				quad.render();
			}
		}
	}
}
