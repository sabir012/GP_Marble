package com.Sabir.development.FlappyBird;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.Sabir.development.FlappyBird.input.Input;

public class Main implements Runnable{

	private int height = 600;
	private int width = 800;

	private long window;
	
	private boolean running = false;
	
	private Thread thread;
	
	
	public void start(){
		running = true;
		//thread = new Thread(this,"Game");
		//thread.start();
		
		run();
	}
	
	public void run(){
		init();
		
		while(running){
			update();
			render();
			
			if(glfwWindowShouldClose(window)){
				running = false;
			}
		}
	}
	
	
	private void init(){
		 if(!glfwInit()){
			 //TODO: handle it
			 return;
		 }
		 
		 glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		 window = glfwCreateWindow(width, height, "Flappy Bird", NULL, NULL);
		 
		 if(window == NULL){
			 //TODO: handle it
			 return;
		 }
		 
		 GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		 glfwSetWindowPos(window, (vidmode.WIDTH - width)/2, (vidmode.HEIGHT - height)/2);
		 
		 glfwSetKeyCallback(window, new Input());
		 
		 glfwMakeContextCurrent(window);
		 GL.createCapabilities();
		 glfwShowWindow(window);
		 
		 glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		 glEnable(GL_DEPTH_TEST);
		 System.out.println("OpenGL: "+glGetString(GL_VERSION));
	}
	
	private void update(){
		glfwPollEvents();
		
	}
	
	private void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glfwSwapBuffers(window);
	}
	
	
	public static void main(String[] args) {
		new Main().start();

	}

}
