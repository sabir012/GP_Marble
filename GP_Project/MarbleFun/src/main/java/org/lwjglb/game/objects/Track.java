package org.lwjglb.game.objects;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.graph.Box;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Texture;

public class Track extends GameItem {

	private float slope; // 0 to 90
	private PhysicalMaterial material;

	private Vector2f start, end;

	private Box box;

	public Track(Mesh mesh, float xstart, float ystart, float xend, float yend, PhysicalMaterial material) throws Exception {
		super(mesh);
		this.material = material;
		
		start = new Vector2f(xstart, ystart);
		end = new Vector2f(xend, yend);
		slope = (float) Math.toDegrees(Math.atan((end.y-start.y)/(end.x-start.x)));
		
		this.box = mesh.getBox();
		
		float meshLength = Math.abs(box.getMaxX().x - box.getMinX().x);
		float trackLength = (float) Math.sqrt(Math.pow(xstart - xend, 2) + Math.pow(ystart - yend, 2));
		setPosition((end.x + start.x)/2, (end.y + start.y) / 2, -1);
		setRotation(0, 0, -slope);
		setScale(trackLength / meshLength);
		System.out.println(material.getTexturePath());
		
		Material mat = new Material();
		mesh.setMaterial(mat);

        
		this.material = PhysicalMaterial.GRAS;
	}

	public Track(Mesh mesh, PhysicalMaterial material) {
		super(mesh);

		this.material = material;

		slope = 0;
	}

	public void setSlope(float slope) {
		this.slope = slope;
	}

	@Override
	public void setScale(float scale) {
		super.setScale(scale);
		this.box.setScale(scale);
	}

	public float getSlope() {
		return slope;
	}

	public PhysicalMaterial getMaterial() {
		return this.material;
	}

	@Override
	public boolean isCollide(Ball ball) {
		Vector2f ballPos = new Vector2f(ball.getPosition().x, ball.getPosition().y);
		float normalLength = (float) Math.sqrt((end.x-start.x)*(end.x-start.x)+(end.y-start.y)*(end.y-start.y));
	    float distance =  Math.abs((ballPos.x-start.x)*(end.y-start.y)-(ballPos.y-start.y)*(end.x-start.x))/normalLength;
		return distance <= ball.getRadius();
	}

}