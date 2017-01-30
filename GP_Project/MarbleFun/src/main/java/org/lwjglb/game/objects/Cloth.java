package org.lwjglb.game.objects;
import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

public class Cloth{
	private int width;
	private int height;
	private float scale;
	private Particle particle;
	private Constraint cons;
	private Vector3f rotation;
	private boolean trans = false;
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private ArrayList<Constraint> constraints;
	private ArrayList<Float> vertices = new ArrayList<Float>();
	private List<Vector3f> posVertices = new ArrayList<>();
	private ArrayList<Float> normals = new ArrayList<Float>();
	private ArrayList<Integer> indices = new ArrayList<Integer>();
	private HashMap<Integer,Integer> check = new HashMap<Integer, Integer>();
	Ball ball;
	private int vaoId;
	private int vboId;
	private int colourVboId;
	private int value;
	
	public Cloth(float widthParticle, float heightParticle, int numParticlesWidth, int numParticlesHeight){
		rotation = new Vector3f(0);
		value = 0;
		constraints = new ArrayList<Constraint>();
		this.width=numParticlesWidth;
		this.height =numParticlesHeight;
		//System.out.println(width*height + "width height" + height);
		particles.ensureCapacity(width*height);
		ArrayList<Float> initVertices = new ArrayList<Float>(width*height);
		for(int i=0;i<width*height;i++)
			particles.add(null);
		for(int x=0; x<width; x++)
		{
			for(int y=0; y<height; y++)
			{
				Vector3f pos = new Vector3f(widthParticle * (x/(float)width),
								-heightParticle * (y/(float)height),
								0);
				particle = new Particle(pos);
				posVertices.add(particle.getPosition());
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
			Vector3f temp = new Vector3f();
			particle.offsetPos(temp.set(0.5f,0.0f,0.0f)); 
			particle.setMovable(false);
			particle.offsetPos(temp.set(-0.5f,0.0f,0.0f));
			particle = getParticle(width-1-i,0);
			particle.setMovable(false);
		}
	}
	
	public List<Vector3f> getPosVertices() {
		return posVertices;
	}

	public void setPosVertices(ArrayList<Vector3f> posVertices) {
		this.posVertices = posVertices;
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
	
	
	public ArrayList<Float> getVertices() {
		return vertices;
	}

	public void setVertices(ArrayList<Float> vertices) {
		this.vertices = vertices;
	}

	public ArrayList<Float> getNormals() {
		return normals;
	}

	public void setNormals(ArrayList<Float> normals) {
		this.normals = normals;
	}

	public ArrayList<Integer> getIndices() {
		return indices;
	}

	public void setIndices(ArrayList<Integer> indices) {
		this.indices = indices;
	}

	public Particle getDrawParticle(int x, int y){
		int ind = width*y+x;
		particle = particles.get(ind);
		
		if(check.containsKey(ind))
			indices.add(check.get(ind));
		else{
			indices.add(value);
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
		Vector3f d = normal.normalize();
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
		Float[]	vert	=	new	Float[]{
				pos1.x,	pos1.y,	pos1.z,
			pos2.x,	pos2.y,	pos2.z,
			pos3.x,pos3.y,pos3.z
		};
		//System.out.println("vertices   "+vert);
		for(int i=0;i<vert.length;i++){
			vertices.add(vert[i]);
			
		}
		System.out.println(pos1 + " " + pos2 + " " + pos3);
			
		Vector3f norm1 = P1.getAccumulatedNormal().normalize();
		Vector3f norm2 = P2.getAccumulatedNormal().normalize();
		Vector3f norm3 = P3.getAccumulatedNormal().normalize();
		Float[]	norm	=	new	Float[]{
				norm1.x, norm1.y,	norm1.z,
			norm2.x,	norm2.y,	norm2.z,
			norm3.x,norm3.y,norm3.z
		};
		for(int i=0;i<norm.length;i++)
			normals.add(norm[i]);
	
	}
	public void drawShaded()
	{
				
		for(Iterator<Particle> itr = particles.iterator();itr.hasNext();)
		{
			particle = itr.next();
			particle.setAccumulatedNormal(new Vector3f(0));
			
		}
		

		//create smooth per particle normals by adding up all the (hard) triangle normals that each particle is part of
		for(int x = 0; x<width-1; x++)
		{
			for(int y=0; y<height-1; y++)
			{
				Vector3f normal = calcTriangleNormal(getParticle(x+1,y),getParticle(x,y),getParticle(x,y+1));
				getParticle(x+1,y).addNormal(normal);
				getParticle(x,y).addNormal(normal);
				getParticle(x,y+1).addNormal(normal);

				normal = calcTriangleNormal(getParticle(x+1,y+1),getParticle(x+1,y),getParticle(x,y+1));
				getParticle(x+1,y+1).addNormal(normal);
				getParticle(x+1,y).addNormal(normal);
				getParticle(x,y+1).addNormal(normal);
			}
		}

		//GL11.glBegin(GL_TRIANGLES);
		for(int x = 0; x<width-1; x++)
		{
			for(int y=0; y<height-1; y++)
			{
				

				drawTriangle(getDrawParticle(x+1,y),getDrawParticle(x,y),getDrawParticle(x,y+1));
				drawTriangle(getDrawParticle(x+1,y+1),getDrawParticle(x+1,y),getDrawParticle(x,y+1));
			}
		}
		//GL11.glEnd();
	}
	public void timeStamp()
	{
		for(Iterator<Constraint> itr = constraints.iterator();itr.hasNext();) // iterate over all constraints several times
		{
			cons = itr.next();
			cons.satisfyConstraint(); // satisfy constraint.
			
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
			Vector3f temp = new Vector3f(0);
			particle.getPosition().sub(center, temp);
			float l = temp.length();
			if(l<radius){
				particle.offsetPos(temp.normalize().mul(radius - l));
			}
		}
	}
}
