package org.lwjglb.game.objects;

import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.graph.Mesh;

public class GUIobject extends GameItem{
	
	public GUIobject(Mesh mesh){
		super(mesh);
	}

	@Override
	public boolean isCollide(Ball ball) {
		// TODO Auto-generated method stub
		return false;
	}
	
}