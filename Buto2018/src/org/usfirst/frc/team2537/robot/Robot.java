package org.usfirst.frc.team2537.robot;

import org.usfirst.frc.team2537.robot.auto.DriveStraightCommand;
import org.usfirst.frc.team2537.robot.auto.VelocityTest;
import org.usfirst.frc.team2537.robot.drive.DriveSubsystem;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	@Override
	public void robotInit() {
		DriveSubsystem.getInstance().resetEncoders();
	}

	@Override
	public void autonomousInit() {
		new DriveStraightCommand(100, 1).start();
	}

	@Override
	public void autonomousPeriodic() {
		DriveSubsystem.getInstance().updateEncoders();
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		new VelocityTest().start();
	}

	@Override
	public void teleopPeriodic() {
		DriveSubsystem.getInstance().updateEncoders();
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
	}
	
}
