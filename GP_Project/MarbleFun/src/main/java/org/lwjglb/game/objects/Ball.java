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
	
	public float getMass(){
		return this.mass;
	}
    
	private void calculateMass() {
		mass = 1;//(float) (4.0/3.0*Math.PI*(radius*Math.pow(10, -4))*material.getMass());
		System.out.println(mass);
	}
    
    public void handleBallCollision(Ball ball){
    	if(this.isCollide(ball)){
    		Vector3f currentPosition = new Vector3f(this.getPosition().x,this.getPosition().y,this.getPosition().z);
    		Vector3f ballPosition = new Vector3f(ball.getPosition().x,ball.getPosition().y,ball.getPosition().z);
    		
    		Vector3f currentVelocity = this.getVelocity();
    		Vector3f ballVelocity = ball.getVelocity();
    		
    	    Vector3f x = ballPosition.sub(currentPosition);
    	    ballPosition = ball.getPosition();
    	    x = x.normalize();
    	     
    	    Vector3f v1 = currentVelocity;
    	    Vector3f v1Temp = new Vector3f(v1.x,v1.y,v1.z);
    	    float x1 = v1Temp.dot(x);
    	    v1Temp = new Vector3f(v1.x,v1.y,v1.z);
    	    
    	    Vector3f xTemp = new Vector3f(x.x,x.y,x.z);
    	    Vector3f v1x = xTemp.mul(x1);
    	    xTemp = new Vector3f(x.x,x.y,x.z);
    	    Vector3f v1y = v1Temp.sub(v1x);
    	    v1Temp = new Vector3f(v1.x,v1.y,v1.z);
    	    
            float m1 = this.getMass();
    	    
    	    x = x.mul(-1);
    	    xTemp = new Vector3f(x.x,x.y,x.z);
    	    Vector3f v2 = ballVelocity;
    	    Vector3f v2Temp = new Vector3f(v2.x,v2.y,v2.z);
    	    
    	    float x2 =v2Temp.dot(xTemp);
    	    xTemp = new Vector3f(x.x,x.y,x.z);
    	    v2Temp = new Vector3f(v2.x,v2.y,v2.z);
    	    
    	    Vector3f v2x = xTemp.mul(x2);
    	    xTemp = new Vector3f(x.x,x.y,x.z);
            Vector3f v2y = v2Temp.sub(v2x);
    	    v2Temp = new Vector3f(v2.x,v2.y,v2.z);

    	    float m2 = ball.getMass();
    	    
    	    float combinedMass = m1 + m2;
    	    
    	    
    	    Vector3f v1xTemp = new Vector3f(v1x.x,v1x.y,v1x.z);
    	    Vector3f v1yTemp = new Vector3f(v1y.x,v1y.y,v1y.z);
    	    Vector3f v2xTemp = new Vector3f(v2x.x,v2x.y,v2x.z);
    	    
    	    Vector3f newVelA = v1xTemp.mul((m1 - m2)/combinedMass).add(v2xTemp.mul((2f * m2) / combinedMass)).add(v1yTemp);
    	    
    	    v1xTemp = new Vector3f(v1x.x,v1x.y,v1x.z);
    	    Vector3f v2yTemp = new Vector3f(v2y.x,v2y.y,v2y.z);
    	    v2xTemp = new Vector3f(v2x.x,v2x.y,v2x.z);
    	    Vector3f newVelB = v1xTemp.mul((2f * m1) / combinedMass).add(v2xTemp.mul((m2 - m1) / combinedMass)).add(v2yTemp);
    	    
            this.setVelocity(new Vector3f(newVelA.x,newVelA.y,this.velocity.z));
            ball.setVelocity(new Vector3f(newVelB.x, newVelB.y, ballVelocity.z));
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
		
		Vector3f forceTemp = new Vector3f(force.x,force.y,force.z);
		
		new_acceleration = forceTemp.div(mass);
		Vector3f lastTempAcc = new Vector3f(last_acceleration.x,last_acceleration.y,last_acceleration.z);
		avg_acceleration = lastTempAcc.add(new_acceleration ).div(2);
		Vector3f avgTempAcc = new Vector3f(avg_acceleration.x,avg_acceleration.y,avg_acceleration.z);
		
		velocity = velocity.add(avgTempAcc.mul(dt));
		
		this.setPosition(position.x, position.y, position.z);
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
