package org.usfirst.frc.team2537.robot.vision;

public class Point {
	private int x;
	private int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String outputPacket() {
		return x + "," + y + "|";
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public double getX(CoordinateSystems system){
		double currentX = x, halfWidth = SerialSubsystem.CAMERA_WIDTH/2.0;
		if(system == CoordinateSystems.CARTESIAN || system == CoordinateSystems.CARTESIAN_NORMALIZED){
			currentX -= halfWidth;
		}
		if(system == CoordinateSystems.UV_NORMALIZED){
			currentX /= SerialSubsystem.CAMERA_WIDTH;
		}
		if(system == CoordinateSystems.CARTESIAN_NORMALIZED){
			currentX /= halfWidth;
		}
		return currentX;
	}
	
	public double getY(CoordinateSystems system){
		double currentY = y, halfHeight = SerialSubsystem.CAMERA_HEIGHT/2.0;
		if(system == CoordinateSystems.CARTESIAN || system == CoordinateSystems.CARTESIAN_NORMALIZED){
			currentY *= -1;
			currentY += halfHeight;
		}
		if(system == CoordinateSystems.UV_NORMALIZED){
			currentY /= SerialSubsystem.CAMERA_HEIGHT;
		}
		if(system == CoordinateSystems.CARTESIAN_NORMALIZED){
			currentY /= halfHeight;
		}
		return currentY;
	}

	@Override
	public String toString() {
		return "X val: " + x + " Y val: " + y + "\n";
	}
}
