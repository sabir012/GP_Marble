package org.lwjglb.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjglb.engine.GameItemType;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh { 
	private static final int X_MODE = 0;
	private static final int Y_MODE = 1;
	private static final int Z_MODE = 2;
	
    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    private final int vaoId;

    private final List<Integer> vboIdList;

    private final int vertexCount;
    
    private Material material;

    private Texture texture;

    private Vector3f colour;
    
    private Vector3f positionOnObject;
    
    private Box box;
    
    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices, GameItemType gameItemType) {
    	if(gameItemType == GameItemType.BALL){
    		positionOnObject = new Vector3f(positions[3],positions[4],positions[5]);
    	}
    	else{
    		box = new Box(this.GetMin(positions, X_MODE),this.GetMin(positions, Y_MODE),this.GetMin(positions, Z_MODE),
    			      this.GetMax(positions, X_MODE),this.GetMax(positions, Y_MODE),this.GetMax(positions, Z_MODE));
    	}
        colour = DEFAULT_COLOUR;
        vertexCount = indices.length;
        vboIdList = new ArrayList();

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Position VBO
        int vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer posBuffer = BufferUtils.createFloatBuffer(positions.length);
        posBuffer.put(positions).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        // Texture coordinates VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer textCoordsBuffer = BufferUtils.createFloatBuffer(textCoords.length);
        textCoordsBuffer.put(textCoords).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        // Vertex normals VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        FloatBuffer vecNormalsBuffer = BufferUtils.createFloatBuffer(normals.length);
        vecNormalsBuffer.put(normals).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

        // Index VBO
        vboId = glGenBuffers();
        vboIdList.add(vboId);
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isTextured() {
        return this.texture != null;
    }
    
    public Texture getTexture() {
        return this.texture;
    }
    
    public void setTexture(Texture texture) {
        this.texture = texture;
    }
    
    public void setColour(Vector3f colour) {
        this.colour = colour;
    }

    public Vector3f getColour() {
        return this.colour;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }
    
    public Vector3f getPositionOnObject(){
    	return this.positionOnObject;
    }
    
    public Box getBox(){
    	return this.box;
    }

    public void render() {
    	Texture texture = material.getTexture();
        if (texture != null) {
            // Activate firs texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }

        // Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the texture
        Texture texture = material.getTexture();
        if (texture != null) {
            texture.cleanup();
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
    
    public Vector3f GetMin(float[] positions,int mode){
    	float min = Integer.MAX_VALUE;
    	float x = 0; 
    	float y = 0; 
    	float z = 0;
    	
    	Vector3f result = new Vector3f();
    	
    	for (int i=0;i<positions.length;i++){
    		if(i % 3 == mode){
    			if(min > positions[i]){
    				min = positions[i];
    				if(mode == 1){
    					x = positions[i-1];
    					z = positions[i+1];
    				}
    				else if(mode == 0){
    					y = positions[i+1];
    					z = positions[i+2];
    				}
    				else if(mode == 2){
    					y = positions[i-1];
    					x = positions[i-2];
    				}
    			}
    		}
    	}
    	
		if(mode == 0){
    		result = new Vector3f(min,y,z);
    	}
		else if(mode == 1){
			result = new Vector3f(x,min,z);
		}
		else if(mode == 2){
			result = new Vector3f(x,y,min);
		}
    	
    	return result;
    }
    
    public Vector3f GetMax(float[] positions, int mode){
    	float max = Integer.MIN_VALUE;
    	float x = 0; 
    	float y = 0; 
    	float z = 0;
    	
    	Vector3f result=new Vector3f();;
    	
    	for (int i=0;i<positions.length;i++){
    		if(i % 3 == mode){
    			if(max < positions[i]){
    				max = positions[i];
    			}
    			
    			if(mode == 1){
					x = positions[i-1];
					z = positions[i+1];
				}
				else if(mode == 0){
					y = positions[i+1];
					z = positions[i+2];
				}
				else if(mode == 2){
					y = positions[i-1];
					x = positions[i-2];
				}
    		}
    	}
    	
		if(mode == 0){
    		result = new Vector3f(max,y,z);
    	}
		else if(mode == 1){
			result = new Vector3f(x,max,z);
		}
		else if(mode == 2){
			result = new Vector3f(x,y,max);
		}
		
    	return result;
    }
    
    public void deleteBuffers() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
