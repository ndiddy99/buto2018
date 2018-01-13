package org.usfirst.frc.team2537.robot;

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
	
	public static DriveSubsystem driveSys;

	@Override
	public void robotInit() {
		driveSys = new DriveSubsystem();
	}

	@Override
	public void autonomousInit() {
	}

	@Override
	public void autonomousPeriodic() {

	}
	
	@Override
	public void teleopInit() {
		
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
	}
}

