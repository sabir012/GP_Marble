package org.lwjglb.game.objects;

import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.Gravitable;
import org.lwjglb.engine.Physics;
import org.lwjglb.engine.graph.Mesh;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Ball extends GameItem implements Gravitable{

	private float radius;
	private Vector3f velocity;
	private float mass;
	
	private Material material;
	
	public Ball(Mesh mesh) {
		super(mesh);
		
		//Vector3f point = mesh.vertexes;
		//this.radius = (float)Math.sqrt(point.x*point.x + point.y*point.y + point.z*point.z);
	}
	
	public Ball(Mesh mesh, float radius, Vector3f velocity) {
		super(mesh);
		this.radius = radius;
		this.velocity = velocity;
	}
	
	
	public float getRadius(){
		return radius;
	}
	
	public void setRadius(float newRadius){
		this.radius = newRadius;
	}
    
    public void handleBallCollision(Ball ball){
    	if(this.isCollide(ball)){
    		Vector3f displacement = this.getPosition().sub(ball.getPosition()).normalize();
       		
    		this.velocity = this.velocity.sub(displacement.mul(2).mul(this.velocity.dot(displacement)));
    		ball.velocity = ball.velocity.sub(displacement.mul(2).mul(ball.velocity.dot(displacement)));
    	} 
    }

	@Override
	public boolean isCollide(Ball ball) {
  	  // we are using multiplications because it's faster than calling Math.pow
	  Vector3f ballPosition = ball.getPosition();
	  Vector3f currentBallPosition = this.getPosition();
  	
      double distance = Math.sqrt((currentBallPosition.x - ballPosition.x) * (currentBallPosition.x - ballPosition.x) +
  	                           (currentBallPosition.y - ballPosition.y) * (currentBallPosition.y - ballPosition.y) +
  	                           (currentBallPosition.z - ballPosition.z) * (currentBallPosition.z - ballPosition.z));
      
      System.out.println("Distance: "+distance);
      
      return distance < (this.radius + ball.radius); 
	}

	@Override
	public Vector2f calculateGravityForce(Vector2f slope) {
		Vector2f gravityForce = new Vector2f((float) Math.sin(Math.toRadians(slope.x)), (float) Math.sin(Math.toRadians(slope.y)));
        gravityForce.mul(5f/7f*mass*Physics.GRAVITY.getValue());
        return gravityForce;
	}
}
