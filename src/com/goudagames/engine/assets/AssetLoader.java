package com.goudagames.engine.assets;

import java.util.ArrayList;

public class AssetLoader {

	private ArrayList<Resource> resources = new ArrayList<Resource>();
	private boolean dirty = false;
	
	private void loadResources(boolean forceLoad) {
		
		for (Resource r : resources) {
			
			if (!r.isLoaded() || forceLoad) {
				
				r.load();
			}
		}
	}
	
	public void load(boolean forceLoad) {
		
		if (dirty || forceLoad) {
			
			loadResources(forceLoad);
			dirty = false;
		}
	}
	
	public void addResource(Resource r) {
		
		resources.add(r);
		markDirty();
	}
	
	public void markDirty() {
		
		dirty = true;
	}
	
	public boolean isDirty() {
		
		return dirty;
	}
}
