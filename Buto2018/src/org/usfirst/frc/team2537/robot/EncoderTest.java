package org.usfirst.frc.team2537.robot;

import edu.wpi.first.wpilibj.command.Command;

public class EncoderTest extends Command {

	public EncoderTest(){
		requires(Robot.driveSys);
	}
	
	@Override
	protected void initialize(){
		
	}
	
	@Override
	protected void execute(){
		System.out.println(Robot.driveSys.getEncoderAverage());
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
