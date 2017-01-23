package org.lwjglb.game.objects;


public enum PhysicalMaterial {
	STEEL(7850, 0.99f, "/textures/steel.png"), GOLD(19320, 0.99f, "/textures/gold.png"), 
    WOOD(600, 0.99f, "/textures/wood.png"), PLASTIC(950, 0.99f, "/textures/marbleRed.png"), GRAS(0, 0.99f, "/textures/grass.png");
     
    private final int mass; // kg per cubic metre 
    private final float damping;
    private final String texturePath;
     
    private PhysicalMaterial(int mass, float damping, String texturePath) {
        this.mass = mass;
        this.damping = damping;
        this.texturePath = texturePath;
    }
     
    public int getMass() {
        return mass;
    }
     
    public float getDamping() {
        return damping;
    }
     
    public String getTexturePath() {
        return texturePath;
    }
     
    public String toString() {
        return this.name() +": "+mass+" kg/m^3, "+texturePath;
    }
}