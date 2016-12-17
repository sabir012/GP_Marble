package Game.Sabir;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL;



public class Main {

	public Main()
	{
		if(!glfwInit()){
			System.out.println("GLFW failed to intialize..");
			System.exit(1);
		}
		
		long win = glfwCreateWindow(700, 580, "Marble Fun", 0, 0);
		
		glfwShowWindow(win);
		
		glfwMakeContextCurrent(win);
		GL.createCapabilities();
		
		glEnable(GL_TEXTURE_2D);
		
		float[] vertices = new float[]{
				-0.5f,0.5f,0,   //TOP LEFT       0
				 0.5f,0.5f,0,   //TOP RIGHT		 1
				 0.5f,-0.5f,0,  //BOTTOM RIGTH   2
				-0.5f,-0.5f,0,  //BOTTOM LEFT	 3
		};
		
		float[] texture = new float[]{
				0,0,
				1,0,
				1,1,
				0,1,
		};
		
		int[] indices = new int[]{
				0,1,2,
				2,3,0
		};
		
		Model model=new Model(vertices,texture,indices);
		
		Shader shader = new Shader("shader");
		
		//Texture tex = new Texture("./res/yoshi_transform.png");
		
		while(!glfwWindowShouldClose(win)){			
			if(glfwGetKey(win, GLFW_KEY_ESCAPE)==GL_TRUE){
				glfwSetWindowShouldClose(win, true);
			}

			glfwPollEvents();
			
			glClear(GL_COLOR_BUFFER_BIT);

			//tex.bind();
			shader.bind();
			model.render();
			
			glfwSwapBuffers(win);
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
