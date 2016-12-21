package de.rwthaachen.gp.marblefun.GameObjects;

import de.rwthaachen.gp.marblefun.math.*;

public abstract class GameObject {
	private Vector2f position;
	
	public GameObject(Vector2f position) {
		this.position = position;
	}
	
	public boolean isColliding() { // Ball collides with object
		return false;
	}
	
	public Vector2f getPosition() {
		return position;
	}
	
	public String toString() {
		return "Position: "+position+"\n";
	}
}
