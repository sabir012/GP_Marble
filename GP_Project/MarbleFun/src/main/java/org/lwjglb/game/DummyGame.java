package org.lwjglb.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import java.util.Random;

import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.GameItemType;
import org.lwjglb.engine.IGameLogic;
import org.lwjglb.engine.MouseInput;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Camera;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;
import org.lwjglb.game.objects.Ball;
import org.lwjglb.game.objects.PhysicalMaterial;
import org.lwjglb.game.objects.Track;
import org.lwjglb.engine.graph.PointLight;
import org.lwjglb.engine.graph.DirectionalLight;

public class DummyGame implements IGameLogic {


    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;
    
    private Vector3f ambientLight;

    private PointLight pointLight;

    private DirectionalLight directionalLight;

    private float lightAngle;
    
    private Hud hud;

    private static final float CAMERA_POS_STEP = 0.05f;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = 10;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        
        float reflectance = 1f;
        
        Mesh mesh = OBJLoader.loadMesh(GameItemType.BALL);
        Mesh trackMesh = OBJLoader.loadMesh(GameItemType.TRACK);
        Mesh mesh2 = OBJLoader.loadMesh(GameItemType.TRACK);
        
        Texture texture = new Texture("/textures/marbleRed.png");
        
        Material material = new Material(texture, reflectance);

        mesh.setMaterial(material);
        mesh2.setMaterial(material);
        trackMesh.setMaterial(material);

        Ball gameItem = new Ball(mesh,0.6f, new Vector3f(), PhysicalMaterial.STEEL);
        gameItem.setPosition(-2.2f, 4f, -10);
        
        Track trackItem = new Track(trackMesh, -2.5f,1,5,-1, PhysicalMaterial.GRAS);  
        Track trackItem2 = new Track(trackMesh, 0f,-12,8,-3, PhysicalMaterial.GRAS);
        
        gameItems = new GameItem[]{gameItem,trackItem,trackItem2};
         
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
        
     // Create HUD
        hud = new Hud();
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
        
          Ball ball1 = (Ball)gameItems[0];
          Track track = (Track)gameItems[1];
          Track track2 = (Track)gameItems[2];
          
          ball1.updateGravity(interval*0.1f);
          
          if(track.isCollide(ball1)){
        	  System.out.println("Upper track");
           }
          else if(track2.isCollide(ball1)){
        	  System.out.println("Below track");
          }
    }

    @Override
    public void render(Window window) {
    	hud.updateSize(window);
    	renderer.render(window, camera, gameItems, ambientLight, pointLight, directionalLight, hud);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }
}
