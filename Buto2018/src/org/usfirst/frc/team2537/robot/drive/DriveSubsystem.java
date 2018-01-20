package org.usfirst.frc.team2537.robot.drive;

import org.usfirst.frc.team2537.robot.Ports;
import org.usfirst.frc.team2537.robot.conversions.Conversions;
import org.usfirst.frc.team2537.robot.conversions.Distances;
import org.usfirst.frc.team2537.robot.conversions.Times;

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
	private EncoderUpdater encFrontLeft;
	private EncoderUpdater encFrontRight;
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
		encFrontLeft = new EncoderUpdater(talonFrontLeft);
		encFrontRight = new EncoderUpdater(talonFrontRight);
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
	
	
	public double getEncoderDistance(){
		
		return (talonFrontLeft.getSensorCollection().getQuadraturePosition() * LEFT_MOTOR_DIRECTION +
			    talonFrontRight.getSensorCollection().getQuadraturePosition() * RIGHT_MOTOR_DIRECTION) / 2;
	}
	
	public double getEncoderVelocity(){
		if(encFrontLeft.skippedCycles() < encFrontRight.skippedCycles()){
			return encFrontLeft.latestValidVelocity() * LEFT_MOTOR_DIRECTION;
		} else {
			return encFrontRight.latestValidVelocity() * RIGHT_MOTOR_DIRECTION;
		}
	}
	
	public void resetEncoders() {
		 talonFrontRight.getSensorCollection().setQuadraturePosition(0,0);
		 talonFrontLeft.getSensorCollection().setQuadraturePosition(0,0);
	}
	
	public void updateEncoders(){
		encFrontLeft.update();
		encFrontRight.update();
	}
	
	/**
	 * @return average velocity of all talons in inches per second
	 */
	public double getVelocityAverage(){
		/* ticks per 100ms */
		double rawVelocity = (talonFrontLeft.getSelectedSensorVelocity(0)
				+ talonFrontRight.getSelectedSensorVelocity(0)) / 2;

		return Conversions.convertSpeed(rawVelocity, Distances.TICKS, Times.HUNDRED_MS, Distances.INCHES, Times.SECONDS);
	}
	
	
/******************************************************************************/
/*                               MOTOR METHODS                                */
/******************************************************************************/
	
	public void setMotor(double speed, Motor id){
		if(id == Motor.FRONT_LEFT || id == Motor.LEFT || id == Motor.FRONT || id == Motor.ALL){
			talonFrontLeft.set(controlMode, speed*LEFT_MOTOR_DIRECTION);
		}
		if(id == Motor.FRONT_RIGHT || id == Motor.RIGHT || id == Motor.FRONT || id == Motor.ALL){
			talonFrontRight.set(controlMode, speed*RIGHT_MOTOR_DIRECTION);
		}
		if(id == Motor.BACK_LEFT || id == Motor.LEFT || id == Motor.BACK || id == Motor.ALL){
			talonBackLeft.set(speed*LEFT_MOTOR_DIRECTION);
		}
		if(id == Motor.BACK_RIGHT || id == Motor.LEFT || id == Motor.BACK || id == Motor.ALL){
			talonBackRight.set(speed*RIGHT_MOTOR_DIRECTION);
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
	
}