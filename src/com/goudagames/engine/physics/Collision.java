package com.goudagames.engine.physics;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.goudagames.engine.geometry.Line;
import com.goudagames.engine.geometry.Rectangle;
import com.goudagames.engine.util.Direction;

public class Collision {

	public static Direction getCollisionDirection(Rectangle r1, Rectangle r2) {
		
		if (r1.left() > r2.left() && r1.left() < r2.right()) {
			
			if (r1.up() > r2.down() && r1.up() < r2.up()) {
				
				if (r1.up() - r2.down() < r2.left() - r1.left()) {
					return Direction.UP;
				}
				else {
					return Direction.LEFT;
				}
			}
			else if (r1.down() > r2.down() && r1.down() < r2.up()) {
				
				if (r2.up() - r1.down() > r2.left() - r1.left()) {
					return Direction.DOWN;
				}
				else {
					return Direction.LEFT;
				}
			}
		}
		else if (r1.right() > r2.left() && r1.right() < r2.right()) {
			
			if (r1.up() > r2.down() && r1.up() < r2.up()) {
				
				if (r1.up() - r2.down() < r1.right() - r2.right()) {
					return Direction.UP;
				}
				else {
					return Direction.RIGHT;
				}
			}
			else if (r1.down() > r2.down() && r1.down() < r2.up()) {
				
				if (r2.up() - r1.down() > r1.right() - r2.right()) {
					return Direction.DOWN;
				}
				else {
					return Direction.RIGHT;
				}
			}
		}
		
		return null;
	}
	
	public static Vector3f isLinesIntersecting(Line l1, Line l2) {
		
		Vector2f p = l1.v0;
		Vector2f v1 = l1.v1;
		Vector2f q = l2.v0;
		Vector2f v3 = l2.v1;
		
		Vector2f r = new Vector2f();
		Vector2f s = new Vector2f();
		Vector2f pq = new Vector2f();
		
		Vector2f.sub(v1, p, r); //p + r
		Vector2f.sub(v3, q, s); //q + s
		Vector2f.sub(p, q, pq); //p - q
		
		float denom = r.x * s.y - s.x * r.y;
		
		if (denom == 0) return new Vector3f(0f, 0f, 0f);
		boolean denomPositive = denom > 0;
		
		float s_number = r.x * pq.y  - r.y * pq.x;
		if ((s_number < 0) == denomPositive) return new Vector3f(0f, 0f, 0f);
		
		float t_number = s.x * pq.y - s.y * pq.x;
		if ((t_number < 0) == denomPositive) return new Vector3f(0f, 0f, 0f);
		
		if (((s_number > denom) == denomPositive) || ((t_number > denom) == denomPositive)) {
			return new Vector3f(0f, 0f, 0f);
		}
		
		float t = t_number / denom;
		
		Vector3f result = new Vector3f(0f, 0f, 1f);
		
		result.x = p.x + (t * r.x);
		result.y = p.y + (t * r.y);
		
		return result;
	}
}
