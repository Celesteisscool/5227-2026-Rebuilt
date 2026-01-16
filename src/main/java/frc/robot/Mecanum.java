package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.geometry.Translation2d;
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
    private final MecanumDrive robotDrive;

    public Mecanum() {
      robotDrive = new MecanumDrive(frontLeft::set, backLeft::set, frontRight::set, backRight::set);
    }

    public void driveFunction() {
      robotDrive.driveCartesian(Constants.Controls.getDriveX(), Constants.Controls.getDriveX(), Constants.Controls.getDriveRot());
    } 

}
