package org.lwjglb.game.objects;

import org.joml.Vector2f;

public enum NumbersTex {
	ZERO(1,4), ONE(0,0), TWO(0,1), THREE(0,2), FOUR(0,3), FIVE(0,4), SIX(1,0), SEVEN(1,1), EIGHT(1,2), NINE(1,3);
     
    private final int row;
    private final int pos;
    
    private final float width = 0.2f;
    private final float height = 0.5f;
    private final float offsetX=0;
    
    
    private NumbersTex(int row, int pos) {
    	this.row = row;
    	this.pos = pos;
    }
    
    public Vector2f getLT() {
    	return new Vector2f(pos*width+offsetX, row*height);
    }
    
    public Vector2f getLB() {
    	return new Vector2f(pos*width+offsetX, (row+1)*height);
    }
    
    public Vector2f getRT() {
    	return new Vector2f((pos+1)*width+offsetX, row*height);
    }
    
    public Vector2f getRB() {
    	return new Vector2f((pos+1)*width+offsetX, (row+1)*height);
    }
}
