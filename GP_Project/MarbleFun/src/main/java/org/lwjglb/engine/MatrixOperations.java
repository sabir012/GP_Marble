package org.lwjglb.engine;

import org.joml.Matrix3f;
import org.joml.Vector3f;

public class MatrixOperations {
		public static Vector3f mult(Matrix3f m, Vector3f v) {
			Vector3f result = new Vector3f();
			result.x = m.m00*v.x + m.m10*v.y + m.m20*v.z;
			result.y = m.m01*v.x + m.m11*v.y + m.m21*v.z;
			result.z = m.m02*v.x + m.m12*v.y + m.m22*v.z;
			
			return result;
		}
}
