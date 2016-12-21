package de.rwthaachen.gp.marblefun.GameObjects;

public enum Material {
	STEEL(7850, "steel.jpg"), GOLD(19320, "gold.jpg"), WOOD(600, "wood.jpg"), PLASTIC(950, "plastic.jpg");
	
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
