package org.lwjglb.game.objects;


public enum Material {
	STEEL(7850, 0.001f, "/textures/steel.png"), GOLD(19320, 0.001f, "/textures/gold.png"), 
    WOOD(600, 0.05f, "/textures/wood.png"), PLASTIC(950, 0.03f, "plastic.jpg"), GRAS(0, 0.1f, "/textures/grass.png");
     
    private final int mass; // kg per cubic metre
    private final float frictionCoeff;
    private final String texturePath;
     
    private Material(int mass, float frictionCoeff, String texturePath) {
        this.mass = mass;
        this.frictionCoeff = frictionCoeff;
        this.texturePath = texturePath;
    }
     
    public int getMass() {
        return mass;
    }
     
    public float getFrictionCoeff() {
        return frictionCoeff;
    }
     
    public String getTexturePath() {
        return texturePath;
    }
     
    public String toString() {
        return this.name() +": "+mass+" kg/m^3, "+texturePath;
    }
}