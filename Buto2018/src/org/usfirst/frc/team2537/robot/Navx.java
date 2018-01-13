package org.usfirst.frc.team2537.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI.Port;

public class Navx extends AHRS{
	
	private static Navx singleton;
	
	public static Navx getInstance(){
		if(singleton == null){
			singleton = new Navx(Port.kMXP);
		}
		return singleton;
	}
	
	private Navx(Port port){
		super(port);
	}
	
	/**
	 * @return magnitude of acceleration in the xy plane
	 */
	public double getAcceleration(){
		return Math.sqrt(Math.pow(super.getRawAccelX(), 2) + Math.pow(super.getRawAccelY(), 2));
	}
	
	/**
	 * @return displacement in the xy plane
	 */
	public double getDisplacement(){
		return Math.sqrt(Math.pow(super.getDisplacementX(), 2) + Math.pow(super.getDisplacementY(), 2));
	}
	
	/**
	 * @return direction of robot from [-180,180]
	 */
	@Override
	public double getAngle(){
		
		return ((super.getAngle() + 180) % 360 + 360) % 360 - 180;
	}
}