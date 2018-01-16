package org.usfirst.frc.team2537.robot.conversions;

public enum Times{
	MILLISECONDS(1),
	CYCLES		(20),
	HUNDRED_MS	(100),
	SECONDS		(1000),
	MINUTES		(6000)
	;
	
	private final double toMillis;
	
	Times(double toMillis){
		this.toMillis = toMillis;
	}
	
	public double getMillis(){
		return toMillis;
	}
}
