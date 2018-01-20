package org.usfirst.frc.team2537.robot.drive;

import org.usfirst.frc.team2537.robot.conversions.Conversions;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class EncoderUpdater {
	public final double SLOW_THRESHOLD = 5, STOPPED_THRESHOLD = 2;
	private double previousEnc, previousVelocity;
	private int skippedCycles;
	private TalonSRX talon;

	public EncoderUpdater(TalonSRX talon) {
		this.talon = talon;
		previousEnc = 0;
		previousVelocity = 0;
		skippedCycles = 1;
	}

	public void update() {
		double enc = talon.getSensorCollection().getQuadraturePosition();
		double deltaEnc = Conversions.roundDigits(enc - previousEnc, 4);
		if (Math.abs(deltaEnc) > STOPPED_THRESHOLD || Math.abs(previousVelocity) < SLOW_THRESHOLD) {
			previousEnc = enc;
			previousVelocity = Conversions.roundDigits(deltaEnc / skippedCycles, 4);
			skippedCycles = 1;
		} else {
			skippedCycles++;
		}
	}
	
	public double latestValidVelocity(){
		return previousVelocity;
	}
	
	public int skippedCycles(){
		return skippedCycles - 1;
	}

}
