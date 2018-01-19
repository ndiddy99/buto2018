package org.usfirst.frc.team2537.robot.auto;

import org.usfirst.frc.team2537.robot.Ports;
import org.usfirst.frc.team2537.robot.Specs;
import org.usfirst.frc.team2537.robot.conversions.Conversions;
import org.usfirst.frc.team2537.robot.drive.DriveSubsystem;
import org.usfirst.frc.team2537.robot.drive.Motor;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class VelocityTest extends Command {
	
	private XboxController xbox;
	private int ticks = 5;
	private double previousCycleDist = 0;
	private long previousCycleTime = 0;
	
	public VelocityTest(){
		requires(DriveSubsystem.getInstance());
	}
	
	@Override
	protected void initialize(){
		xbox = new XboxController(Ports.XBOX);
		DriveSubsystem.getInstance().setMode(ControlMode.PercentOutput);
	}
	
	@Override
	protected void execute(){
		if (Math.abs(xbox.getY(Hand.kLeft)) > .05 || Math.abs(xbox.getY(Hand.kRight)) > .05) {
			DriveSubsystem.getInstance().setMotors(-xbox.getY(Hand.kLeft), Motor.LEFT);
			DriveSubsystem.getInstance().setMotors(-xbox.getY(Hand.kRight), Motor.RIGHT);
		} else {
			DriveSubsystem.getInstance().setMotors(0);
		}

		double velocityAvg = DriveSubsystem.getInstance().getVelocityAverage();

		double cycleDist =  DriveSubsystem.getInstance().getEncoderAverage();
		long cycleTime = System.nanoTime();

		double velocity = (cycleDist - previousCycleDist) / (cycleTime - previousCycleTime);
//		velocity = Conversions.convertSpeed(velocity, Distances.TICKS, Times.NANOSECONDS, Distances.INCHES, Times.SECONDS);
		velocity *= 1e9 / 1024 * Specs.WHEEL_DIAMETER * Math.PI;
		
		previousCycleDist = cycleDist;
		previousCycleTime = cycleTime;

		if (ticks > 0) {
			ticks--;
		} else {
			ticks = 2;
			System.out.println(Conversions.roundDigits(velocityAvg,3) + " :: " + Conversions.roundDigits(velocity,3));
		}

	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
