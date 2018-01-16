package org.usfirst.frc.team2537.robot.conversions;

public class Conversions {
	
	public static final double WHEEL_DIAMETER = 7.5;
	public static final int TICKS_PER_REVOLUTION = 1024;
	
	public static double convertDistance(double value, Distances currentType, Distances desiredType){
		return value * desiredType.getTicks() / currentType.getTicks();
	}
	
	public static double convertTime(double value, Times currentType, Times desiredType){
		return value * desiredType.getMillis() / currentType.getMillis();
	}
	
	public static double convertSpeed(double value, Distances currentDistanceType, Times currentTimeType,
			Distances desiredDistanceType, Times desiredTimeType){
		return value * (desiredDistanceType.getTicks() / desiredTimeType.getMillis())
				* (currentTimeType.getMillis() / currentDistanceType.getTicks());
	}
}
