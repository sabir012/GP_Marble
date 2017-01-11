package org.lwjglb.game.objects;

import org.joml.Vector3f;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.graph.Mesh;

public class Wall extends GameItem{

	private Vector3f minCorner;
	private Vector3f maxCorner;
	
	public Wall(Mesh mesh) {
		super(mesh);
		// TODO Auto-generated constructor stub
	}
	
	
	public Wall(Mesh mesh, Vector3f min, Vector3f max) {
		super(mesh);
		// TODO Auto-generated constructor stub
		
		this.minCorner = min;
		this.maxCorner = max;
	}
	
	public void setMinCorner(Vector3f min){
		this.minCorner = min;
	}
	
	public void setMaxCorner(Vector3f max){
		this.maxCorner = max;
	}
	
	public Vector3f getMinCorner(){
		return this.minCorner;
	}
	
	public Vector3f getMaxCorner(){
		return this.maxCorner;
	}

	public boolean isCollide(Ball ball) {
    	// get box closest point to sphere center by clamping
    	Vector3f minCorner = this.getMinCorner();
    	Vector3f maxCorner = this.getMaxCorner();
    	Vector3f ballPosition = ball.getPosition();
    	
    	  float x = Math.max(minCorner.x, Math.min(ballPosition.x, maxCorner.x));
    	  float y = Math.max(minCorner.y, Math.min(ballPosition.y, maxCorner.y));
    	  float z = Math.max(minCorner.z, Math.min(ballPosition.z, maxCorner.z));

    	  // this is the same as isPointInsideSphere
    	  double distance = Math.sqrt((x - ballPosition.x) * (x - ballPosition.x) +
    	                           (y - ballPosition.y) * (y - ballPosition.y) +
    	                           (z - ballPosition.z) * (z - ballPosition.z));
    	  
    	  return distance < ball.getRadius();
	}
}
