package org.lwjglb.game.objects;


public enum Material {
	STEEL(7850, "/textures/steel.png"), GOLD(19320, "/textures/gold.png"), WOOD(600, "/textures/wood.png"), PLASTIC(950, "plastic.jpg"), GRAS(0, "/textures/grass.png");
	
	private final int mass; // kg per cubic metre
	private final String texturePath;
	
	private Material(int mass, String texturePath) {
		this.mass = mass;
		this.texturePath = texturePath;
	}
	
	public int getMass() {
		return mass;
	}
	
	public String getTexturePath() {
		return texturePath;
	}
	
	public String toString() {
		return this.name() +": "+mass+" kg/m^3, "+texturePath;
	}
}