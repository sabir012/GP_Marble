package org.lwjglb.game.objects;

import org.joml.Vector3f;
import org.lwjglb.engine.graph.*;


public class Constraint {
	private float restDist;
	Particle P1,P2;
	Constraint(Particle Par1, Particle Par2){
		this.P1 = Par1;
		this.P2 = Par2;
		Vector3f temp = new Vector3f(0,0,0);
		temp = P1.getPosition().sub(P2.getPosition());
		restDist = temp.length();
	}
	public void satisfyConstraint(){
		Vector3f distP1_P2 = P2.getPosition().sub(P1.getPosition());
		float currentDist =distP1_P2.length();
		float factor= 1 - restDist/currentDist;
		Vector3f correction = distP1_P2.mul(factor);
		Vector3f correctionHalf =new Vector3f(0,0,0);
		correctionHalf.half(correction);
		P1.offsetPos(correctionHalf);
		P2.offsetPos(correctionHalf.negate());
	}
}
