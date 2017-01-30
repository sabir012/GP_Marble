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
	private Vector3f velocity;
	private Vector3f acceleration;
	private Vector3f accumulatedNormal;
	private boolean movable;
	private float mass;
	public Particle nextP;
	private int vaoId;
	private int vboId;
	private Timer time;
	private float damp;
	private float elapsedTime;
	
	public Particle(){}
	
	public Particle(Vector3f pos){
		this.position = pos;
		this.oldPosition = pos;
		this.acceleration=new Vector3f(0,0,0);
		this.mass=1.0f;
		time = new Timer();
		elapsedTime=time.getElapsedTime();
		damp = 0.01f;
	}
		
	
	
	public void addForce(Vector3f f){
		acceleration = acceleration.add(f.div(mass));
	}
	
	public void timeStamp(){
		if(isMovable()){
			float damping = 1.0f - damp;
			Vector3f temp = position;
			Vector3f temp1 = position;
			position = (position.sub(oldPosition));
			Vector3f newPos = position.mul(damping);
			Vector3f newAcc = acceleration.mul(elapsedTime);
			position = temp1.add(newPos.add(newAcc));
			oldPosition = temp;
			acceleration = new Vector3f(0,0,0);
		}
	}
	
	public void offsetPos(Vector3f pos){
		if(isMovable()){
			position.add(pos);
		}
	}
	
	public void addNormal(Vector3f norm){
		if(isMovable()){
			accumulatedNormal.add(norm);
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

	public int getVaoId() {
        return vaoId;
    }
	


	public Vector3f getPosition2f() {
		return position;
	}

	public void setPosition(Vector3f pos) {
		this.position = pos;
	}
	
	public void setPositionX(float x){
		this.velocity.x = x;
	}
	
	public void setPositionY(float y){
		this.velocity.y = y;
	}

	public Vector3f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector3f velocity) {
		this.velocity = velocity;
	}
	
	public void setVelocityX(float x){
		this.velocity.x = x;
	}
	
	public void setVelocityY(float y){
		this.velocity.y = y;
	}

	public Vector3f getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector3f acceleration) {
		this.acceleration = acceleration;
	}

	
	
/*	public float getSurf_norm() {
		return surf_norm;
	}

	public void setSurf_norm(float surf_norm) {
		this.surf_norm = surf_norm;
	}
*/
	public Particle getNextP() {
		return nextP;
	}

	public void setNextP(Particle nextP) {
		this.nextP = nextP;
	}
/*
	public int getId() {
		return id;
	}*/
	
	

}
