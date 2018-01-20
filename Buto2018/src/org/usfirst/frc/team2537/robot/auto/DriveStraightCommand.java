package org.usfirst.frc.team2537.robot.auto;


import org.usfirst.frc.team2537.robot.conversions.Conversions;
import org.usfirst.frc.team2537.robot.conversions.Distances;
import org.usfirst.frc.team2537.robot.drive.DriveSubsystem;
import org.usfirst.frc.team2537.robot.drive.Motor;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class DriveStraightCommand extends Command {
	
/******************************************************************************/
/*                              PUBLIC CONSTANTS                              */
/******************************************************************************/

	/** when we are at or below this angle (degrees) from the target, we gradually slow down until the angle is 0 */
	public static final double ANGLE_TOLERANCE = 1;
	
	public static final double SLOW_DOWN_POWER = 15;
	
	public static final double[] ANGLE_PID = new double[]{20,0,0};
	
	/** value [0,1] representing the percent of power to motors */
	public static final double DEFAULT_PERCENT_OUTPUT = 1;
	
	/* first element is number of encoder ticks
	 * second element is system nanoseconds that denote the time of reading the encoder
	 */
	private double[] previousTickTime;
	
	
/******************************************************************************/
/*                             INSTANCE VARIABLES                             */
/******************************************************************************/
	
	private double targetTicks;
	private double motorPower;
	private double slowDownDistance;
	private boolean slowingDown;
	
	
/******************************************************************************/
/*                                CONSTRUCTORS                                */
/******************************************************************************/

	/**
	 * @param distance (inches)
	 * @param defaultPercentOutput power to the motors [0,1]
	 */
	public DriveStraightCommand(double distance) {
		this(distance, DEFAULT_PERCENT_OUTPUT);
	}

	/**
	 * @param distance (inches)
	 * @param defaultPercentOutput power to the motors [0,1]
	 */
	public DriveStraightCommand(double distance, double defaultPercentOutput) {
		requires(DriveSubsystem.getInstance());
		targetTicks = Conversions.convertDistance(distance, Distances.INCHES, Distances.TICKS);
		motorPower = defaultPercentOutput;
		slowingDown = false;
	}

	
/******************************************************************************/
/*                             OVERRIDEN METHODS                              */
/******************************************************************************/
	
	@Override
	protected void initialize() {
		DriveSubsystem.getInstance().resetEncoders();
		DriveSubsystem.getInstance().setMode(ControlMode.PercentOutput);
		Navx.getInstance().reset();
	}
	
	@Override
	protected void execute(){
		/* we convert angles to values in range [-1,1] */
		double normalizedAngle = Navx.getInstance().getAngle() / 180;
		double normalizedSlowDownAngle = ANGLE_TOLERANCE / 180;
		double currentTicks = DriveSubsystem.getInstance().getEncoderDistance();
		double power = motorPower;
		double encoderVelocity = DriveSubsystem.getInstance().getEncoderVelocity();
		
		if(!slowingDown){
			slowDownDistance = calculateSlowDownDistance(encoderVelocity);
//			System.out.println("avg velocity: " + (int)encoderVelocity +" in/s;  slow down distance: " + slowDownDistance + " in.");
			slowDownDistance = Conversions.convertDistance(slowDownDistance, Distances.INCHES, Distances.TICKS);
			if(targetTicks - currentTicks <= slowDownDistance){
				System.out.println("\n\n\n\n\n SLOWING DOWN!!! slow down distance: " + slowDownDistance);
				slowingDown = true;
			}
		}
		
		if(slowingDown){
			System.out.println("avg velocity: " + (int)encoderVelocity +" in/s");
			power *= (targetTicks - currentTicks) / targetTicks;
		}
		
		/* we add a speed delta to compensate for being off angle */
		double slowDownDelta = 0;
		if(Math.abs(normalizedAngle) > Math.abs(normalizedSlowDownAngle)){
			slowDownDelta = normalizedAngle * ANGLE_PID[0] * power;
		}
		
		DriveSubsystem.getInstance().setMotors(power - slowDownDelta, Motor.FRONT_LEFT, Motor.BACK_LEFT);
		DriveSubsystem.getInstance().setMotors(power + slowDownDelta, Motor.FRONT_RIGHT, Motor.BACK_RIGHT);
	}
	
	@Override
	protected boolean isFinished() {
		return DriveSubsystem.getInstance().getEncoderDistance() >= targetTicks;
	}
	
	@Override
	protected void end(){
		DriveSubsystem.getInstance().setMotors(0);
	}

	/** Called when another command which requires one or more of the same subsystems is scheduled to run
	 *
	 */
	@Override
	protected void interrupted() {
		end();
	}

	
/******************************************************************************/
/*                                   MISC                                     */
/******************************************************************************/
	
	/**
	 * @param speed (inches per second)
	 * @return the optimal distance away from the target at which the robot should slow down (inches)
	 */
	private double calculateSlowDownDistance(double speed){
		return Math.pow(speed, 2) / SLOW_DOWN_POWER;
	}
	
}