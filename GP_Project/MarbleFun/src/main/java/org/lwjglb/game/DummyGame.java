package org.lwjglb.game;

import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;
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
import org.lwjglb.game.objects.Wall;
import org.lwjglb.game.objects.Bowl;
import org.lwjglb.game.objects.Cloth;
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

    private Cloth cloth1;
    
    
    
    private static final float CAMERA_POS_STEP = 0.05f;

    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0, 0, 0);
        lightAngle = 10;
        cloth1=new Cloth(14,10,55,45);
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);
        
        float[]	positions	=	new	float[]{
				-0.5f,		0.5f,	-1f,
				-0.5f,	-0.5f,	-1f,
					0.5f,	-0.5f,	-1f,
					0.5f,		0.5f,	-1f,
        };
        int[]	indices	=	new	int[]{
				0,	1,	3,	3,	1,	2,
        };
        Mesh meshSquare = new Mesh(positions, indices);
        
        
        //cloth1.drawShaded();
        float reflectance = 1f;
        List<Vector3f> posVertices = cloth1.getPosVertices();
              
        
        Mesh mesh = OBJLoader.loadMesh(GameItemType.BALL);
        Mesh mesh3 = OBJLoader.loadMesh(GameItemType.BALL);
        Mesh trackMesh = OBJLoader.loadMesh(GameItemType.TRACK);
        Mesh mesh2 = OBJLoader.loadMesh(GameItemType.BOWL);
        
        Texture texture = new Texture("/textures/marbleRed.png");
        Texture tex = new Texture("/textures/grass.png");
        
        Material material = new Material(texture, reflectance);
        Material matBowl = new Material(tex, reflectance);

        mesh.setMaterial(material);
        mesh2.setMaterial(matBowl);
        trackMesh.setMaterial(material);
        mesh3.setMaterial(material);
        Material matCloth = new Material(new Vector3f(1.0f,0.0f,0.0f), reflectance);
        
        meshSquare.setMaterial(matCloth);
        //meshCloth.setMaterial(matCloth);
        Ball gameItem = new Ball(mesh);
        gameItem.setPosition(-3f, 4f, -1);
        gameItem.setScale(0.6f);
        
        Ball gameItem1 = new Ball(mesh3);
        gameItem1.setPosition(5f, 0f, -1);
        gameItem1.setScale(0.2f);
        
        
//       // GameItem squareItem= new GameItem(meshCloth);
//        squareItem.setPosition(-5f,0f,-1f);
//        squareItem.setScale(0.1f);
//        squareItem.setTrans(true);
        Track trackItem = new Track(trackMesh, -1,1,5,0, PhysicalMaterial.GRAS);
        
        Bowl bowlItem = new Bowl(mesh2,PhysicalMaterial.GOLD);
        bowlItem.setPosition(5f, -3f, -1);
        bowlItem.setScale(0.5f);
        
        
        
        gameItems = new GameItem[]{bowlItem, bowlItem,gameItem,gameItem1,trackItem};
         
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
        
          Ball ball1 = (Ball)gameItems[2];
          Track track = (Track)gameItems[4];
          Ball ball2 = (Ball)gameItems[3];
           
          ball1.updateGravity(interval*0.01f);
          ball2.updateGravity(interval*0.02f);
          
          if(track.isCollide(ball1)){
              ball1.collideWithWall();
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
