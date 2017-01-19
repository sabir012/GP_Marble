package org.lwjglb.game.objects;

import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.Gravitable;
import org.lwjglb.engine.Physics;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Texture;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Ball extends GameItem implements Gravitable{

	private float radius;
	private Vector3f velocity;
	private float mass;
	private float perimeter;
	private PhysicalMaterial material;
	
	public Ball(Mesh mesh) throws Exception {
		this(mesh,1,new Vector3f(),PhysicalMaterial.PLASTIC);
	}
	
	public Ball(Mesh mesh,float radius, Vector3f velocity,PhysicalMaterial material) throws Exception {
		super(mesh);
		this.velocity = velocity;
		this.material = material;
		this.radius = radius;
		this.setScale(radius);
		calculateMass();
		
		Texture tex = new Texture(material.getTexturePath());
		
		Material mat = new Material(tex,0f);
		
		
		mesh.setMaterial(mat);
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
        this.setRadius(scale);
    }
    
	private void calculateMass() {
		mass = 1;//(float) (4.0/3.0*Math.PI*(radius*Math.pow(10, -4))*material.getMass());
		System.out.println(mass);
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
		
		new_acceleration = force.div(mass);
		avg_acceleration = last_acceleration.add(new_acceleration ).div(2);
		velocity = velocity.add(avg_acceleration.mul(dt));
		
		this.setPosition(position.x, position.y, position.z);
	}
	
	//TEST
	public void collideWithWall(){
		velocity = velocity.mul((-0.5f));
		this.setPosition(this.getPosition().x, this.getPosition().y+radius, -1);
	}

	@Override
	public Vector2f calculateGravityForce(float slope) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setVelocity(Vector3f velocity){
		this.velocity = velocity;
	}
	
	public Vector3f getVelocity(){
		return velocity;
	}
}
