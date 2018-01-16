package org.usfirst.frc.team2537.robot.conversions;

public enum Distances{
	TICKS			(1),
	REVOLUTIONS		(Conversions.TICKS_PER_REVOLUTION),
	INCHES			(REVOLUTIONS.getTicks()*Math.PI*Conversions.WHEEL_DIAMETER),
	FEET			(INCHES.getTicks()*12),
	CENTIMETERS		(INCHES.getTicks()/2.54),
	METERS			(CENTIMETERS.getTicks()*100),
	NAUTICAL_MILES	(METERS.getTicks()*1852)
	;
	
	private final double toTicks;
	
	Distances(double toTicks){
		this.toTicks = toTicks;
	}
	
	public double getTicks(){
		return toTicks;
	}
}
