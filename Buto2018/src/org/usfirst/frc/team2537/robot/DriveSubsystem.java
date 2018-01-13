package org.usfirst.frc.team2537.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveSubsystem extends Subsystem{

	TalonSRX talonFrontLeft;
	TalonSRX talonFrontRight;
	PWMTalonSRX talonBackLeft;
	PWMTalonSRX talonBackRight;
	
	/** inches */
	public static final double WHEEL_DIAMETER = 7.5;
	/** encoder ticks per full wheel revolution */
	public static final int PULSES_PER_REVOLUTION = 1024;
	/** encoder ticks per inch of circumference */
	public static final double TICKS_PER_INCH = PULSES_PER_REVOLUTION / (Math.PI * WHEEL_DIAMETER);
	
	/** set to 1 if the motors are in the forward direction
	 *  otherwise set to -1 when the motors are upside-down
	 *  
	 *  methods in this class that take speed parameters use these
	 *  multipliers to flip the sign of reversed motors.
	 */
	public static final int LEFT_MOTOR_DIRECTION = 1;
	public static final int RIGHT_MOTOR_DIRECTION = -1;
	
	public static ControlMode currentControlMode = ControlMode.PercentOutput;
	
	public DriveSubsystem(){
		talonFrontLeft  = new TalonSRX(Ports.FRONT_LEFT_MOTOR);
		talonFrontRight = new TalonSRX(Ports.FRONT_RIGHT_MOTOR);
		talonBackLeft   = new PWMTalonSRX(Ports.BACK_LEFT_MOTOR);
		talonBackRight  = new PWMTalonSRX(Ports.BACK_RIGHT_MOTOR);
		
		talonFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,  0,0);
		talonFrontRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0,0);
	}
	
	@Override
	protected void initDefaultCommand() {
	}
	
	public void resetEncoders() {
		 talonFrontRight.getSensorCollection().setQuadraturePosition(0,0);
		 talonFrontLeft.getSensorCollection().setQuadraturePosition(0,0);
	}

/** control mode not actually changed until you 
 * set a value (thanks a lot dumbasses @ ctre)
 * @param controlMode
 */
	
	public void setMode(ControlMode controlMode) {
		currentControlMode = controlMode;
	}
	
	public double getEncoderAverage(){
		return (talonFrontLeft.getSensorCollection().getQuadraturePosition() * LEFT_MOTOR_DIRECTION +
			    talonFrontRight.getSensorCollection().getQuadraturePosition() * RIGHT_MOTOR_DIRECTION) / 2;
	}
	
	public void setMotors(double speed) {
		talonFrontLeft.set(currentControlMode, speed * LEFT_MOTOR_DIRECTION);
		talonFrontRight.set(currentControlMode,speed * RIGHT_MOTOR_DIRECTION);
		talonBackLeft.set(speed * LEFT_MOTOR_DIRECTION);
		talonBackRight.set(speed * RIGHT_MOTOR_DIRECTION);
	}
	
	public void setMotors(double leftSpeed, double rightSpeed){
		talonFrontLeft.set(currentControlMode, leftSpeed * LEFT_MOTOR_DIRECTION);
		talonBackLeft.set(leftSpeed * LEFT_MOTOR_DIRECTION);
		
		talonFrontRight.set(currentControlMode, rightSpeed * RIGHT_MOTOR_DIRECTION);
		talonBackRight.set(rightSpeed * RIGHT_MOTOR_DIRECTION);
	}
	
	public void setLeftMotors(double speed){
		talonFrontLeft.set(currentControlMode, speed * LEFT_MOTOR_DIRECTION);
		talonBackLeft.set(speed * LEFT_MOTOR_DIRECTION);
	}
	
	public void setRightMotors(double speed){
		talonFrontRight.set(currentControlMode,speed * RIGHT_MOTOR_DIRECTION);
		talonBackRight.set(speed * RIGHT_MOTOR_DIRECTION);
	}
}
