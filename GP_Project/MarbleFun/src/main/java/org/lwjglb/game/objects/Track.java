package org.lwjglb.game.objects;

import org.joml.Matrix3f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.MatrixOperations;
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
		setPosition((end.x + start.x)/2, (end.y + start.y) / 2, -20);
		setRotation(0, 0, -slope);
		setScale(trackLength / meshLength);
		Texture tex = new Texture(material.getTexturePath());
		
		Material mat = new Material(tex,0f);
		
		
		mesh.setMaterial(mat);

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
		boolean collision = false;
		// translate track and ball so that the origin is at the line start
		Vector3f s = new Vector3f(start.x, start.y, 1); Vector3f e = new Vector3f(end.x, end.y, 1);
		Vector3f ballPos = new Vector3f(ball.getPosition());
		ballPos.z = 1;
		Matrix3f t = new Matrix3f(1, 0, 0, 0 , 1, 0, -s.x, -s.y, 1);
		s = MatrixOperations.mult(t, s);
		e = MatrixOperations.mult(t, e);
		ballPos = MatrixOperations.mult(t, ballPos);
		//orthogonal projection of ball onto line
		Vector3f eTmp = new Vector3f(e);
		Vector3f point = eTmp.mul(ballPos.dot(e)/e.dot(e));
		float distance = (float) Math.sqrt(Math.pow(ballPos.x-point.x, 2)+Math.pow(ballPos.y-point.y, 2));
		float velocityAngle = 0;
		float normalAngle = 0;
		
		if (distance<=ball.getRadius()) {
			if (point.x<s.x-ball.getRadius() || point.x>e.x+ball.getRadius()) { // ball can't collide with track;
				collision = false;
			} else if (point.x>=s.x && point.x<=e.x) { // ball on track
				collision = true;
				normalAngle = 90 + slope;
				boolean aboveTrack = ballPos.y-point.y>=0;
				if (aboveTrack) {
					normalAngle = 90 + slope;
				} else {
					normalAngle = -90 + slope;
				}
			} else { // collision with end or start point of track
				Vector3f b = new Vector3f(ballPos.x, s.y, 1);
				if (point.x<s.x) { // at start
					distance = (float) Math.sqrt(Math.pow(ballPos.x-s.x, 2)+Math.pow(ballPos.y-s.y, 2));
					normalAngle = 180- (float) Math.toDegrees(Math.acos(b.dot(ballPos)/(b.length()*ballPos.length())));
				} else { // at end
					distance = (float) Math.sqrt(Math.pow(ballPos.x-e.x, 2)+Math.pow(ballPos.y-e.y, 2));
					normalAngle = (float) Math.toDegrees(Math.acos(b.dot(ballPos)/(b.length()*ballPos.length())));
				}
				if (distance<=ball.getRadius()) {
					collision = true;
				}
			}
		} else {
			collision = false;
			
		}
		if (collision) {	
			Vector3f v = new Vector3f(-ball.getVelocity().x, -ball.getVelocity().y, 0);
			Vector3f h = new Vector3f(1,0,0);
			velocityAngle = (float) Math.toDegrees(Math.acos(v.dot(h)/(v.length()*h.length())));
			velocityAngle = ball.getVelocity().y<0 ? -velocityAngle : velocityAngle;
			float phi = normalAngle - velocityAngle;
			float newVelocityAngle = normalAngle + phi;
			newVelocityAngle = (float) Math.toRadians(newVelocityAngle);
			ball.setVelocity(new Vector3f((float) Math.cos(newVelocityAngle)*v.length()*material.getDamping(), (float) -Math.sin(newVelocityAngle)*v.length()*material.getDamping(), ball.getVelocity().z));
			ball.setPosition(ball.getPosition().x+(float) Math.cos(newVelocityAngle)*0.01f*v.length(), ball.getPosition().y+(float) Math.sin(newVelocityAngle)*0.01f*v.length(), ball.getPosition().z);
		}
		return collision;
	}

}