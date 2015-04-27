package com.goudagames.engine.render.object;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.geometry.Rectangle;
import com.goudagames.engine.render.Program;
import com.goudagames.engine.util.Vertex;

public class RenderQuadOutline extends RenderObject {

	Vertex[] quad;
	public Vector2f size = new Vector2f(1f, 1f);
	
	boolean setArray = false;
	
	public RenderQuadOutline() {
		
		super();
		
		program = Program.BASIC;
		
		quad = new Vertex[4];
		
		quad[0] = new Vertex().setXY(-0.5f, 0.5f).setColor(new Color());
		quad[1] = new Vertex().setXY(-0.5f, -0.5f).setColor(new Color());
		quad[2] = new Vertex().setXY(0.5f, -0.5f).setColor(new Color());
		quad[3] = new Vertex().setXY(0.5f, 0.5f).setColor(new Color());
	}
	
	public RenderQuadOutline(Vertex[] quad) {
		
		super();
		
		program = Program.BASIC;
		
		this.quad = quad;
		setArray = true;
	}
	
	@Override
	public void render() {
		
		super.render();
		
		if (!setArray) {
			quad[0].setColor(color); quad[1].setColor(color); quad[2].setColor(color); quad[3].setColor(color);
		}
		
		applyTransformations();
		scale(size);
		
		setup(quad);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, 4);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		setModelMatrix(new Matrix4f());
	}
	
	public Rectangle getBounds() {
		
		return new Rectangle(position.x, position.y, size.x * (size.x < 0 ? -1:1), size.y * (size.y < 0 ? -1:1));
	}
}
