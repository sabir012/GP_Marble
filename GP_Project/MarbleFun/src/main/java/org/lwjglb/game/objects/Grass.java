package org.lwjglb.game.objects;

import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.GameItemType;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Texture;

public class Grass extends GameItem implements Comparable<Grass>{
	
	private float maxShearing;
	private int shearingDirection = 1;

	public Grass(float x, float y, float z) {
		try {
			setMesh(generateMesh());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setPosition(x, y, z);
		setRotation(0, 0, 0);
		maxShearing = (float) Math.random()/5*4;
	}

	private Mesh generateMesh() throws Exception {
		float[] positions = new float[] {
				// For text coords in top face
				// V8: V4 repeated
				0f, 0f, 0,
				// V9: V5 repeated
				1f, 0f, 0f,
				// V10: V0 repeated
				0f, 1f, 0f,
				// V11: V3 repeated
				1f, 1f, 0f, };
		float[] textCoords = new float[] {
				// For text coords in top face
				0.0f, 1f, 1f, 1f, 0f, 0f, 1f, 0f, };
		int[] indices = new int[] { 0, 2, 3, 1, 0, 3 };
		Mesh mesh = new Mesh(positions, textCoords, new float[0], indices, GameItemType.GRASS);
		Texture tex = new Texture(PhysicalMaterial.GRASSIDE.getTexturePath());
		Material mat = new Material(tex, 0f);
		mesh.setMaterial(mat);
		setMesh(mesh);
		return mesh;
	}
	
	public float getMaxShearing() {
		return maxShearing;
	}
	
	public int getShearingDirection(float shearing) {
		if (shearing>maxShearing) {
			shearingDirection = -1;
		} else if (shearing<-maxShearing) {
			shearingDirection = 1;
		}
		return shearingDirection;
	}

	@Override
	public boolean isCollide(Ball ball) {
		return false;
	}

	@Override
	public int compareTo(Grass o) {
		return getPosition().y<=o.getPosition().y ? 1 : -1;
	}
}