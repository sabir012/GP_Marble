package org.lwjglb.game.objects;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Vector;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;

public class Cloth{

	private static final int CONSTRAINT_ITERATIONS = 15;
	private int width;
	
	private int height;
	private float scale;
	private Particle particle;
	private Constraint cons;
	private Vector3f rotation;
	private boolean trans = false;
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private ArrayList<Constraint> constraints;
	
	private List<Vector3f> posVertices = new ArrayList<>();
	private List<Vector3f> posNormals = new ArrayList<>();
	private List<Integer> posIndices = new ArrayList<>();
	private HashMap<Integer,Integer> check = new HashMap<Integer, Integer>();
	Ball ball;
	private int value;
	
	public Cloth(float widthParticle, float heightParticle, int numParticlesWidth, int numParticlesHeight){
		rotation = new Vector3f(0.0f,0.0f,0.0f);
		value = 0;
		constraints = new ArrayList<Constraint>();
		this.width=numParticlesWidth;
		this.height =numParticlesHeight;
		//System.out.println(width*height + "width height" + height);
		particles.ensureCapacity(width*height);
		
		for(int i=0;i<width*height;i++)
			particles.add(null);
		for(int x=0; x<width; x++)
		{
			for(int y=0; y<height; y++)
			{
				Vector3f pos = new Vector3f(widthParticle * (x/(float)width),
								-heightParticle * (y/(float)height),
								-10);
				particle = new Particle(pos);
				particles.set((y*width + x),particle);
			}
		}
		for(int x=0; x<width; x++)
		{
			for(int y=0; y<height; y++)
			{
				if (x<width-1) 
					makeConstraint(getParticle(x,y),getParticle(x+1,y));
				if (y<height-1)
					makeConstraint(getParticle(x,y),getParticle(x,y+1));
				if (x<width-1 && y<height-1)
					makeConstraint(getParticle(x,y),getParticle(x+1,y+1));
				if (x<width-1 && y<height-1) 
					makeConstraint(getParticle(x+1,y),getParticle(x,y+1));
			}
		}
		for(int x=0; x<width; x++)
		{
			for(int y=0; y<height; y++)
			{
				if (x<width-2) 
					makeConstraint(getParticle(x,y),getParticle(x+2,y));
				if (y<height-2) 
					makeConstraint(getParticle(x,y),getParticle(x,y+2));
				if (x<width-2 && y<height-2) 
					makeConstraint(getParticle(x,y),getParticle(x+2,y+2));
				if (x<width-2 && y<height-2) 
					makeConstraint(getParticle(x+2,y),getParticle(x,y+2));
			}
		}
		for(int i=0;i<3; i++)
		{
			particle = getParticle(0+i ,0);
			Vector3f temp = new Vector3f(0.0f,0.0f,0.0f);
			particle.offsetPos(temp.set(0.5f,0.0f,0.0f)); 
			particle.setMovable(false);
			particle.offsetPos(temp.set(-0.5f,0.0f,0.0f));
			particle = getParticle(width-1-i,0);
			particle.setMovable(false);
		}
	}
	
	public List<Vector3f> getPosVertices() {
		
		System.out.println(posVertices.size());
		return posVertices;
	}
	public List<Vector3f> getPosNormals() {
	
		System.out.println(posNormals.size());
		return posNormals;
	}
	

	
	public List<Integer> getPosIndices() {
		
		System.out.println(posIndices.size() + "Indicessize");
		return posIndices;
	}

	public void setPosIndices(List<Integer> posIndices) {
		this.posIndices = posIndices;
	}

	
	public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(float x, float y, float z) {
        this.rotation.x = x;
        this.rotation.y = y;
        this.rotation.z = z;
    }
	
	public Particle getParticle(int x, int y){
		particle = particles.get(width*y+x);
		return particle;
	}
	
	
	public Particle getDrawParticle(int x, int y){
		int ind = width*y+x;
		particle = particles.get(ind);
		
		if(check.containsKey(ind))
			posIndices.add(check.get(ind));
		else{
			posIndices.add(value);
			check.put(ind, value++ );
		}
		return particle;
		
	}
	public void makeConstraint(Particle P1,Particle P2){
		cons = new Constraint(P1,P2);
		constraints.add(cons);
	}
	public Vector3f calcTriangleNormal(Particle P1, Particle P2, Particle P3){
		Vector3f pos1 = P1.getPosition();
		Vector3f pos2 = P2.getPosition();
		Vector3f pos3 = P3.getPosition();
		Vector3f v1 = pos2.sub(pos1);
		Vector3f v2 = pos3.sub(pos1);
		return v1.cross(v2);
	}
	public void addWindForcesForTriangle(Particle P1,Particle P2,Particle P3,Vector3f direction)
	{
		Vector3f normal = calcTriangleNormal(P1,P2,P3);
		Vector3f tempd = normal;
		Vector3f d = tempd.normalize();
		Vector3f force = normal.mul(d.dot(direction));
		P1.addForce(force);
		P2.addForce(force);
		P3.addForce(force);
	}
	public void drawTriangle(Particle P1, Particle P2, Particle P3)
	{
		Vector3f pos1 = P1.getPosition();
		Vector3f pos2 = P2.getPosition();
		Vector3f pos3 = P3.getPosition();
		Vector3f tempNorm1 = P1.getAccumulatedNormal();
		Vector3f tempNorm2 = P2.getAccumulatedNormal();
		Vector3f tempNorm3 = P3.getAccumulatedNormal();
		Vector3f norm1 = tempNorm1.normalize();
		Vector3f norm2 = tempNorm2.normalize();
		Vector3f norm3 = tempNorm3.normalize();
		if(posVertices.isEmpty()){
			posVertices.add(pos1);
			posVertices.add(pos2);
			posVertices.add(pos3);
			posNormals.add(norm1);
			posNormals.add(norm2);
			posNormals.add(norm3);
		}
		else{
			if(!(posVertices.contains(pos1))){
				posNormals.add(norm1);
				posVertices.add(pos1);
			}
			if(!(posVertices.contains(pos2))){
				posVertices.add(pos2);
				posNormals.add(norm2);
			}
			if(!(posVertices.contains(pos3))){
				posVertices.add(pos3);
				posNormals.add(norm3);
			}
		}
		
//			ListIterator<Vector3f> itr = posVertices.listIterator();
//			boolean flag1 = true;
//			boolean flag2 = true;
//			boolean flag3 = true;
//			while(itr.hasNext())
//			{
//				Vector3f temp = itr.next();
//				if(temp.equals(pos1))
//					flag1=false;
//				if(temp.equals(pos2))
//					flag2=false;
//				if(!(temp.equals(pos3)))
//					flag3=false;
//			}
//			if(flag1)
//				itr.add(pos1);
//			if(flag2)
//				itr.add(pos2);
//			if(flag3)
//				itr.add(pos3);
//		
//		}
		
						
	}
	public void drawShaded()
	{
				
		for(Iterator<Particle> itr = particles.iterator();itr.hasNext();)
		{
			particle = itr.next();
			particle.setAccumulatedNormal(new Vector3f(0.0f,0.0f,0.0f));
			
		}
		

		//create smooth per particle normals by adding up all the (hard) triangle normals that each particle is part of
		for(int x = 0; x<width-1; x++)
		{
			for(int y=0; y<height-1; y++)
			{
				Vector3f normal = calcTriangleNormal(getParticle(x+1,y),getParticle(x,y),getParticle(x,y+1));
				getParticle(x+1,y).addAccumulatedNormal(normal);
				getParticle(x,y).addAccumulatedNormal(normal);
				getParticle(x,y+1).addAccumulatedNormal(normal);

				normal = calcTriangleNormal(getParticle(x+1,y+1),getParticle(x+1,y),getParticle(x,y+1));
				getParticle(x+1,y+1).addAccumulatedNormal(normal);
				getParticle(x+1,y).addAccumulatedNormal(normal);
				getParticle(x,y+1).addAccumulatedNormal(normal);
			}
		}

		
		for(int x = 0; x<width-1; x++)
		{
			for(int y=0; y<height-1; y++)
			{
				

				drawTriangle(getDrawParticle(x+1,y),getDrawParticle(x,y),getDrawParticle(x,y+1));
				drawTriangle(getDrawParticle(x+1,y+1),getDrawParticle(x+1,y),getDrawParticle(x,y+1));
			}
		}
	
	}
	public void timeStamp()
	{
		for(int i=0;i<CONSTRAINT_ITERATIONS;i++)
		{
			for(Iterator<Constraint> itr = constraints.iterator();itr.hasNext();) // iterate over all constraints several times
			{
				cons = itr.next();
				cons.satisfyConstraint(); // satisfy constraint.
			}
		}
		for(Iterator<Particle> itr = particles.iterator();itr.hasNext();)
		{
			particle = itr.next();
			particle.timeStamp();
			
		}
	}
	public void addForce(Vector3f direction)
	{
		for(Iterator<Particle> itr = particles.iterator();itr.hasNext();)
		{
			particle = itr.next();
			particle.addForce(direction);
			
		}
	}
	public void windForce(Vector3f direction)
	{
		for(int x = 0; x<width-1; x++)
		{
			for(int y=0; y<height-1; y++)
			{
				addWindForcesForTriangle(getParticle(x+1,y),getParticle(x,y),getParticle(x,y+1),direction);
				addWindForcesForTriangle(getParticle(x+1,y+1),getParticle(x+1,y),getParticle(x,y+1),direction);
			}
		}
	}
	public void ballCollision(Ball ball)
	{
		float radius = ball.getRadius();
		Vector3f center = ball.getPosition();
		for(Iterator<Particle> itr = particles.iterator();itr.hasNext();)
		{
			particle = itr.next();
			Vector3f temp = new Vector3f(0.0f,0.0f,0.0f);
			particle.getPosition().sub(center, temp);
			float l = temp.length();
			if(l<radius){
				particle.offsetPos(temp.normalize().mul(radius - l));
			}
		}
	}
}
