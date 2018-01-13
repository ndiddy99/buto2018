package org.usfirst.frc.team2537.robot;


import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightCommand extends Command {

	private final double targetTicks;
	final int ENCODER_TOLERANCE = 500;
	final double SPEED = .5;
	final boolean DEBUG = true;

	public DriveStraightCommand(double distance) {
		requires(Robot.driveSys);
		targetTicks = distance * DriveSubsystem.TICKS_PER_INCH;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		System.out.println("[DriveStraightCommand] target ticks: " + targetTicks);
		Robot.driveSys.resetEncoders();
		// Robot.driveSys.getAhrs().reset();
		Robot.driveSys.setMode(ControlMode.PercentOutput);
		Robot.driveSys.setFrontMotors(SPEED);
		// Robot.driveSys.setFrontMotors(targetTicks, -targetTicks);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		if (DEBUG) {
			System.out.println("current ticks " + Robot.driveSys.getEncoderAverage());
			System.out.println("distance from target ticks: " + Robot.driveSys.getEncoderAverage());
		}
		return Math.abs(Robot.driveSys.getEncoderAverage() - targetTicks) < ENCODER_TOLERANCE;
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}

}