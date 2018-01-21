package org.usfirst.frc.team2537.robot.drive;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class EncoderUpdater {
	private TalonSRX talon;
	private double previousEnc, previousVelocity;
	//private int skippedCycles;
	//public final double SLOW_THRESHOLD = 5, STOPPED_THRESHOLD = 2;

	public EncoderUpdater(TalonSRX talon) {
		this.talon = talon;
		previousEnc = 0;
		previousVelocity = 0;
		//skippedCycles = 1;
	}

	public void update() {
		double enc = talon.getSelectedSensorPosition(0);
		double deltaEnc = enc - previousEnc;
		previousEnc = enc;
		previousVelocity = deltaEnc;
		/*
		if (Math.abs(deltaEnc) > STOPPED_THRESHOLD || Math.abs(previousVelocity) < SLOW_THRESHOLD) {
			previousEnc = enc;
			previousVelocity = Conversions.roundDigits(deltaEnc / skippedCycles, 4);
			skippedCycles = 1;
		} else {
			skippedCycles++;
		}
		*/
	}
	
	public double latestValidVelocity(){
		return previousVelocity;
	}
	
	/*
	public int skippedCycles(){
		return skippedCycles - 1;
	}
	*/

}
