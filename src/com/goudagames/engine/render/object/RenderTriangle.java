package com.goudagames.engine.render.object;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.render.Program;
import com.goudagames.engine.util.Vertex;

public class RenderTriangle extends RenderObject {

	Vertex[] tri;
	public Vector2f size = new Vector2f(1f, 1f);
	
	private boolean setArray = false;
	
	public RenderTriangle() {
		
		super();
		
		program = Program.BASIC;
		
		tri = new Vertex[3];
		
		tri[0] = new Vertex().setXY(-0.5f, -0.5f).setColor(new Color());
		tri[1] = new Vertex().setXY(0f, 0.5f).setColor(new Color());
		tri[2] = new Vertex().setXY(0.5f, -0.5f).setColor(new Color());
	}

	public RenderTriangle(Vertex[] tri) {
		
		super();
		
		program = Program.BASIC;
		
		this.tri = tri;
		setArray = true;
	}
	
	@Override
	public void render() {
		
		super.render();
		
		if (!setArray) {
			tri[0].setColor(color); tri[1].setColor(color); tri[2].setColor(color);;
		}
		
		applyTransformations();
		scale(size);
		
		setup(tri);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		setModelMatrix(new Matrix4f());
	}
}
