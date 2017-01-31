	package org.lwjglb.game.objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Texture;
import org.lwjglb.engine.*;

public class Particle{
	
	private Vector3f position;
	private Vector3f oldPosition;
	private Vector3f acceleration;
	private Vector3f accumulatedNormal;
	private boolean movable;
	private float mass;
	private float damp;
	private float elapsedTime;
	
	public Particle(Vector3f pos){
		this.position = pos;
		this.oldPosition = pos;
		this.acceleration=new Vector3f(0.0f,0.0f,0.0f);
		this.mass=1.0f;
		this.movable = true;
		this.accumulatedNormal = new Vector3f(0.0f,0.0f,0.0f);
		elapsedTime = 0.25f;
		damp = 0.01f;
	}
		
	
	
	public void addForce(Vector3f f){
		this.acceleration.add(f.div(mass));
	}
	
	public void timeStamp(){
		if(isMovable()){
			float damping = 1.0f - damp;
			Vector3f temp = position;
			Vector3f temp1 = position;
			position = (position.sub(oldPosition));
			Vector3f newPos = position.mul(damping);
			Vector3f newAcc = acceleration;
			newAcc.mul(elapsedTime);
			position = temp1.add(newPos.add(newAcc));
			oldPosition = temp;
			acceleration = new Vector3f(0.0f,0.0f,0.0f);
		}
	}
	
	public void offsetPos(Vector3f pos){
		if(isMovable()){
			position.add(pos);
		}
	}
	
	
	
	public Vector3f getAccumulatedNormal() {
		return accumulatedNormal;
	}

	public void setAccumulatedNormal(Vector3f accumulatedNormal) {
		this.accumulatedNormal = accumulatedNormal;
	}

	public boolean isMovable() {
		return movable;
	}

	public void setMovable(boolean movable) {
	
		this.movable = movable;
	}	
	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(float elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public Vector3f getPosition() {
		return position;
	}

		

	public void setPosition(Vector3f pos) {
		this.position = pos;
	}
	
	
	public Vector3f getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector3f acceleration) {
		this.acceleration = acceleration;
	}

	public void addAccumulatedNormal(Vector3f normal) {
		// TODO Auto-generated method stub
		if(isMovable()){
			accumulatedNormal.add(normal);
		}
		
	}

		

}
