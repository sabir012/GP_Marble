package org.lwjglb.game.objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Texture;

public class Particle{
	
	private Vector2f position;
	private Vector2f velocity;
	private Vector2f acceleration;
	private Vector2f ev;
//	private Vector3f color;
//	private Vector2f force;
//	private float mass;
	private float density;
	private float pressure;
//	private float surf_norm;
//	private PhysicalMaterial material = PhysicalMaterial.PLASTIC;
	
//	private int id;
	
	public Particle nextP;
	private int vaoId;
	private int vboId;
	
	public Particle(){}
	
	public Particle(Vector2f position, Vector2f velocity, Vector2f acceleration, Vector2f ev, float density,
			float pressure, Particle nextP) {
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
		this.ev = ev;
		this.density = density;
		this.pressure = pressure;
	//	this.id = id;
		this.nextP = nextP;
		
	/*	Texture tex = new Texture(material.getTexturePath());
		
		Material mat = new Material(tex,0f);
		
		
		mesh.setMaterial(mat);*/
	//	System.out.println(position.x+" "+ position.y);
		float[] vertices = new float[]{position.x, position.y};
	    FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
	    verticesBuffer.put(vertices).flip();
	    
	    vaoId = glGenVertexArrays();
	    glBindVertexArray(vaoId);
	    
	    vboId = glGenBuffers();
	    glBindBuffer(GL_ARRAY_BUFFER, vboId);
	    glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
	    glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
	 // Unbind the VBO
	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	    // Unbind the VAO
	    glBindVertexArray(0);
	}
	
	public int getVaoId() {
        return vaoId;
    }
	
	public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);
        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

	public Vector2f getPosition2f() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
	public void setPositionX(float x){
		this.velocity.x = x;
	}
	
	public void setPositionY(float y){
		this.velocity.y = y;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}
	
	public void setVelocityX(float x){
		this.velocity.x = x;
	}
	
	public void setVelocityY(float y){
		this.velocity.y = y;
	}

	public Vector2f getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Vector2f acceleration) {
		this.acceleration = acceleration;
	}

	public Vector2f getEv() {
		return ev;
	}

	public void setEv(Vector2f ev) {
		this.ev = ev;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public float getPressure() {
		return pressure;
	}

	public void setPressure(float pressure) {
		this.pressure = pressure;
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
	
	public void render(){
		// Bind to the VAO
		glBindVertexArray(vaoId);
		glEnableVertexAttribArray(0);
		// Draw the vertices
		glDrawArrays(GL_POINTS, 0, 2);
		// Restore state
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
    }

}
