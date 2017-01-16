package org.lwjglb.engine.graph;

public class Box {

	private float minX;
	private float minY;
	private float minZ;
	
	private float maxX;
	private float maxY;
	private float maxZ;
	
	
	public Box(float minX, float minY, float minZ, float maxX, float maxY, float maxZ){
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	
	public float getMinX(){
		return this.minX;
	}
	
	public float getMinY(){
		return this.minY;
	}
	
	public float getMinZ(){
		return this.minZ;
	}
	
	public float getMaxX(){
		return this.maxX;
	}
	
	public float getMaxY(){
		return this.maxY;
	}
	
	public float getMaxZ(){
		return this.maxZ;
	}
	
	public void setScale(float scale) {
		this.maxX*=scale;
		this.maxY*=scale;
		this.maxZ*=scale;
		
		this.minX*=scale;
		this.minY*=scale;
		this.minZ*=scale;
    }
	  
}
