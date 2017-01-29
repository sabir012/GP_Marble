package org.lwjglb.game.objects;

import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.GameItemType;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;

public class NumberCube extends GameItem {
	
	private NumbersTex currentTex = NumbersTex.ZERO;

	public NumberCube(float x, float y, float z, NumbersTex newTex, NumbersTex oldTex) {
		setMesh(generateMesh(newTex));
		setPosition(x, y, z);
		Texture tex;
		try {
			tex = new Texture("/textures/numbers.png");
			Material mat = new Material(tex, 0f);

			getMesh().setMaterial(mat);
			
			setRotation(0, 0, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	private Mesh generateMesh(NumbersTex nt) {
		float[] positions = new float[] {
				1, -1, -1, 1, -1, 1, -1, -1, 1, -1, -1, -1, 1, 1, -1, 1, 1, 1, -1, 1, 1, -1, 1, -1, 1, -1, -1, 1, -1, -1, 1, -1, 1, 1, -1, 1, -1, -1, -1, -1, -1, -1, 1, 1, -1, 1, 1, -1, -1, -1, 1, -1, -1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1, -1, 1, 1, -1, 1, -1, -1, 1, -1
	        };
		float red = 0.15f;
		float black = 0.1f;
	        float[] textCoords = new float[]{
	        		0.8f, 0, // rt2
	        		nt.getRB().x, nt.getRB().y, // rb
	        		red, red, // not
	        		0.6f, 0, // lb2
	        		0.8f, 0.5f, // rb2
	        		nt.getRT().x, nt.getRT().y, // rt 
	        		red, red, 
	        		0.6f, 0.5f, //lt2
	        		black, black, 
	        		black, black, 
	        		black, black, 
	        		black, black, 
	        		black, black, 
	        		red, red, 
	        		black, black,
	        		black, black, 
	        		black, black, 
	        		nt.getLB().x, nt.getLB().y, //lb 
	        		black, black,
	        		black, black,
	        		nt.getLT().x, nt.getLT().y, // lt
	        		black, black,
	        		red, red,
	        		black, black,
	        };
	        float[] normals = new float[] {
	        		0, 0, -1, 0, 0, 1, -1, 0, 0, 0, 0, -1, 0, 0, -1, 0, 0, 1, -1, 0, 0, 0, 0, -1, 0, -1, 0, 1, 0, 0, 0, -1, 0, 1, 0, 0, 0, -1, 0, -1, 0, 0, 1, 0, 0, 0, 1, 0, 0, -1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, -1, 0, 0, 0, 1, 0
	        };
	        int[] indices = new int[]{
	        		10, 16, 12, 23, 21, 19, 14, 18, 11, 5, 20, 17, 2, 6, 22, 0, 3, 7, 8, 10, 12, 15, 23, 19, 9, 14, 11, 1, 5, 17, 13, 2, 22, 4, 0, 7
	        		};
	        return new Mesh(positions, textCoords, normals, indices, GameItemType.NUMBERCUBE);
	}

	public void display(int number) {
		getMesh().cleanUp();
		NumbersTex[] nts = NumbersTex.values();
		setMesh(generateMesh(nts[number]));
	}

	@Override
	public boolean isCollide(Ball ball) {
		return false;
	}
	
	public NumbersTex getCurrentTex() {
		return currentTex;
	}

}
