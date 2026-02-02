package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.ctre.phoenix6.hardware.Pigeon2;
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
    Translation2d frontLeftLocation  = new Translation2d(0.381, 0.381);
    Translation2d frontRightLocation = new Translation2d(0.381, -0.381);
    Translation2d backLeftLocation   = new Translation2d(-0.381, 0.381);
    Translation2d backRightLocation  = new Translation2d(-0.381, -0.381);

    SparkMax frontLeft  = new SparkMax(0, MotorType.kBrushless);
    SparkMax frontRight = new SparkMax(1, MotorType.kBrushless);
    SparkMax backLeft   = new SparkMax(2, MotorType.kBrushless);
    SparkMax backRight  = new SparkMax(3, MotorType.kBrushless);
    Pigeon2 gyro = new Pigeon2(4);

    private final MecanumDrive robotDrive;

    MecanumDriveKinematics mecanumKinematics = new MecanumDriveKinematics(
      frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation
    );

    MecanumDriveWheelPositions wheelPositions = new MecanumDriveWheelPositions(
      frontLeft.getEncoder().getPosition(), frontRight.getEncoder().getPosition(),
      backLeft.getEncoder().getPosition(), backRight.getEncoder().getPosition()
    );

    MecanumDriveOdometry odometry = new MecanumDriveOdometry(
      mecanumKinematics,
      gyro.getRotation2d(),
      wheelPositions,
      new Pose2d(5.0, 13.5, new Rotation2d())
    );

    public Mecanum() {
      robotDrive = new MecanumDrive(frontLeft::set, backLeft::set, frontRight::set, backRight::set);
    }

    public void driveFunction() {
      robotDrive.driveCartesian(Constants.Controls.getDriveX(), Constants.Controls.getDriveY(), Constants.Controls.getDriveRot());
      odometry.update(gyro.getRotation2d(), wheelPositions);
      odometry.getPoseMeters();

      
      
    } 

}
