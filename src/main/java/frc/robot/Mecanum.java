package frc.robot;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;
import edu.wpi.first.math.kinematics.MecanumDriveOdometry;
import edu.wpi.first.math.kinematics.MecanumDriveWheelPositions;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Mecanum {
	// Locations of the wheels relative to the robot center.
	Translation2d frontLeftLocation = new Translation2d(0.381, 0.381);
	Translation2d frontRightLocation = new Translation2d(0.381, -0.381);
	Translation2d backLeftLocation = new Translation2d(-0.381, 0.381);
	Translation2d backRightLocation = new Translation2d(-0.381, -0.381);

	SparkMax frontLeft = new SparkMax(4, MotorType.kBrushless);
	SparkMax frontRight = new SparkMax(1, MotorType.kBrushless);
	SparkMax backLeft = new SparkMax(3, MotorType.kBrushless);
	SparkMax backRight = new SparkMax(2, MotorType.kBrushless);
	Pigeon2 gyro = new Pigeon2(32);

	private final MecanumDrive robotDrive;

	MecanumDriveKinematics mecanumKinematics = new MecanumDriveKinematics(
			frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);

	MecanumDriveWheelPositions wheelPositions = new MecanumDriveWheelPositions(
			frontLeft.getEncoder().getPosition(), frontRight.getEncoder().getPosition(),
			backLeft.getEncoder().getPosition(), backRight.getEncoder().getPosition());

	MecanumDriveOdometry odometry; // We dont really use this :>

	public Mecanum() {
		robotDrive = new MecanumDrive(frontLeft::set, backLeft::set, frontRight::set, backRight::set);
		// Initialize odometry using the same gyro sign convention as driveCartesian
		// (getGyro())
		odometry = new MecanumDriveOdometry(
				mecanumKinematics,
				getGyro(),
				wheelPositions,
				new Pose2d(5.0, 13.5, new Rotation2d()));
	}

	/** return our gyro's pose as a Rotation2d */
	private Rotation2d getGyro() { 
		return gyro.getRotation2d().unaryMinus();
	}

	public void driveFunction() {
		double slowSpeed = 1.0;
		if (Classes.Controls.getSlowMode()) { slowSpeed = 0.5; } // Only use the slowspeed if we are using slowmode
		double driveX = (Classes.Controls.getDriveX() * slowSpeed);
		double driveY = (Classes.Controls.getDriveY() * slowSpeed);
		double driveRot = (Classes.Controls.getDriveRot() * slowSpeed);

		// Use a consistent gyro sign for field-oriented control.
		robotDrive.driveCartesian(
				driveX,
				driveY,
				driveRot,
				getGyro());

		// Refresh wheel positions from encoders before updating odometry.
		wheelPositions = new MecanumDriveWheelPositions(
				frontLeft.getEncoder().getPosition(),
				frontRight.getEncoder().getPosition(),
				backLeft.getEncoder().getPosition(),
				backRight.getEncoder().getPosition());

		odometry.update(getGyro(), wheelPositions);
		odometry.getPoseMeters();
		
		if (Classes.Controls.resetGyro()) { 
			gyro.reset();
			gyro.setYaw(180); // Resets in a way where the intake is facing the drivers
		}
	}



}
