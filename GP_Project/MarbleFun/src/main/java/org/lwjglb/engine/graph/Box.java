package org.lwjglb.engine.graph;

import org.joml.Vector3f;

public class Box {

	private Vector3f minX;
	private Vector3f minY;
	private Vector3f minZ;
	
	private Vector3f maxX;
	private Vector3f maxY;
	private Vector3f maxZ;
	
	
	public Box(Vector3f minX, Vector3f minY, Vector3f minZ, Vector3f maxX, Vector3f maxY, Vector3f maxZ){
		this.minX = minX;
		this.minY = minY;
		this.minZ = minZ;
		
		this.maxX = maxX;
		this.maxY = maxY;
		this.maxZ = maxZ;
	}
	
	
	public Vector3f getMinX(){
		return this.minX;
	}
	
	public Vector3f getMinY(){
		return this.minY;
	}
	
	public Vector3f getMinZ(){
		return this.minZ;
	}
	
	public Vector3f getMaxX(){
		return this.maxX;
	}
	
	public Vector3f getMaxY(){
		return this.maxY;
	}
	
	public Vector3f getMaxZ(){
		return this.maxZ;
	}
	
	public void setScale(float scale) {
		this.maxX = this.maxX.mul(scale);
		this.maxY = this.maxY.mul(scale);
		this.maxZ = this.maxZ.mul(scale);
		
		this.minX = this.minX.mul(scale);
		this.minY = this.minY.mul(scale);;
		this.minZ = this.minZ.mul(scale);;
    }
	  
}
