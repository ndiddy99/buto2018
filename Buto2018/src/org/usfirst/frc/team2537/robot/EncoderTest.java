package org.usfirst.frc.team2537.robot;

import edu.wpi.first.wpilibj.command.Command;

public class EncoderTest extends Command {

	public EncoderTest(){
		requires(DriveSubsystem.getInstance());
	}
	
	@Override
	protected void initialize(){
		
	}
	
	@Override
	protected void execute(){
		System.out.println(DriveSubsystem.getInstance().getEncoderAverage());
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
