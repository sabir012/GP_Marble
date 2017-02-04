package org.lwjglb.game;

import org.joml.Vector2f;

import org.joml.Vector3f;
import org.lwjgl.openal.AL11;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.lwjglb.engine.GameEngine;
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
import org.lwjglb.engine.sound.SoundBuffer;
import org.lwjglb.engine.sound.SoundListener;
import org.lwjglb.engine.sound.SoundManager;
import org.lwjglb.engine.sound.SoundSource;
import org.lwjglb.engine.sound.Sounds;
import org.lwjglb.game.objects.Background;
import org.lwjglb.game.objects.Ball;
import org.lwjglb.game.objects.Grasspatch;
import org.lwjglb.game.objects.NumberCube;
import org.lwjglb.game.objects.NumbersTex;
import org.lwjglb.game.objects.PhysicalMaterial;
import org.lwjglb.game.objects.Track;
import org.lwjglb.engine.graph.PointLight;
import org.lwjglb.engine.graph.DirectionalLight;

public class MarbleGame implements IGameLogic {
	private static final float MOUSE_SENSITIVITY = 0.2f;

	private final Vector3f cameraInc;

	private final Renderer renderer;

	private final Camera camera;

	private final SoundManager soundManager;
	
	

	private GameItem[] gameItems;
	private Track[] tracks;
	private Ball[] balls;
	private NumberCube[] numberCubes;

	private Grasspatch[] grass;

	private Vector3f ambientLight;

	private PointLight pointLight;

	private DirectionalLight directionalLight;

	private float lightAngle;
	
	private double clock = 0;
	
	private int score = 0;

	private boolean updateActive = true;
	private Date preRunDate = new Date();
	private static final float CAMERA_POS_STEP = 0.05f;

	private List<Integer> scoredBalls = new ArrayList<>();
	
	public MarbleGame() {
		renderer = new Renderer();
		camera = new Camera();
		soundManager = new SoundManager();
		cameraInc = new Vector3f(0, 0, 0);
		lightAngle = 10;
	}

	@Override
	public void init(Window window) throws Exception {
		renderer.init(window);
		soundManager.init();

		float reflectance = 1f;

		Mesh mesh = OBJLoader.loadMesh(GameItemType.TRACK);
		Mesh trackMesh = OBJLoader.loadMesh(GameItemType.TRACK);
		Mesh mesh2 = OBJLoader.loadMesh(GameItemType.BALL);

		Texture texture = new Texture("/textures/marbleRed.png");

		Material material = new Material(texture, reflectance);

		mesh.setMaterial(material);
		mesh2.setMaterial(material);
		trackMesh.setMaterial(material);

		grass = new Grasspatch[] { new Grasspatch(-60f, -20f, -140f, 120f, 120f, 8) };

		balls = new Ball[] {
				new Ball(OBJLoader.loadMesh(GameItemType.BALL), 0.3f, new Vector3f(), PhysicalMaterial.STEEL, -18f, 12f,
						-20),
				new Ball(OBJLoader.loadMesh(GameItemType.BALL), 0.35f, new Vector3f(), PhysicalMaterial.GOLD, -16f,
						11.5f, -20),
				new Ball(OBJLoader.loadMesh(GameItemType.BALL), 0.39f, new Vector3f(), PhysicalMaterial.WOOD, -15f,
						12.5f, -20) };

		tracks = new Track[] {
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -18, 10, -12, 9f, PhysicalMaterial.WOOD),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -12f, 9f, -8f, 5f, PhysicalMaterial.WOOD),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -9f, 6, -3f, 5, PhysicalMaterial.GOLD),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), 0f, 5.5f, 12, 5.5f, PhysicalMaterial.STEEL),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), 15f, 12f, 16f, -1f, PhysicalMaterial.PLASTIC),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), 8f, 0f, 18f, 3f, PhysicalMaterial.PLASTIC),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -6f, 0f, 4, 0f, PhysicalMaterial.STEEL),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -14f, 3f, -5.5f, 0f, PhysicalMaterial.WOOD),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -14f, 9f, -13f, 0f, PhysicalMaterial.PLASTIC),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), 6f, -3, 16f, -3, PhysicalMaterial.GOLD),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), 13f, -6, 17.5f, 2.5f, PhysicalMaterial.WOOD),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), 0f, -6, 6f, -3, PhysicalMaterial.GOLD),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -10f, -7.5f, 0f, -6, PhysicalMaterial.GOLD),
				// bucket
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -12.5f, -12f, -9.5f, -7.5f, PhysicalMaterial.GRAS),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -16f, -7.5f, -14f, -11, PhysicalMaterial.GRAS),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -16f, 0, -15.5f, -8.5f, PhysicalMaterial.PLASTIC),
				new Track(OBJLoader.loadMesh(GameItemType.TRACK), -17f, 0, -11f, 0, PhysicalMaterial.PLASTIC)};
		
		numberCubes = new NumberCube[] {
				new NumberCube(10, -8, -19, NumbersTex.ZERO, NumbersTex.ZERO),
				new NumberCube(12, -8, -19, NumbersTex.ZERO, NumbersTex.ZERO)
		};

		gameItems = new GameItem[tracks.length + balls.length + grass.length + numberCubes.length+1];

		for (int i = 0; i < balls.length; i++) {
			gameItems[i] = balls[i];
		}
		for (int i = balls.length; i < balls.length + tracks.length; i++) {
			gameItems[i] = tracks[i - balls.length];
		}
		for (int i = balls.length + tracks.length; i < balls.length + tracks.length + grass.length; i++) {
			gameItems[i] = grass[i - balls.length - tracks.length];
		}
		for (int i = balls.length + tracks.length + grass.length; i < balls.length + tracks.length + grass.length + numberCubes.length; i++) {
			gameItems[i] = numberCubes[i - balls.length - tracks.length - grass.length];
		}
		
		Background background = new Background(0, 50, -300, 0.185f, 1920, 1080);
		for (int i = gameItems.length-1; i >0 ; i--) {
			gameItems[i] = gameItems[i-1];
		}
		gameItems[0] = background;

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

		// Sound
		this.soundManager.init();
		this.soundManager.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
		setupSounds();
	}

	private void setupSounds() throws Exception {
		SoundBuffer backSound = new SoundBuffer("/sounds/backMusic.ogg");
		soundManager.addSoundBuffer(backSound);
		SoundSource sourceBack = new SoundSource(true, true);
		sourceBack.setBuffer(backSound.getBufferId());
		soundManager.addSoundSource(Sounds.MUSIC.toString(), sourceBack);

		SoundBuffer soundCollision = new SoundBuffer("/sounds/collision.ogg");
		soundManager.addSoundBuffer(soundCollision);
		SoundSource sourcebuffCollision = new SoundSource(false, true);
		sourcebuffCollision.setBuffer(soundCollision.getBufferId());
		soundManager.addSoundSource(Sounds.BEEP.toString(), sourcebuffCollision);

		soundManager.setListener(new SoundListener(new Vector3f(0, 0, 0)));

		sourceBack.play();
	}

	@Override
	public void input(Window window, MouseInput mouseInput) {
		if (GameEngine.ENABLECAMERA) {
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
		}
		if (window.isKeyPressed(GLFW_KEY_B)) { // Working period 3
														// seconds
			try {
				updateActive = false;
				Calendar calobj = Calendar.getInstance();
				Date date = calobj.getTime();

				long secondsBetween = (date.getTime() - preRunDate.getTime()) / 1000;

				if (secondsBetween > 5) {
					preRunDate = date;
					Ball ball = new Ball(OBJLoader.loadMesh(GameItemType.BALL), 0.3f, new Vector3f(),
							PhysicalMaterial.STEEL, -17.5f, 12f, -20);

					Ball[] newBalls = new Ball[balls.length + 1];
					newBalls[newBalls.length - 1] = ball;
					System.arraycopy(balls, 0, newBalls, 0, balls.length);
					balls = newBalls;

					GameItem[] newGameItems = new GameItem[gameItems.length+1];
					System.arraycopy(gameItems, 0, newGameItems, 0, gameItems.length);
					newGameItems[newGameItems.length - 1] = ball;
					gameItems = newGameItems;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				updateActive = true;
			}
		}
		/*
		 * float lightPos = pointLight.getPosition().z; if
		 * (window.isKeyPressed(GLFW_KEY_N)) { this.pointLight.getPosition().z =
		 * lightPos + 0.1f; } else if (window.isKeyPressed(GLFW_KEY_M)) {
		 * this.pointLight.getPosition().z = lightPos - 0.1f; }
		 */
	}

	@Override
	public void update(float interval, MouseInput mouseInput) {
		clock += interval;
		if (updateActive) {
			// Update camera position
			camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP,
					cameraInc.z * CAMERA_POS_STEP);

			// Update camera based on mouse
			if (mouseInput.isRightButtonPressed()) {
				Vector2f rotVec = mouseInput.getDisplVec();
				camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
			}

			float windstrength = 1.5f;
			for (Grasspatch grass : grass) {
				grass.animate(windstrength, interval);
			}
			

			for (int i = 0; i < balls.length; i++) {
				balls[i].updateGravity(interval * 0.7f);
				for (int j = i + 1; j < balls.length; j++) {
					if (balls[i].isCollide(balls[j])) {
						balls[i].handleBallCollision(balls[j]);
						if (clock-balls[i].getLastCollision()>0.1) {
							soundManager.playSoundSource(Sounds.BEEP.toString());
						}
						balls[i].setLastColliosion(clock);
					}
				}
				
				Vector3f position = balls[i].getPosition();
				if(position.x>=-14 && position.x<=-12.5 && position.y<=-11.3F){
					if(!this.scoredBalls.contains(i)){
						setScore(score+=1);
						scoredBalls.add(i);
					}
				}
			}

			for (Track track : tracks) {
				for (Ball ball : balls) {
					if (track.isCollide(ball)) {
						Vector2f v = new Vector2f(ball.getVelocity().x, ball.getVelocity().y);
						if (clock-ball.getLastCollision()>0.1) {
							soundManager.playSoundSource(Sounds.BEEP.toString());
						}
						ball.setLastColliosion(clock);
					}
				}
			}
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
	
	private void setScore(int score) {
		score %=100;
		NumbersTex[] nts = NumbersTex.values();
		for (int i = 0; i < numberCubes.length; i++) {
			numberCubes[i].getMesh().cleanUp();
		}
		numberCubes[0] = new NumberCube(10, -8, -19, nts[score/10], numberCubes[0].getCurrentTex());
		numberCubes[1] = new NumberCube(10, -8, -19, nts[score%10], numberCubes[1].getCurrentTex());
	}
}
