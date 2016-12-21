package de.rwthaachen.gp.marblefun.math;

public class Vector2f {
	public float x,y;
	
	public Vector2f(){
		x=0.0f;
		y=0.0f;
	}
	
	public Vector2f(float x, float y){
		this.x=x;
		this.y=y;
	}
	
	public Vector2f(double x, double y){
		this((float)x,(float)y);
	}
	
	public String toString() {
		return "["+x+", "+y+"]";
	}
}
