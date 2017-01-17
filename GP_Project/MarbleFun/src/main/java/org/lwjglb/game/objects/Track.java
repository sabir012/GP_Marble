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
     
 
	public boolean isCollide(Ball ball, float dt) {
    	// get box closest point to sphere center by clamping
//    	Vector3f ballPosition = ball.getPosition();
//    	
//    	  float x = Math.max(box.getMinX().x, Math.min(ballPosition.x, box.getMaxX().x));
//    	  float y = Math.max(box.getMinX().y, Math.min(ballPosition.y, box.getMaxX().y));
//    	  float z = Math.max(box.getMinX().z, Math.min(ballPosition.z, box.getMaxX().z));
//
//    	  // this is the same as isPointInsideSphere
//    	  double distance = Math.sqrt((x - ballPosition.x) * (x - ballPosition.x) +
//    	                           (y - ballPosition.y) * (y - ballPosition.y) +
//    	                           (z - ballPosition.z) * (z - ballPosition.z));
//    	  
//    	  return distance < ball.getRadius();
    	  
    	  Vector3f C = getPosition();
    	  Vector3f E = box.getMinX();
    	  Vector3f L = box.getMaxX();
    	  
    	  Vector3f d = L.sub(E); // Direction vector of ray, from start to end )
    	  Vector3f f= E.sub(C); //Vector from center sphere to ray start )
    	  
    	  Vector3f P = E.add(d.mul(dt));
    	  
    	  float distance = (float)Math.sqrt((P.x-C.x)*(P.x-C.x) + (P.y-C.y)*(P.y-C.y)+(P.z-C.z)*(P.z-C.z));
    	  
    	  System.out.println(distance+"---"+ball.getRadius());
    	  
    	  return distance<=ball.getRadius();
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

	@Override
	public boolean isCollide(Ball ball) {
		// TODO Auto-generated method stub
		return false;
	}
 
}