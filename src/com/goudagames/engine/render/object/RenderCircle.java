package com.goudagames.engine.render.object;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.render.Program;
import com.goudagames.engine.util.MathUtil;
import com.goudagames.engine.util.Vertex;

public class RenderCircle extends RenderObject {

	Vertex[] circle;
	public float radius;
	
	public RenderCircle(int segments) {
		
		program = Program.BASIC;
		
		circle = new Vertex[segments + 2];
		circle[0] = new Vertex().setXY(0f, 0f).setColor(new Color()).setUV(0.5f, 0.5f);
		
		float theta = 2f * (float)Math.PI / (float)segments;
		float tan_factor = MathUtil.tanf(theta);
		float rad_factor = MathUtil.cosf(theta);
		
		float x = 0.5f;
		float y = 0;
		
		for (int i = 1; i < circle.length - 1; i++) {
			
			circle[i] = new Vertex().setXY(x, y).setColor(new Color()).setUV(x + 0.5f, y + 0.5f);
			
			float tx = -y;
			float ty = x;
			
			x += tx * tan_factor;
			y += ty * tan_factor;
			
			x *= rad_factor;
			y *= rad_factor;
		}
		
		circle[segments + 1] = circle[1];
	}
	
	public RenderCircle() {
		
		this(360);
	}
	
	@Override
	public void render() {
		
		super.render();
		
		for (Vertex v : circle) {
			
			v.setColor(color);
		}
		
		translate(getAbsolutePosition());
		scale(new Vector2f(radius * 2f, radius * 2f));
		
		setup(circle);
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, circle.length);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		setModelMatrix(new Matrix4f());
	}
}
