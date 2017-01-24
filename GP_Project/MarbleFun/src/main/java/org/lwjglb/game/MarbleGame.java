package org.lwjglb.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.util.Random;

import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.GameItemType;
import org.lwjglb.engine.IGameLogic;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.particleSystem;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;
import org.lwjglb.game.objects.Ball;
import org.lwjglb.game.objects.Particle;
import org.lwjglb.game.objects.PhysicalMaterial;
import org.lwjglb.game.objects.Track;
import org.lwjglb.engine.graph.PointLight;
import org.lwjglb.engine.graph.DirectionalLight;

public class MarbleGame implements IGameLogic {
	
	private particleSystem sph;

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;
    private Track[] tracks;
    
    private Vector3f ambientLight;

    private PointLight pointLight;

    private DirectionalLight directionalLight;

    private float lightAngle;

    private static final float CAMERA_POS_STEP = 0.05f;

    public MarbleGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = 10;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        init_sph();
        float reflectance = 1f;
        
   /*     Mesh mesh = OBJLoader.loadMesh(GameItemType.BALL);
        
        Texture texture = new Texture("/textures/marbleRed.png");
        
        Material material = new Material(texture, reflectance);
        
        mesh.setMaterial(material);


        Ball gameItem = new Ball(mesh,0.3f, new Vector3f(), PhysicalMaterial.STEEL);
        gameItem.setPosition(-5f, 4.5f, -10);*/

     
    //    gameItems[0] = gameItem;
         
        ambientLight = new Vector3f(0.6f, 0.6f, 0.6f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

        lightPosition = new Vector3f(0, 1, 1);
        lightColour = new Vector3f(1, 1, 1);
        directionalLight = new DirectionalLight(lightColour, lightPosition, lightIntensity);
        
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
  /*      float lightPos = pointLight.getPosition().z;
        if (window.isKeyPressed(GLFW_KEY_N)) {
            this.pointLight.getPosition().z = lightPos + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_M)) {
            this.pointLight.getPosition().z = lightPos - 0.1f;
        }*/
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }
        
      /*    Ball ball1 = (Ball)gameItems[0];
          Ball ball2 = (Ball)gameItems[1];
          
          ball1.updateGravity(interval*0.05f);
          ball2.updateGravity(interval*0.05f);
          
          if(ball1.isCollide(ball2)){
        	  ball1.handleBallCollision(ball2);
          }
          
          for (Track track : tracks) {
        	  if (track.isCollide(ball1)) {
        		 System.out.println("Collided Ball1");
        	  }
        	  if (track.isCollide(ball2)) {
         		 System.out.println("Collided Ball2");
         	  }
          }*/
    }

    @Override
    public void render(Window window) {
    	sph.animation();
    	renderer.render(window, camera, gameItems, ambientLight, pointLight, directionalLight, sph);
    	
    //	display_particle();
    }
    
    public void init_sph() throws Exception{
    //	Mesh mesh = OBJLoader.loadMesh(GameItemType.BALL);
    	sph=new particleSystem();
    	sph.init_system();
    }
    
    

    @Override
    public void cleanup() {
        renderer.cleanup();
    /*    for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }*/
    }
}
