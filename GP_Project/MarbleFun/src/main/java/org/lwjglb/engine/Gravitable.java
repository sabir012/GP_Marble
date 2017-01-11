package org.lwjglb.engine;

import org.joml.Vector2f;

public interface Gravitable {
    public Vector2f calculateGravityForce(Vector2f slope); // from 0 to 90°
}
