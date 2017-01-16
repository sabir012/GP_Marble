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
        
        Mesh mesh = OBJLoader.loadMesh("/models/marble1.obj",GameItemType.Ball);
        Mesh trackMesh = OBJLoader.loadMesh("/models/Track.obj",GameItemType.Track);
        Mesh mesh2 = OBJLoader.loadMesh("/models/marble1.obj",GameItemType.Ball);
        
        Texture texture = new Texture("/textures/marbleRed.png");
        
        Material material = new Material(texture, reflectance);

        mesh.setMaterial(material);
        mesh2.setMaterial(material);
        trackMesh.setMaterial(material);

        Ball gameItem = new Ball(mesh);
        gameItem.setPosition(0f, 0.5f, -1);
        gameItem.setScale(0.05f);
        
        Track trackItem = new Track(trackMesh);
        trackItem.setPosition(0f, -0.3f, -1);
        trackItem.setScale(0.1f);
        
        gameItems = new GameItem[]{gameItem,trackItem};
         
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

        
             Ball ball1 = (Ball)gameItems[0];
             Track track = (Track)gameItems[1];
             
//             Vector2f force = ball1.calculateGravityForce(track.getSlope()).mul(0.01f);
//             force = force.add(ball1.calculateTrackFrictionForce(track));
//             ball1.updateVelocity(force, interval);
             
             //force = ball2.calculateGravityForce(track.getSlope());
             //force = force.add(ball2.calculateTrackFrictionForce(track));
             //ball2.updateVelocity(force, interval);
           
             ball1.updateGravity(interval*0.1f);
             
             System.out.println("Ball: {"+ball1.getPosition().x+", "+ball1.getPosition().y+", "+ball1.getPosition().z);
             System.out.println("Track: {"+track.getPosition().x+", "+track.getPosition().y+", "+track.getPosition().z);
             
            if(!(track.isCollide(ball1))){
            	//gameItems[0].setPosition(0,0,-1);
            }
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
