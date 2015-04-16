package com.goudagames.engine.render;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import com.goudagames.engine.shader.ShaderLoader;
import com.goudagames.engine.util.Vertex;

public class RenderEngine {

	private static RenderEngine instance;
	static boolean initiated = false;
	
	private int vaoID = 0;
	private int vboID = 0;
	
	private Matrix4f projection, defaultProj;
	
	private Camera camera;
	
	int bmLoc, bpLoc, tex0Loc, tex1Loc;
	
	private FrameBufferObject curFBO = null;
	
	public static RenderEngine instance() {
		return instance;
	}
	
	/**
	 * Initiates the render engine. Should not be called, use Engine.start() instead.
	 */
	public static void init() {
		
		if (!initiated) {
			
			instance = new RenderEngine();
			initiated = true;
		}
	}
	
	/**
	 * Returns the currently bound framebuffer.
	 * @returns the current fbo
	 */
	public FrameBufferObject getCurrentFramebuffer() {
		
		return curFBO;
	}
	
	/**
	 * Binds the given framebuffer object.
	 * @param fbo
	 */
	public void bindFramebuffer(FrameBufferObject fbo) {
		
		if (fbo == null) {
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		}
		else {
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo.id);
		}
		curFBO = fbo;
	}
	
	public int getVBO() {
		
		return vboID;
	}
	
	//public void renderGrid(float x, float y, float)
	
	//TODO Add Render classes for methods below.
	/**
	 * Renders a fan with the given texture using the given vertices, 
	 * where the center vertex is the middle point.
	 * @param vertices
	 * @param texture
	 */
	/*public void renderTexturedFan(Vertex[] vertices, String texture) {
		
		if (vertices == null) return;
		
		if (currentProgram != texProgramID) {
			
			GL20.glUseProgram(texProgramID);
			currentProgram = texProgramID;
		}
		
		Texture tex = TextureLibrary.get(texture);
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
		for (int i = 0; i < vertices.length; i++) {
			
			vertexBuffer.put(vertices[i].getElements());
		}
		vertexBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		GL20.glVertexAttribPointer(2, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
		
		projection.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(projectionLocation, false, matrix44Buffer);
		model.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(modelLocation, false, matrix44Buffer);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());
		
		GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, vertices.length);
		
		model = new Matrix4f();
	}
	
	/**
	 * Renders a fan with the given color using the given vertices, 
	 * where the center vertex is the middle point.
	 * @param points
	 * @param color
	 */
	/*public void renderFan(Vector2f[] points, Color color) {
		
		if (currentProgram != programID) {
			
			GL20.glUseProgram(programID);
			currentProgram = programID;
		}
		
		Vertex[] vertices = new Vertex[points.length];
		for (int i = 0; i < points.length; i++) {
			Vector2f point = points[i];
			
			Vertex vertex = new Vertex();
			vertex.setXY(point.x, point.y);
			vertex.setColor(color);
			vertices[i] = vertex;
		}
		
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
		for (int i = 0; i < vertices.length; i++) {
			vertexBuffer.put(vertices[i].getElements());
		}
		vertexBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		
		projection.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(projectionLocation, false, matrix44Buffer);
		model.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(modelLocation, false, matrix44Buffer);

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, vertices.length);
		
		model = new Matrix4f();
	}
	
	public void renderLine(Vector2f v1, Vector2f v2) {
		renderLine(v1, v2, new Color());
	}
	
	public void renderLine(Vector2f v1, Vector2f v2, Color color) {
		
		if (currentProgram != programID) {
			
			GL20.glUseProgram(programID);
			currentProgram = programID;
		}
		
		v1.x = Math.round(v1.x);
		v1.y = Math.round(v1.y);
		v2.x = Math.round(v2.x);
		v2.y = Math.round(v2.y);
		
		Vertex vert1 = new Vertex();
		Vertex vert2 = new Vertex();
		vert1.setXY(v1.x, v1.y);
		vert2.setXY(v2.x, v2.y);
		vert1.setColor(color);
		vert2.setColor(color);
		
		Vertex[] vertices = new Vertex[] {vert1, vert2};
		FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
		for (int i = 0; i < vertices.length; i++) {
			vertexBuffer.put(vertices[i].getElements());
		}
		vertexBuffer.flip();
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		
		projection.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(projectionLocation, false, matrix44Buffer);
		model.store(matrix44Buffer); matrix44Buffer.flip();
		GL20.glUniformMatrix4(modelLocation, false, matrix44Buffer);

		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glDrawArrays(GL11.GL_LINES, 0, 2);
		
		model = new Matrix4f();
	}
	*/
	
	public Camera getCamera() {
		
		return camera;
	}
	
	public void setCamera(Camera camera) {
		
		this.camera = camera;
	}
	
	/**
	 * Returns the current projection matrix.
	 * @returns projection
	 */
	public Matrix4f getProjection() {
		
		return projection;
	}
	
	/**
	 * Sets the projection matrix to the given matrix.
	 * @param projection
	 */
	public void setProjection(Matrix4f p) {
		
		projection = p;
	}
	
	/**
	 * Destroys the resources used by the render engine.
	 * Should only be called by the Engine class.
	 */
	public void destroy() {
		
		GL20.glUseProgram(0);
		
		GL30.glBindVertexArray(vaoID);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL15.glDeleteBuffers(vboID);
		
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoID);
		
		instance = null;
	}
	
	/**
	 * Sets the default projection matrix to
	 * the current projection matrix.
	 */
	public void setDefaultProjection() {

		defaultProj = projection;
	}
	
	/**
	 * Sets the projection matrix to the default
	 * projection matrix.
	 */
	public void applyDefaultProjection() {
		
		projection = defaultProj;
	}
	
	/**
	 * Sets the current projection matrix to an orthographic matrix
	 * using the given values.
	 * @param left
	 * @param right
	 * @param bottom
	 * @param top
	 * @param near
	 * @param far
	 */
	public void ortho(float left, float right, float bottom, float top, float near, float far) {
		
		projection = new Matrix4f();
		
		projection.m00 = 2f / (right - left);
		projection.m11 = 2f / (top - bottom);
		projection.m22 = 2f / (far - near);
		projection.m30 = -(right - left) / (right - left);
		projection.m31 = -(top - bottom) / (top - bottom);
		projection.m32 = -(far - near) / (far - near);
		projection.m33 = 1f;
	}
	
	private RenderEngine() {
		
		ortho(0, Display.getWidth(), 0, Display.getHeight(), -1, 1);
		setDefaultProjection();
		
		camera = new Camera(0f, 0f);
		
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		instance = this;
		
		vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		
		vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		GL20.glVertexAttribPointer(0, Vertex.positionElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.positionByteOffset);
		GL20.glVertexAttribPointer(1, Vertex.colorElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.colorByteOffset);
		GL20.glVertexAttribPointer(2, Vertex.textureElementCount, GL11.GL_FLOAT, false, Vertex.stride, Vertex.textureByteOffset);
		
		GL30.glBindVertexArray(0);
		
		ShaderLoader.load("com/goudagames/engine/shader/tex_vert.glsl", GL20.GL_VERTEX_SHADER);
		ShaderLoader.load("com/goudagames/engine/shader/tex_frag.glsl", GL20.GL_FRAGMENT_SHADER);
		
		ShaderLoader.load("com/goudagames/engine/shader/vert.glsl", GL20.GL_VERTEX_SHADER);
		ShaderLoader.load("com/goudagames/engine/shader/frag.glsl", GL20.GL_FRAGMENT_SHADER);
		
		ShaderLoader.load("com/goudagames/engine/shader/blend_vert.glsl", GL20.GL_VERTEX_SHADER);
		
		ShaderLoader.load("com/goudagames/engine/shader/mult_frag.glsl", GL20.GL_FRAGMENT_SHADER);
		ShaderLoader.load("com/goudagames/engine/shader/add_frag.glsl", GL20.GL_FRAGMENT_SHADER);
		
		Program.TEXTURE = new Program("tex_vert", "tex_frag");
		
		Program.TEXTURE.bindAttribLocation(0, "in_Position");
		Program.TEXTURE.bindAttribLocation(1, "in_Color");
		Program.TEXTURE.bindAttribLocation(2, "in_TexCoord");
		
		Program.TEXTURE.link();
		
		Program.BASIC = new Program("vert", "frag");
		
		Program.BASIC.bindAttribLocation(0, "in_Position");
		Program.BASIC.bindAttribLocation(1, "in_Color");
		
		Program.BASIC.link();
		
		Program.MULTIPLY = new Program("blend_vert", "mult_frag");
		
		Program.MULTIPLY.bindAttribLocation(0, "in_Position");
		Program.MULTIPLY.bindAttribLocation(1, "in_Color");
		Program.MULTIPLY.bindAttribLocation(2, "in_TexCoord0");
		Program.MULTIPLY.bindAttribLocation(3, "in_TexCoord1");
		
		Program.MULTIPLY.link();
		
		Program.ADD = new Program("blend_vert", "add_frag");
		
		Program.ADD.bindAttribLocation(0, "in_Position");
		Program.ADD.bindAttribLocation(1, "in_Color");
		Program.ADD.bindAttribLocation(2, "in_TexCoord");
		Program.MULTIPLY.bindAttribLocation(2, "in_TexCoord0");
		Program.MULTIPLY.bindAttribLocation(3, "in_TexCoord1");
		
		Program.ADD.link();
		
		Program.MULTIPLY.storeUniformLocation("texture0_diffuse");
		Program.MULTIPLY.storeUniformLocation("texture1_diffuse");
		Program.MULTIPLY.storeUniformLocation("ignoreAlpha");
		Program.ADD.storeUniformLocation("texture0_diffuse");
		Program.ADD.storeUniformLocation("texture1_diffuse");
		Program.ADD.storeUniformLocation("ignoreAlpha");
		
		GL30.glBindVertexArray(vaoID);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		Program.TEXTURE.storeUniformLocation("model");
		Program.TEXTURE.storeUniformLocation("projection");
		Program.TEXTURE.storeUniformLocation("view");
		Program.BASIC.storeUniformLocation("model");
		Program.BASIC.storeUniformLocation("projection");
		Program.BASIC.storeUniformLocation("view");
		Program.MULTIPLY.storeUniformLocation("model");
		Program.MULTIPLY.storeUniformLocation("projection");
		Program.MULTIPLY.storeUniformLocation("view");
		Program.ADD.storeUniformLocation("model");
		Program.ADD.storeUniformLocation("projection");
		Program.ADD.storeUniformLocation("view");
		
		GL11.glClearColor(0f, 0f, 0f, 1f);
	}

	public void setProjectionUniform(int loc) {
		
		FloatBuffer b = BufferUtils.createFloatBuffer(16);
		projection.store(b); b.flip();
		GL20.glUniformMatrix4(loc, false, b);
	}
	
	public void setCameraUniform(int loc) {
	
		FloatBuffer b = BufferUtils.createFloatBuffer(16);
		camera.getMatrix().store(b); b.flip();
		GL20.glUniformMatrix4(loc, false, b);
	}
}
