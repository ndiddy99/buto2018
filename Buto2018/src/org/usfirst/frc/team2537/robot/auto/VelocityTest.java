package org.usfirst.frc.team2537.robot.auto;

import org.usfirst.frc.team2537.robot.Ports;
import org.usfirst.frc.team2537.robot.drive.DriveSubsystem;
import org.usfirst.frc.team2537.robot.drive.Motor;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;

public class VelocityTest extends Command {
	
	private XboxController xbox;
	
	public VelocityTest(){
		requires(DriveSubsystem.getInstance());
	}
	
	@Override
	protected void initialize(){
		xbox = new XboxController(Ports.XBOX);
		DriveSubsystem.getInstance().setMode(ControlMode.PercentOutput);
		DriveSubsystem.getInstance().resetEncoders();
	}
	
	@Override
	protected void execute(){
		if (Math.abs(xbox.getY(Hand.kLeft)) > .05 || Math.abs(xbox.getY(Hand.kRight)) > .05) {
			DriveSubsystem.getInstance().setMotors(-xbox.getY(Hand.kLeft), Motor.LEFT);
			DriveSubsystem.getInstance().setMotors(-xbox.getY(Hand.kRight), Motor.RIGHT);
		} else {
			DriveSubsystem.getInstance().setMotors(0);
		}
		//System.out.println(DriveSubsystem.getInstance().getEncoderVelocity());
		System.out.println(Navx.getInstance().getAngle());
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
