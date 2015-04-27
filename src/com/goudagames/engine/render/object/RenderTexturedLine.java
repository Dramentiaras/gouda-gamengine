package com.goudagames.engine.render.object;

import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.assets.Texture;
import com.goudagames.engine.color.Color;
import com.goudagames.engine.geometry.Line;
import com.goudagames.engine.render.Program;
import com.goudagames.engine.util.Vertex;

public class RenderTexturedLine extends RenderObject {

	public Vector2f v0, v1;
	public Color color1;
	public float width = 1f;
	
	Texture texture;
	
	public RenderTexturedLine(Vector2f v0, Vector2f v1, Texture texture) {
		
		super();
		
		program = Program.TEXTURE;
		
		this.texture = texture;
		
		color1 = new Color();
		this.v0 = v0;
		this.v1 = v1;
	}
	
	public RenderTexturedLine(Line line, Texture texture) {
		
		super();
		
		program = Program.TEXTURE;
		
		this.texture = texture;
		
		color1 = new Color();
		this.v0 = line.v0;
		this.v1 = line.v1;
	}
	
	public Texture getTexture() {
		
		return texture;
	}
	
	public void setTexture(Texture texture) {
		
		this.texture = texture;
	}
	
	@Override
	public void render() {
		
		super.render();
		
		Vector2f line = new Vector2f();
		
		Vector2f.sub(v0, v1, line);
		Vector2f normal = (Vector2f) new Vector2f(-line.y, line.x).normalise();
		normal.scale(width / 2f);
		
		Vector2f a = new Vector2f();
		Vector2f.sub(v0, normal, a);
		Vertex vert0 = new Vertex().setXY(a.x, a.y).setColor(color).setUV(0f, 1f);
		
		Vector2f b = new Vector2f();
		Vector2f.add(v0, normal, b);
		Vertex vert1 = new Vertex().setXY(b.x, b.y).setColor(color).setUV(0f, 0f);
		
		Vector2f c = new Vector2f();
		Vector2f.sub(v1, normal, c);
		Vertex vert2 = new Vertex().setXY(c.x, c.y).setColor(color1).setUV(1f, 1f);
		
		Vector2f d = new Vector2f();
		Vector2f.add(v1, normal, d);
		Vertex vert3 = new Vertex().setXY(d.x, d.y).setColor(color1).setUV(1f, 0f);
		
		RenderTexturedQuad quad = new RenderTexturedQuad(texture, new Vertex[] {vert0, vert2, vert3, vert1});
		quad.view = view;
		
		quad.render();
	}

}
