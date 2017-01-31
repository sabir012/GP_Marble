package org.lwjglb.game.objects;

import org.joml.Vector3f;
import org.lwjglb.engine.graph.*;


public class Constraint {
	private float restDist;
	Particle P1,P2;
	Constraint(Particle Par1, Particle Par2){
		this.P1 = Par1;
		this.P2 = Par2;
		Vector3f temp = P1.getPosition();
		temp.sub(P2.getPosition());
		restDist = temp.length();
	}
	public void satisfyConstraint(){
		Vector3f tempPosP1 = P1.getPosition();
		Vector3f tempPosP2 = P2.getPosition();
		Vector3f distP1_P2 = tempPosP2.sub(tempPosP1);
		float currentDist =distP1_P2.length();
		float factor= 1 - restDist/currentDist;
		Vector3f tempCor = distP1_P2;
		Vector3f correction = tempCor.mul(factor);
		Vector3f correctionHalf =new Vector3f(0.0f,0.0f,0.0f);
		correctionHalf.half(correction);
		P1.offsetPos(correctionHalf);
		P2.offsetPos(correctionHalf.negate());
	}
}
