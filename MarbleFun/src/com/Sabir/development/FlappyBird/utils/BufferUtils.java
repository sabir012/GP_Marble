package com.Sabir.development.FlappyBird.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {

	public BufferUtils(){
		
	}
	
	public static ByteBuffer createByteBuffer(byte[] array){
		ByteBuffer buffer = ByteBuffer.allocate(array.length).order(ByteOrder.nativeOrder());
		buffer.put(array);
		buffer.flip();
		return buffer;
	}
	
	public static FloatBuffer createFloatBuffer(float[] array){
		FloatBuffer buffer = ByteBuffer.allocate(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		buffer.put(array);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer createFloatBuffer(int[] array){
		IntBuffer buffer = ByteBuffer.allocate(array.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
		buffer.put(array);
		buffer.flip();
		return buffer;
	}
}
