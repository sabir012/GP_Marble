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
		Texture tex = new Texture(material.getTexturePath());
		Material mat = new Material();
		mesh.setMaterial(mat);

        
		this.material = PhysicalMaterial.GRAS;
	}

	public Track(Mesh mesh, PhysicalMaterial material) {
		super(mesh);

		this.material = material;

		slope = 0;
	}

	public boolean isCollide(Ball ball, float dt) {
		Vector2f ballPos = new Vector2f(ball.getPosition().x, ball.getPosition().y);
		float normalLength = (float) Math.sqrt((end.x-start.x)*(end.x-start.x)+(end.y-start.y)*(end.y-start.y));
	    float distance =  Math.abs((ballPos.x-start.x)*(end.y-start.y)-(ballPos.y-start.y)*(end.x-start.x))/normalLength;
		return distance <= ball.getRadius();
		// // get box closest point to sphere center by clamping
		//// Vector3f ballPosition = ball.getPosition();
		////
		//// float x = Math.max(box.getMinX().x, Math.min(ballPosition.x,
		// box.getMaxX().x));
		//// float y = Math.max(box.getMinX().y, Math.min(ballPosition.y,
		// box.getMaxX().y));
		//// float z = Math.max(box.getMinX().z, Math.min(ballPosition.z,
		// box.getMaxX().z));
		////
		//// // this is the same as isPointInsideSphere
		//// double distance = Math.sqrt((x - ballPosition.x) * (x -
		// ballPosition.x) +
		//// (y - ballPosition.y) * (y - ballPosition.y) +
		//// (z - ballPosition.z) * (z - ballPosition.z));
		////
		//// return distance < ball.getRadius();
		//
		// Vector3f C = getPosition();
		// Vector3f E = box.getMinX();
		// Vector3f L = box.getMaxX();
		//
		// Vector3f d = L.sub(E); // Direction vector of ray, from start to end
		// )
		// Vector3f f= E.sub(C); //Vector from center sphere to ray start )
		//
		// Vector3f P = E.add(d.mul(dt));
		//
		// float distance = (float)Math.sqrt((P.x-C.x)*(P.x-C.x) +
		// (P.y-C.y)*(P.y-C.y)+(P.z-C.z)*(P.z-C.z));
		//
		// System.out.println(getPosition()+ " " + ball.getPosition());
		// System.out.println(E + " " +L);
		// System.out.println(distance+"---"+ball.getRadius());

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
		// TODO Auto-generated method stub
		return false;
	}

}