package org.lwjglb.game.objects;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.graph.Mesh;

public class Track extends GameItem {
    
    private float slope; // 0 to 90
    private Material material;
    
	private Vector3f minCorner;
	private Vector3f maxCorner;
	
	public Track(Mesh mesh) {
		super(mesh);
		
		this.minCorner = mesh.minXMaxY;
		this.maxCorner = mesh.maxXMinY;
		slope = (float)Math.toDegrees(Math.atan((minCorner.y - maxCorner.y)/(minCorner.x - maxCorner.x)));
		this.material = Material.GRAS;
	}
	
    public Track(Mesh mesh, Material material,Vector3f min, Vector3f max) {
        super(mesh);
        
        this.material = material;

        this.minCorner = min;
		this.maxCorner = max;
		
        slope = 0;
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
     
    public void setSlope(float slope) {
        this.slope = slope;
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
     
    public float getSlope() {
        return slope;
    }
    public Material getMaterial() {
        return this.material;
    }
 
}