package org.lwjglb.engine;

public enum GameItemType {
	BALL("marble1.obj"), TRACK("track.obj"), trackHUD("trackHUD.obj"), panelHUD("panel3.obj"), GRASS(null);
	
	private final String filename;
	private final String meshpath = "/models/";
	
	private GameItemType(String filename) {
		this.filename = filename;
	}
	
	public String getPath() {
		return meshpath+filename;
	}
}
