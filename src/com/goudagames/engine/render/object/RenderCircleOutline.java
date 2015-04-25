package com.goudagames.engine.render.object;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.goudagames.engine.color.Color;
import com.goudagames.engine.render.Program;
import com.goudagames.engine.system.GLSystem;
import com.goudagames.engine.util.MathUtil;
import com.goudagames.engine.util.Vertex;

public class RenderCircleOutline extends RenderBase {

	Vertex[] circle;
	public float radius;
	public boolean view = true;
	
	public RenderCircleOutline(int segments) {
		
		circle = new Vertex[segments];
		
		float theta = 2f * (float)Math.PI / (float)segments;
		float tan_factor = MathUtil.tanf(theta);
		float rad_factor = MathUtil.cosf(theta);
		
		float x = 0.5f;
		float y = 0;
		
		for (int i = 0; i < circle.length; i++) {
			
			circle[i] = new Vertex().setXY(x, y).setColor(new Color()).setUV(x + 0.5f, y + 0.5f);
			
			float tx = -y;
			float ty = x;
			
			x += tx * tan_factor;
			y += ty * tan_factor;
			
			x *= rad_factor;
			y *= rad_factor;
		}
	}
	
	public RenderCircleOutline() {
		
		this(360);
	}
	
	@Override
	public void render() {
		
		Program.BASIC.use();
		
		for (Vertex v : circle) {
			
			v.setColor(color);
		}
		
		position = new Vector2f(Math.round(position.x), Math.round(position.y));
		
		FloatBuffer vertices = BufferUtils.createFloatBuffer(circle.length * Vertex.elementCount);
		for (int i = 0; i < circle.length; i++) {
			
			vertices.put(circle[i].getElements());
		}
		vertices.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, GLSystem.instance().getVBO());
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		GL20.glVertexAttribPointer(2, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
		
		translate(position);
		scale(new Vector2f(radius * 2f, radius * 2f));
		
		GLSystem.instance().setProjectionUniform(Program.BASIC.getUniformLocation("projection"));
		setModelUniform(Program.BASIC.getUniformLocation("model"));
		
		if (view) {
			
			GLSystem.instance().setCameraUniform(Program.TEXTURE.getUniformLocation("view"));
		}
		else {
			
			FloatBuffer b = BufferUtils.createFloatBuffer(16);
			new Matrix4f().store(b); b.flip();
			GL20.glUniformMatrix4(Program.TEXTURE.getUniformLocation("view"), false, b);
		}
		
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, circle.length);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		setModelMatrix(new Matrix4f());
	}
}
