package org.lwjglb.game;

import java.awt.Font;
import org.joml.Vector3f;
import org.lwjglb.engine.GameItem;
import org.lwjglb.engine.GameItemType;
import org.lwjglb.engine.IHud;
import org.lwjglb.engine.TextItem;
import org.lwjglb.engine.Window;
import org.lwjglb.engine.graph.FontTexture;
import org.lwjglb.engine.graph.Material;
import org.lwjglb.engine.graph.Mesh;
import org.lwjglb.engine.graph.OBJLoader;
import org.lwjglb.engine.graph.Texture;
import org.lwjglb.game.objects.GUIobject;

public class Hud implements IHud {

    private static final Font FONT = new Font("Arial", Font.PLAIN, 20);

    private static final String CHARSET = "ISO-8859-1";

    private final GameItem[] gameItems;

//    private final TextItem statusTextItem;

    private final GUIobject gameItem;

    public Hud() throws Exception {
    //    FontTexture fontTexture = new FontTexture(FONT, CHARSET);
    //    this.statusTextItem = new TextItem(statusText, fontTexture);
    //    this.statusTextItem.getMesh().getMaterial().setColour(new Vector3f(1, 1, 1));

        // Create compass
        Mesh mesh = OBJLoader.loadMesh(GameItemType.HUD1);
        Texture texture = new Texture("/textures/wood2.png");
        Material material = new Material(texture);
      //  material.setColour(new Vector3f(1, 0, 0));
        mesh.setMaterial(material);
        this.gameItem = new GUIobject(mesh);
        gameItem.setScale(30.0f);
        
      
        // Rotate to transform it to screen coordinates
        gameItem.setRotation(0f, 0f, 180f);

        // Create list that holds the items that compose the HUD
        gameItems = new GameItem[]{gameItem};
    }

  /*  public void setStatusText(String statusText) {
        this.statusTextItem.setText(statusText);
    }
    */
    public void rotateCompass(float angle) {
        this.gameItem.setRotation(0, 0, 180 + angle);
    }

    @Override
    public GameItem[] getGameItems() {
        return gameItems;
    }
   
    public void updateSize(Window window) {
        this.gameItem.setPosition(window.getWidth() - 70f, 50f, 0);
        System.out.println(this.gameItem.getPosition());
        System.out.println(this.gameItem.getScale());
    }
}