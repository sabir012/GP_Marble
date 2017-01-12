package org.lwjglb.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import java.util.Random;

import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.IGameLogic;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;
import org.lwjglb.game.objects.Ball;
import org.lwjglb.engine.graph.PointLight;
import org.lwjglb.engine.graph.DirectionalLight;

public class DummyGame implements IGameLogic {

	//
	private final float GRAVITY = 8.0f;
	private final float BOX_SIZE = 12.0f;
	private final float TIME_BETWEEN_UPDATES = 0.0001f;
	private final int TIMER_MS = 25;
	
	private final int MAX_OCTREE_DEPTH = 6;
	private final int MIN_BALLS_PER_OCTREE = 3;
	private final int MAX_BALLS_PER_OCTREE = 6;
	//
	
    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;
    
    private Vector3f ambientLight;

    private PointLight pointLight;

    private DirectionalLight directionalLight;

    private float lightAngle;

    private static final float CAMERA_POS_STEP = 0.05f;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = -90;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        
        float reflectance = 1f;
        
        Mesh mesh = OBJLoader.loadMesh("/models/tennisball.obj");
        //Mesh mesh = OBJLoader.loadMesh("/models/compass.obj");
        Mesh mesh2 = OBJLoader.loadMesh("/models/soccer_ball.obj");
        
        Texture texture = new Texture("/textures/marbleRed.png");
     //   Texture texture2 = new Texture("/textures/rock.png");
        
        Material material = new Material(texture, reflectance);

        mesh.setMaterial(material);
        mesh2.setMaterial(material);
   //     mesh.setTexture(texture2);
        Ball gameItem = new Ball(mesh);
        gameItem.setScale(0.005f);
        gameItem.setPosition(0f, -1f, -5);
        
        mesh2.setTexture(texture);
        Ball gameItem2 = new Ball(mesh2);
        gameItem2.setScale(0.005f);
        gameItem2.setPosition(0f, 1.5f, -5);
        
        
        gameItems = new GameItem[]{gameItem,gameItem2};
        
        ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
        Vector3f lightColour = new Vector3f(1, 1, 1);
        Vector3f lightPosition = new Vector3f(0, 0, 1);
        float lightIntensity = 1.0f;
        pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
        PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
        pointLight.setAttenuation(att);

        lightPosition = new Vector3f(-1, 0, 0);
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
        float lightPos = pointLight.getPosition().z;
        if (window.isKeyPressed(GLFW_KEY_N)) {
            this.pointLight.getPosition().z = lightPos + 0.1f;
        } else if (window.isKeyPressed(GLFW_KEY_M)) {
            this.pointLight.getPosition().z = lightPos - 0.1f;
        }
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
        	
            // Update rotation angle
//            float rotation = gameItems[0].getRotation().x + 1f;
//            if (rotation > 360) {
//                rotation = 0;
//            }
//            gameItems[0].setRotation(rotation, 0, 0);
            
             Ball ball1 = (Ball)gameItems[0];
             Ball ball2 = (Ball)gameItems[1];
             
             ball1.setRadius(0.4f);
             ball2.setRadius(0.7f);
             
             boolean result = ball1.isCollide(ball2);
             System.out.println(result);
        
            gameItems[0].setPosition(0, gameItems[0].getPosition().y+0.005f, gameItems[0].getPosition().z);
            System.out.println(gameItems[0].getPosition().y);
    }

    @Override
    public void render(Window window) {
    	renderer.render(window, camera, gameItems, ambientLight, pointLight, directionalLight);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
