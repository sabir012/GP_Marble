
import static org.lwjgl.glfw.GLFW.*;

public class Main {

	public Main()
	{
		if(!glfwInit()){
			System.out.println("GLFW failed to intialize..");
			System.exit(1);
		}
		
		long win = glfwCreateWindow(640, 480, "Marble Fun", 0, 0);
		
		glfwShowWindow(win);
		
		while(!glfwWindowShouldClose(win)){
			glfwPollEvents();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Main();
	}

}
