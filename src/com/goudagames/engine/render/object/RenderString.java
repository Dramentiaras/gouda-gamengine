package com.goudagames.engine.render.object;

import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.assets.TextureLibrary;

public class RenderString extends RenderBase {
	
	public float size, padding;
	private float iconHeight, iconWidth;
	public String text;
	
	private String texture;
	
	public boolean centered = false;
	public boolean view = true;
	
	public RenderString(String text, String texture) {
		
		this(text, texture, 8f, 8f);
	}
	
	public RenderString(String text, String texture, float iconWidth, float iconHeight) {
		
		this.text = text;
		this.texture = texture;
		this.iconWidth = iconWidth;
		this.iconHeight = iconHeight;
		size = iconHeight;
	}
	
	public float getStringHeight() {
		
		return getStringHeight(size);
	}
	
	public float getStringHeight(float size) {
		
		float scale = size / iconHeight;
		return scale * iconHeight;
	}
	
	public float getStringWidth() {
		
		return getStringWidth(size);
	}
	
	public float getStringWidth(float size) {
		
		float scale = size / iconHeight;
		return scale * iconHeight * text.length() - padding * (text.length() - 1);
	}

	@Override
	public void render() {
		
		float scale = size / iconWidth;
		float width = iconWidth;
		float height = iconHeight;
		
		Vector2f pos = new Vector2f(position.x, position.y);
		
		if (centered) {
			
			pos.x -= getStringWidth() / 2f;
			pos.y -= getStringHeight() / 2f;
		}
		
		for (int i = 0; i < text.length(); i++) {
			
			int c = text.codePointAt(i);
			
			if (c < 0 || c >= 128) c = 0;
			
			float u = (float) ((c % 16f) * width);
			float v = (float) (Math.floor(c / 16f) * height);
			float mod = padding * i;
			
			RenderTexturedQuad quad = new RenderTexturedQuad(TextureLibrary.get(texture));
			quad.view = view;
			
			quad.translate(new Vector2f(pos.x + width * scale / 2f + i * width * scale + mod, pos.y + height * scale / 2f));
			quad.size = new Vector2f(width * scale, height * scale);
			quad.color = color;
			quad.setUV(new Vector2f(u, v), new Vector2f(width, height), true);
			
			quad.render();
		}
	}
}
