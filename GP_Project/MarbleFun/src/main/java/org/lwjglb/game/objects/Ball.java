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
	private float perimeter;
	private PhysicalMaterial material;
	
	public Ball(Mesh mesh) {
		super(mesh);
		this.radius = (float)Math.sqrt(mesh.getPositionOnObject().x * mesh.getPositionOnObject().x 
									+ mesh.getPositionOnObject().y * mesh.getPositionOnObject().y 
									+ mesh.getPositionOnObject().z * mesh.getPositionOnObject().z);
		material = PhysicalMaterial.PLASTIC;
		calculateMass();
		velocity = new Vector3f();
	}
	
	public Ball(Mesh mesh, Vector3f velocity,PhysicalMaterial material) {
		this(mesh);
		
		this.velocity = velocity;
		this.material = material;
		perimeter = (float) (2*Math.PI*this.radius);
	}
	
	
	public float getRadius(){
		return radius;
	}
	
	public PhysicalMaterial getMaterial() {
        return material;
    }
	
	public void setRadius(float newRadius){
		this.radius = newRadius;
	}
	
	@Override
    public void setScale(float scale) {
        super.setScale(scale);
        this.setRadius(this.getRadius()*scale);
    }
    
	private void calculateMass() {
		mass = 100f;//(float) (4.0/3.0*Math.PI*(radius*Math.pow(10, -6))*material.getMass());
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

//	@Override
//	public Vector2f calculateGravityForce(float slope) {
//		Vector2f gravityForce = new Vector2f((float) Math.sin(Math.toRadians(slope)),0);
//        gravityForce.mul(5f/7f*mass*Physics.GRAVITY.getValue());
//        return gravityForce;
//	}
	
//	public Vector2f calculateTrackFrictionForce(Track track) {
//		Vector2f force = new Vector2f();
//		if (track != null) {
//	        float slope = track.getSlope();
//	        force.x *= (-1) * mass * Physics.GRAVITY.getValue() * Math.sin(Math.toRadians(slope)) * (track.getMaterial().getFrictionCoeff() + material.getFrictionCoeff());
//	        force.y = 0;
//		} else {
//			force.x = 0;
//			force.y = (-1) * mass * Physics.GRAVITY.getValue();
//		}
//        
//        return force;
//    }
	
//	public void updateVelocity(Vector2f force, float t) {
//        velocity.x += force.x/mass*t;
//        velocity.y += force.y/mass*t;
//        Vector3f pos = this.getPosition();
//        pos.y += velocity.x*t;
//        pos.x += velocity.y*t;
//        Vector3f rot = this.getRotation();
//        System.out.println("Rot: "+getRotation());
//        //this.setRotation(rot.x+(velocity.y*t*36000/this.perimeter), rot.y, rot.z+(velocity.x*t*36000/this.perimeter));
//    }
	
	private Vector3f last_acceleration = new Vector3f();
	private Vector3f acceleration= new Vector3f();
	private Vector3f new_acceleration= new Vector3f();
	private Vector3f avg_acceleration= new Vector3f();
	private Vector3f force= new Vector3f();
	private float coefficientOfDrag = 0.47f;// Coeffecient of drag for a ball
	private float frontalAreaOfTheBall;
	private float densityOfAir = 1.2f;// Density of air. Try 1000 for water.
	
	public void calculateFinalForce(){
		frontalAreaOfTheBall = (float)Math.PI * radius * radius / 10000;
		
		this.force = this.force.add(new Vector3f(0,(float)(mass * 9.81),0)); 
		this.force = this.force.add(new Vector3f((float)(-1 * 0.5 * densityOfAir * coefficientOfDrag * frontalAreaOfTheBall * velocity.x * velocity.x),
				(float)(-1 * 0.5 * densityOfAir * coefficientOfDrag * frontalAreaOfTheBall * velocity.y * velocity.y),
				(float)(-1 * 0.5 * densityOfAir * coefficientOfDrag * frontalAreaOfTheBall * velocity.z * velocity.z)));
	}
	
	
	public void updateGravity(float dt){
		Vector3f position = this.getPosition();
		
		
		frontalAreaOfTheBall = (float)Math.PI * radius * radius / 10000;
		
		force = force.add(new Vector3f(0,(float)(mass * 9.81),0)); 
		force = force.add(new Vector3f((float)(-1 * 0.5 * densityOfAir * coefficientOfDrag * frontalAreaOfTheBall * velocity.x * velocity.x),
				(float)(-1 * 0.5 * densityOfAir * coefficientOfDrag * frontalAreaOfTheBall * velocity.y * velocity.y),
				(float)(-1 * 0.5 * densityOfAir * coefficientOfDrag * frontalAreaOfTheBall * velocity.z * velocity.z)));
		
		last_acceleration = acceleration;
		position.x = position.x + velocity.x*dt + last_acceleration.x*0.5f*dt*dt;
		position.y = position.y + (-1)*(velocity.y*dt + last_acceleration.y*0.5f*dt*dt);
		position.z = position.z + velocity.z*dt + last_acceleration.z*0.5f*dt*dt;
		
		//position =  position.add(this.velocity.mul(dt).add(last_acceleration.mul(0.5f * dt*dt)));
		new_acceleration = force.div(mass);
		avg_acceleration = last_acceleration.add(new_acceleration ).div(2);
		velocity = velocity.add(avg_acceleration.mul(dt));
		
		this.setPosition(position.x, position.y, position.z);
	}

	@Override
	public Vector2f calculateGravityForce(float slope) {
		// TODO Auto-generated method stub
		return null;
	}
}
