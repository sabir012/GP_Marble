package org.lwjglb.engine;

public enum Physics {
    GRAVITY(9.81f,"m/s^2");
     
    private float value;
    private String unit;
     
    private Physics(float value, String unit) {
        this.value = value;
        this.unit = unit;
    }
     
    public float getValue() {
        return value;
    }
     
    public String getUnit() {
        return unit;
    }
}