package com.goudagames.engine.util;

import java.util.HashMap;

import org.lwjgl.util.vector.Vector2f;

public enum Direction {
	
	NONE(-1, new Vector2f(0f, 0f)), DOWN(0, new Vector2f(0f, -1f)), LEFT(1, new Vector2f(-1f, 0f)),
			UP(2, new Vector2f(0f, 1f)), RIGHT(3, new Vector2f(1f, 0f));
	
	public int id;
	Vector2f direction;
	static HashMap<Integer, Direction> idMap = new HashMap<Integer, Direction>();
	static HashMap<Vector2f, Direction> vectorMap = new HashMap<Vector2f, Direction>();
	
	private Direction(int id, Vector2f direction) {
		
		this.id = id;
		this.direction = direction;
	}
	
	/**
	 * Return the direction rotated 90 degrees clockwise.
	 * @return direction
	 */
	public Direction rotate90CW() {
		
		int nid = id + 1;
		
		if (nid > 3) {
			
			nid = 0;
		}
		
		return idMap.get(nid);
	}
	
	/**
	 * Return the direction rotated 90 degrees counter-clockwise.
	 * @return direction
	 */
	public Direction rotate90CounterCW() {
		
		int nid = id - 1;
		
		if (nid < 0) {
			
			nid = 3;
		}
		
		return idMap.get(nid);
	}
	
	/**
	 * Get the 2D vector representation of this direction.
	 * @return vector
	 */
	public Vector2f toVector() {
		
		return direction;
	}
	
	/**
	 * Get the opposite direction.
	 * @return opposite
	 */
	public Direction getOpposite() {
		
		Direction opp = vectorMap.get(direction.negate());
		
		if (opp == null) {
			
			return Direction.NONE;
		}
		
		return opp;
	}
}
