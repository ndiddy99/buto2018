package org.usfirst.frc.team2537.robot;


import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightCommand extends Command {

	/** we are expecting to overshoot by these many encoder ticks */
	public static final int ENCODER_TOLERANCE = 0;
	/** value [0,1] representing the percent of power to motors */
	public static final double DEFAULT_SPEED = 1;
	/** when we are at or below this angle, we gradually turn until the angle is 0 */
	public static final double ANGLE_TOLERANCE = 1;
	/** when we are at or below this distance (inches), we gradually slow down */
	public static final double SLOW_DOWN_DISTANCE = 10;
	
	public static final double[] ANGLE_PID = new double[]{20,0,0};
	public static final double[] DISTANCE_PID = new double[]{0,0,10};
	
	private double targetTicks;
	/* the number of ticks that we reached in the prior 20 ms update of this command
	 * we use this variable to calculate current speed.
	 */
	private double previousTicks;
	/* the number of ticks per update to this command that we reached 20 ms ago.
	 * we use this variable to calculate acceleration.
	 */
	private double previousTicksPerCycle;

	/**
	 * @param distance (inches)
	 */
	public DriveStraightCommand(double distance) {
		requires(Robot.driveSys);
		targetTicks = distance * DriveSubsystem.TICKS_PER_INCH;
	}
	
	@Override
	protected void initialize() {
		Robot.driveSys.resetEncoders();
		Robot.driveSys.setMode(ControlMode.PercentOutput);
		Navx.getInstance().reset();
		previousTicks = 0;
	}
	
	@Override
	protected void execute(){
		/* we convert angles to values in range [-1,1] */
		double normalizedAngle = Navx.getInstance().getAngle() / 180;
		double normalizedSlowDownAngle = ANGLE_TOLERANCE / 180;
		double currentTicks = Robot.driveSys.getEncoderAverage();
		double speed = DEFAULT_SPEED;
		
		System.out.println("acceleration: " + Navx.getInstance().getAcceleration());
		
		/* if we start slowing down when we approach our target distance */
		if((targetTicks - currentTicks) / Robot.driveSys.TICKS_PER_INCH < SLOW_DOWN_DISTANCE){
			speed *= (targetTicks - currentTicks) / targetTicks;
		}
		
		/* we add a speed delta to compensate for being off angle */
		double slowDownDelta = 0;
		if(Math.abs(normalizedAngle) > Math.abs(normalizedSlowDownAngle)){
			slowDownDelta = normalizedAngle * ANGLE_PID[0] * speed;
		}
		
		Robot.driveSys.setMotors(speed - slowDownDelta, speed + slowDownDelta);
	}
	
	@Override
	protected boolean isFinished() {
		return Robot.driveSys.getEncoderAverage() >= targetTicks;
	}
	
	@Override
	protected void end(){
		Robot.driveSys.setMotors(0);
	}

	/** Called when another command which requires one or more of the same subsystems is scheduled to run
	 *
	 */
	@Override
	protected void interrupted() {
		end();
	}
}