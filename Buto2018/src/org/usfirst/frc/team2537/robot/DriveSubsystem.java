package org.usfirst.frc.team2537.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveSubsystem extends Subsystem{

	private static DriveSubsystem singleton;
	
	
/******************************************************************************/
/*                              PUBLIC CONSTANTS                              */
/******************************************************************************/
		
		/** inches */
		public static final double WHEEL_DIAMETER = 7.5;
		
		/** inches */
		public static final double WHEEL_CIRCUMFERENCE = Math.PI * WHEEL_DIAMETER;
		
		/** encoder ticks per full wheel revolution */
		public static final int TICKS_PER_REVOLUTION = 1024;
		
		public static final double TICKS_PER_INCH = TICKS_PER_REVOLUTION / WHEEL_CIRCUMFERENCE;
		
		/** set to 1 if the motors are in the forward direction
		 *  otherwise set to -1 when the motors are upside-down
		 *  
		 *  methods in this class that take speed parameters use these
		 *  multipliers to flip the sign of reversed motors.
		 */
		public static final int LEFT_MOTOR_DIRECTION = 1;
		public static final int RIGHT_MOTOR_DIRECTION = -1;

		
/******************************************************************************/
/*                             INSTANCE VARIABLES                             */
/******************************************************************************/
	
	private TalonSRX talonFrontLeft;
	private TalonSRX talonFrontRight;
	private PWMTalonSRX talonBackLeft;
	private PWMTalonSRX talonBackRight;
	public ControlMode controlMode = ControlMode.PercentOutput;
	
	
/******************************************************************************/
/*                                CONSTRUCTORS                                */
/******************************************************************************/
	
	public static DriveSubsystem getInstance(){
		if(singleton == null){
			singleton = new DriveSubsystem();
		}
		return singleton;
	}
	
	private DriveSubsystem(){
		talonFrontLeft  = new TalonSRX(Ports.FRONT_LEFT_MOTOR);
		talonFrontRight = new TalonSRX(Ports.FRONT_RIGHT_MOTOR);
		talonBackLeft   = new PWMTalonSRX(Ports.BACK_LEFT_MOTOR);
		talonBackRight  = new PWMTalonSRX(Ports.BACK_RIGHT_MOTOR);
		
		talonFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,  0,0);
		talonFrontRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0,0);
		
		talonFrontLeft.getSelectedSensorVelocity(0);
	}
	
	
/******************************************************************************/
/*                             OVERRIDEN METHODS                              */
/******************************************************************************/
	
	
	@Override
	protected void initDefaultCommand() {
	}
	
/******************************************************************************/
/*                              ENCODER METHODS                               */
/******************************************************************************/
	
	
	public double getEncoderAverage(){
		return (talonFrontLeft.getSensorCollection().getQuadraturePosition() * LEFT_MOTOR_DIRECTION +
			    talonFrontRight.getSensorCollection().getQuadraturePosition() * RIGHT_MOTOR_DIRECTION) / 2;
	}
	
	public void resetEncoders() {
		 talonFrontRight.getSensorCollection().setQuadraturePosition(0,0);
		 talonFrontLeft.getSensorCollection().setQuadraturePosition(0,0);
	}
	
	/**
	 * @return average velocity of all talons in inches per second
	 */
	public double getVelocityAverage(){
		/* ticks per 100ms */
		double rawVelocity = (talonFrontLeft.getSelectedSensorVelocity(0)
				+ talonFrontRight.getSelectedSensorVelocity(0)) / 2;
		
		double rotationsPerSecond = 10 * rawVelocity / TICKS_PER_REVOLUTION;
		
		return WHEEL_CIRCUMFERENCE * rotationsPerSecond;
	}
	
	
/******************************************************************************/
/*                               MOTOR METHODS                                */
/******************************************************************************/
	
	public void setMotor(double speed, Motor id){
		switch(id){
		case FRONT_LEFT:
			talonFrontLeft.set(controlMode, speed*LEFT_MOTOR_DIRECTION);
			break;
		case FRONT_RIGHT:
			talonFrontRight.set(controlMode, speed*RIGHT_MOTOR_DIRECTION);
			break;
		case BACK_LEFT:
			talonBackLeft.set(speed*LEFT_MOTOR_DIRECTION);
			break;
		case BACK_RIGHT:
			talonBackRight.set(speed*RIGHT_MOTOR_DIRECTION);
			break;
		}
	}
	
	public void setMotors(double speed, Motor...motors){
		if(motors.length == 0){
			setMotors(speed, Motor.FRONT_LEFT, Motor.FRONT_RIGHT, Motor.BACK_LEFT, Motor.BACK_RIGHT);
		} else {
			for(Motor motor : motors){
				setMotor(speed,motor);
			}
		}
	}
	
	
/******************************************************************************/
/*                                  SETTERS                                   */
/******************************************************************************/

/** control mode not actually changed until you 
 * set a value (thanks a lot dumbasses @ ctre)
 * @param controlMode
 */
	
	public void setMode(ControlMode controlMode) {
		this.controlMode = controlMode;
	}
	
	
/******************************************************************************/
/*                                CONVERSIONS                                 */
/******************************************************************************/	
	
	public double ticks2Inches(double ticks){
		return ticks / TICKS_PER_INCH;
	}
	
	public double inches2Ticks(double inches){
		return inches * TICKS_PER_INCH;
	}
	
}