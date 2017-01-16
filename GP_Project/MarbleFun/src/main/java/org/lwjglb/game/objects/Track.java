package org.lwjglb.game.objects;


import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.graph.Box;
import org.lwjglb.engine.graph.Mesh;

public class Track extends GameItem {
    
    private float slope; // 0 to 90
    private PhysicalMaterial material;
	
	private Box box;
	
	
	public Track(Mesh mesh) {
		super(mesh);
		
		this.box = mesh.getBox();
		
		//slope = (float)Math.toDegrees(Math.atan((minCorner.y - maxCorner.y)/(minCorner.x - maxCorner.x)));
		this.material = PhysicalMaterial.GRAS;
	}
	
    public Track(Mesh mesh, PhysicalMaterial material) {
        super(mesh);
        
        this.material = material;
		
        slope = 0;
    }
     
 
	public boolean isCollide(Ball ball) {
    	// get box closest point to sphere center by clamping
    	Vector3f ballPosition = ball.getPosition();
    	
    	  float x = Math.max(box.getMinX(), Math.min(ballPosition.x, box.getMaxX()));
    	  float y = Math.max(box.getMinY(), Math.min(ballPosition.y, box.getMaxY()));
    	  float z = Math.max(box.getMinZ(), Math.min(ballPosition.z, box.getMaxY()));

    	  // this is the same as isPointInsideSphere
    	  double distance = Math.sqrt((x - ballPosition.x) * (x - ballPosition.x) +
    	                           (y - ballPosition.y) * (y - ballPosition.y) +
    	                           (z - ballPosition.z) * (z - ballPosition.z));
    	  
    	  return distance < ball.getRadius();
	}
     
    public void setSlope(float slope) {
        this.slope = slope;
    }
    
	@Override
    public void setScale(float scale) {
        super.setScale(scale);
        this.box.setScale(scale);
    }
     
    public float getSlope() {
        return slope;
    }
    public PhysicalMaterial getMaterial() {
        return this.material;
    }
 
}