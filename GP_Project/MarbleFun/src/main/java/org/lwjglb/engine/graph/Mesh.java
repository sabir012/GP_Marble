package org.lwjglb.engine.graph;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private static final Vector3f DEFAULT_COLOUR = new Vector3f(1.0f, 1.0f, 1.0f);

    private final int vaoId;

    private final List<Integer> vboIdList;

    private final int vertexCount;
    
    private Material material;

    private Texture texture;

    private Vector3f colour;
    
    public Vector3f posOnObject;
    public Vector3f minXMaxY;
    public Vector3f maxXMinY;
    
    public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
    	
    	posOnObject = new Vector3f(positions[3],positions[4],positions[5]);
    	minXMaxY  = GetMinXMaxY(positions);
    	maxXMinY = GetMaxXMinY(positions);
    	
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
    
    public Vector3f GetMinXMaxY(float[] positions){
    	float minX = Integer.MAX_VALUE;
    	float y = 0;
    	float z = 0;
    	
    	for (int i=0;i<positions.length;i++){
    		if(i % 3 == 0){
    			if(minX>positions[i]){
    				minX = positions[i];
    				y = positions[i+1];
    				z = positions[i+2];
    			}
    		}
    	}
    	
    	return new Vector3f(minX,y,z);
    }
    
    public Vector3f GetMaxXMinY(float[] positions){
    	float maxX = Integer.MIN_VALUE;
    	float y = 0;
    	float z = 0;
    	
    	for (int i=0;i<positions.length;i++){
    		if(i % 3 == 0){
    			if(maxX<positions[i]){
    				maxX = positions[i];
    				y = positions[i+1];
    				z = positions[i+2];
    			}
    		}
    	}
    	
    	return new Vector3f(maxX,y,z);
    }
}
