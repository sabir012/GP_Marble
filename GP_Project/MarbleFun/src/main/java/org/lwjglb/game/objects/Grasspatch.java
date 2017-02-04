package org.lwjglb.game.objects;

import java.awt.HeadlessException;
import java.util.Arrays;

import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.GameItemType;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.Texture;

public class Grasspatch extends GameItem {
	private Grass grass[];

	public Grasspatch(float x, float y, float z, float length, float width, int number) {
		try {
			setMesh(generateMesh(length, width));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setPosition(x, y, z);

		grass = new Grass[number * (number-1)];
		Grass g;
		for (int i = 0; i < number; i++) {
			for (int j = 0; j < number-1; j++) {
				g = new Grass(x + length / number * j +(float)Math.random()*length / number/2.0f, y, z + width / number * i+(float)Math.random()*width / number/2.0f);
				g.setScale(length / number);
				grass[i * (number-1) + j] = g;

			}
		}
		Arrays.sort(grass);
	}

	private Mesh generateMesh(float length, float width) throws Exception {
		float[] positions = new float[] {
				// For text coords in top face
				// V8: V4 repeated
				0f, 0f, 0f,
				// V9: V5 repeated
				length, 0, 0f,
				// V10: V0 repeated
				0f, 0, width,
				// V11: V3 repeated
				length, 0, width, };
		float[] textCoords = new float[] {
				// For text coords in top face
				0.0f, 0.0f, 1f, 0.0f, 0f, 1f, 1f, 1.0f, };
		int[] indices = new int[] { 0, 2, 3, 1, 0, 3 };
		Mesh mesh = new Mesh(positions, textCoords, new float[0], indices, GameItemType.GRASS);
		Texture tex = new Texture("/textures/grass.png");
		Material mat = new Material(tex, 0f);
		mesh.setMaterial(mat);
		setMesh(mesh);
		return mesh;

	}

	@Override
	public boolean isCollide(Ball ball) {
		return false;
	}

	@Override
	public GameItem[] getSubItems() {
		return grass;
	}

	public void animate(float windstrength, float interval) {
		int direction = 1;
		if (grass!=null && grass.length>0) {
			direction = grass[0].getShearingDirection(grass[0].getShearing().x);
		}
		for (Grass g : grass) {
			float dShearing = windstrength * interval * (g.getMaxShearing());
			float shearing = g.getShearing().x;
			g.setShearing(shearing + dShearing * direction, g.getShearing().y);
		}
	}
}
