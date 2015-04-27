package com.goudagames.engine.render.object;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

public class RenderObjectContainer {
	
	public RenderObjectContainer parent;
	private ArrayList<RenderObject> children;
	
	public float rotation = 0f;
	public Vector2f offset = new Vector2f();
	public Vector2f position = new Vector2f();
	
	public boolean view = true;
	
	public RenderObjectContainer() {
		
		children = new ArrayList<RenderObject>();
	}
	
	public void add(RenderObject o) {
		
		children.add(o);
		o.parent = this;
		o.view = view;
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
			Vector2f.add(position, parent.position, position);
		}
		
		for (RenderObject child : children) {
			
			child.render();
		}
	}
}
