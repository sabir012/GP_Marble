package org.lwjglb.game;

import java.awt.Font;
import org.joml.Vector3f;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.GameItemType;
import org.lwjglb.engine.IHud;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;
import org.lwjglb.game.objects.GUIobject;

public class Hud implements IHud {

    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);

    private static final String CHARSET = "ISO-8859-1";

    private final GameItem[] gameItems;
    
    private GUIobject panelHUD, trackHUD;

//    private final TextItem statusTextItem;

    public Hud() throws Exception {
    //    FontTexture fontTexture = new FontTexture(FONT, CHARSET);
    //    this.statusTextItem = new TextItem(statusText, fontTexture);
    //    this.statusTextItem.getMesh().getMaterial().setColour(new Vector3f(1, 1, 1));
    	
    	//Create back panel
    	Mesh panelMesh = OBJLoader.loadMesh(GameItemType.panelHUD);
        Texture panelTexture = new Texture("/textures/marbleRed.png");
        Material panelMaterial = new Material(panelTexture);
        panelMesh.setMaterial(panelMaterial);
        panelHUD = new GUIobject(panelMesh);
        panelHUD.setScale(50.0f);
      
        // Rotate to transform it to screen coordinates
        panelHUD.setRotation(0f, 0f, 180f);
    	

        // Create track
        Mesh trackMesh = OBJLoader.loadMesh(GameItemType.trackHUD);
        Texture trackTexture = new Texture("/textures/wood2.png");
        Material trackMaterial = new Material(trackTexture);
        trackMesh.setMaterial(trackMaterial);
        trackHUD = new GUIobject(trackMesh);
        trackHUD.setScale(30.0f);
      
        // Rotate to transform it to screen coordinates
        trackHUD.setRotation(0f, 0f, 180f);

        // Create list that holds the items that compose the HUD
        gameItems = new GameItem[]{panelHUD, trackHUD};
    }

  /*  public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }
    */
    public void rotateCompass(GUIobject gameItem, float angle) {
        gameItem.setRotation(0, 0, 180 + angle);
    }
    @Override
    public GameItem[] getGameItems() {
        return gameItems;
    }
   
    public void updateSize(Window window) {
        this.panelHUD.setPosition(window.getWidth() - 70f, 50f, 0);
        this.trackHUD.setPosition(window.getWidth() - 70f, 50f, 0);
    }
}