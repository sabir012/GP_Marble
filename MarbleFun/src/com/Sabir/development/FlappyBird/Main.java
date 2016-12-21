package com.Sabir.development.FlappyBird;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import com.Sabir.development.FlappyBird.graphics.Shader;
import com.Sabir.development.FlappyBird.input.Input;
import com.Sabir.development.FlappyBird.level.Level;
import com.Sabir.development.FlappyBird.math.Matrix4f;

public class Main implements Runnable{

	private int height = 600;
	private int width = 800;

	private long window;
	
	private boolean running = false;
	
	private Thread thread;
	
	private Level level;
	
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
		 
		 glfwWindowHint(GLFW_SAMPLES, 4);
		 glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		 glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		 glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
		 glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		 glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		 window = glfwCreateWindow(width, height, "Flappy Bird", NULL, NULL);
		 
		 if(window == NULL){
			 //TODO: handle it
			 return;
		 }
		 
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(
				window,
				(vidmode.width() - width) / 2,
				(vidmode.height() - height) / 2
			);
		 
		 glfwSetKeyCallback(window, new Input());
		 
		 glfwMakeContextCurrent(window);
		 GL.createCapabilities();
		 glfwShowWindow(window);
		 
		 glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		 glEnable(GL_DEPTH_TEST);
		 System.out.println("OpenGL: "+glGetString(GL_VERSION));
		 
		 Shader.loadAll();
		 
		 Shader.BG.enable();
		 Matrix4f pr_matrix = Matrix4f.orthographic(-10.0f, 10.0f, 10.0f * (-9.0f)/16.0f, 10.0f * (9.0f)/16.0f, -1.0f, 1.0f);
		 Shader.BG.setUniformMat4f("pr_matrix", pr_matrix);
		 Shader.BG.disable();
		 
		 level = new Level();
	}
	
	private void update(){
		glfwPollEvents();
		
	}
	
	private void render(){
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		glfwSwapBuffers(window);
	}
	
	
	public static void main(String[] args) {
		new Main().start();

	}

}
