package org.lwjglb.game;

import org.lwjglb.engine.GameEngine;
import org.lwjglb.engine.IGameLogic;
 
public class Main {
 
    public static void main(String[] args) {
    	System.setProperty("java.awt.headless", "true");
        try {
            boolean vSync = true;
            IGameLogic logic = new MarbleGame();
            GameEngine engine = new GameEngine("GAME", 1680, 1050, vSync, logic);
            engine.start();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}