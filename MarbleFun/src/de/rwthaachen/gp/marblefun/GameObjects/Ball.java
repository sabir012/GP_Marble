package de.rwthaachen.gp.marblefun.GameObjects;

import de.rwthaachen.gp.marblefun.math.*;

public class Ball extends GameObject {
	private float radius; // in cm
	private float mass; // in kg
	private Material material;
	
	public Ball(Vector2f position, Material material, float radius) {
		super(position);
		this.material = material;
		this.radius = radius;
		calculateMass();
	}
	
	private void calculateMass() { // calculate ball mass from material and ball size
		mass = (float) (4.0/3.0*Math.PI*(radius*Math.pow(10, -6))*material.getMass());
	}
	
	public float getMass() {
		return mass;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public String toString() {
		return super.toString()+
				"Mass: "+mass+" kg\n"+
				"Radius: "+radius+" cm\n"+
				"Material: "+material+"\n";
	}
	
}
