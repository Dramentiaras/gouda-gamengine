package com.goudagames.engine.render.object;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

public class RenderObjectContainer {
	
	public RenderObjectContainer parent;
	private ArrayList<RenderObject> children;
	
	public float rotation = 0f;
	public Vector2f offset = new Vector2f();
	private Vector2f position = new Vector2f();
	
	public Vector2f absPosition = new Vector2f();
	
	public boolean view = true;
	
	public RenderObjectContainer() {
		
		children = new ArrayList<RenderObject>();
	}
	
	public void add(RenderObject o) {
		
		children.add(o);
		o.parent = this;
		o.view = view;
	}
	
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
	public Vector2f getAbsolutePosition() {
		
		Vector2f result = new Vector2f();
		if (parent != null) {
			Vector2f.add(position, parent.getAbsolutePosition(), result);
		}
		else {
			result = position;
		}
		
		return result;
	}
	
	public Vector2f getPosition() {
		
		return position;
	}
	
	public void remove(RenderObject o) {
		
		children.remove(o);
		o.parent = null;
	}
	
	public ArrayList<RenderObject> getChildren() {
		
		return children;
	}
	
	public void render() {
		
		if (parent != null) {
			
			rotation += parent.rotation;
		}
		
		for (RenderObject child : children) {
			
			child.render();
		}
	}
}
