package org.usfirst.frc.team2537.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;

import org.usfirst.frc.team2537.robot.auto.DriveStraightCommand;
import org.usfirst.frc.team2537.robot.auto.Navx;
import org.usfirst.frc.team2537.robot.drive.DriveSubsystem;
import org.usfirst.frc.team2537.robot.drive.Motor;

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
		//Navx.getInstance().reset();
		Navx.getInstance().resetDisplacement();
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		if (Math.abs(xbox.getY(Hand.kLeft)) > .05 || Math.abs(xbox.getY(Hand.kRight)) > .05) {
			DriveSubsystem.getInstance().setMotors(-xbox.getY(Hand.kLeft), Motor.FRONT_LEFT, Motor.BACK_LEFT);
			DriveSubsystem.getInstance().setMotors(-xbox.getY(Hand.kRight), Motor.FRONT_RIGHT, Motor.BACK_RIGHT);
		}
		else{
			DriveSubsystem.getInstance().setMotors(0);
		}
	}

	@Override
	public void testPeriodic() {
	}
}
