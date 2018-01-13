package org.usfirst.frc.team2537.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DriveSubsystem extends Subsystem{

	TalonSRX talonFrontLeft, talonFrontRight;
	Talon talonBackLeft, talonBackRight;
	
	public static final double WHEEL_DIAMETER = 7.5;
	public static final int PULSES_PER_REVOLUTION = 1040;
	public static final double TICKS_PER_INCH = PULSES_PER_REVOLUTION / (Math.PI * WHEEL_DIAMETER);
	public ControlMode currentControlMode=ControlMode.PercentOutput;
	
	public DriveSubsystem(){
		talonFrontLeft = new TalonSRX(Ports.FRONT_LEFT_MOTOR);
		talonFrontRight = new TalonSRX(Ports.FRONT_RIGHT_MOTOR);
		talonBackLeft = new Talon(Ports.BACK_LEFT_MOTOR);
		talonBackRight = new Talon(Ports.BACK_RIGHT_MOTOR);
		
		talonFrontLeft.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0,0);
		talonFrontRight.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0,0);
	}
	
	@Override
	protected void initDefaultCommand() {
//		this.setDefaultCommand(new EncoderTest());
		this.setDefaultCommand(new DriveStraightCommand(42069));
	}
	
	public void resetEncoders() {
		 talonFrontRight.getSensorCollection(). setQuadraturePosition(0,0);
		 talonFrontLeft.getSensorCollection(). setQuadraturePosition(0,0);
	}

/** control mode not actually changed until you 
 * set a value (thanks a lot dumbasses @ ctre)	
 * @param controlMode
 */
	
	public void setMode(ControlMode controlMode) {
		currentControlMode=controlMode;
	}
	
	public double getEncoderAverage(){
		return (talonFrontLeft.getSensorCollection().getQuadraturePosition() - talonFrontRight.getSensorCollection().getQuadraturePosition()) / 2;
	}
	
	public void setFrontMotors(double speed) {
		talonFrontLeft.set(currentControlMode,speed);
		talonFrontRight.set(currentControlMode,-speed);
	}
}
