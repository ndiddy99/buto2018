package org.usfirst.frc.team2537.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;

import org.usfirst.frc.team2537.robot.auto.DriveStraightCommand;
import org.usfirst.frc.team2537.robot.auto.Navx;
import org.usfirst.frc.team2537.robot.conversions.Conversions;
import org.usfirst.frc.team2537.robot.conversions.Distances;
import org.usfirst.frc.team2537.robot.conversions.Times;
import org.usfirst.frc.team2537.robot.drive.DriveSubsystem;
import org.usfirst.frc.team2537.robot.drive.Motor;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static XboxController xbox;
	public static int ticks = 5;
	public static double[] previousTickTime = new double[2];

	@Override
	public void robotInit() {
		xbox = new XboxController(Ports.XBOX);
	}

	@Override
	public void autonomousInit() {
		new DriveStraightCommand(100, 1).start();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// Navx.getInstance().reset();
		Navx.getInstance().resetDisplacement();
		DriveSubsystem.getInstance().setMode(ControlMode.PercentOutput);
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		if (Math.abs(xbox.getY(Hand.kLeft)) > .05 || Math.abs(xbox.getY(Hand.kRight)) > .05) {
			DriveSubsystem.getInstance().setMotors(-xbox.getY(Hand.kLeft), Motor.LEFT);
			DriveSubsystem.getInstance().setMotors(-xbox.getY(Hand.kRight), Motor.RIGHT);
		} else {
			DriveSubsystem.getInstance().setMotors(0);
		}

		double velocityAvg = DriveSubsystem.getInstance().getVelocityAverage();

		double[] tickTime = new double[2];
		tickTime[0] = DriveSubsystem.getInstance().getEncoderAverage();
		tickTime[1] = System.nanoTime();

		double velocity = (tickTime[0] - previousTickTime[0]) / (tickTime[1] - previousTickTime[1]);
//		velocity = Conversions.convertSpeed(velocity, Distances.TICKS, Times.NANOSECONDS, Distances.INCHES, Times.SECONDS);
		velocity *= 1e9 / 1024 * Specs.WHEEL_DIAMETER * Math.PI;
		
		previousTickTime = tickTime;

		if (ticks > 0) {
			ticks--;
		} else {
			ticks = 2;
			System.out.println(roundDigits(velocityAvg,3) + " :: " + roundDigits(velocity,3));
		}

	}

	@Override
	public void testPeriodic() {
	}
	
	private double roundDigits(double value, int digits){
		return (int)(value * Math.pow(10, digits))/Math.pow(10, digits);
	}
}
