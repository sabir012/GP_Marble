package de.rwthaachen.gp.marblefun;

import de.rwthaachen.gp.marblefun.GameObjects.Ball;
import de.rwthaachen.gp.marblefun.GameObjects.Material;
import de.rwthaachen.gp.marblefun.math.Vector2f;

public class Main {

	public static void main(String[] args) {
		Ball b = new Ball(new Vector2f(15.257,-9.324), Material.STEEL, 5);
		System.out.println(b);
	}

}
