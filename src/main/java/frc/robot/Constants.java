package frc.robot;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Controls.ControlInterface;
import frc.robot.Controls.RebindDemo;

public class Constants {
    // Drivetrain Constants
    public static final double maxSwerveSpeed = 1.5; // 1.5 meters per second
    public static final double maxSwerveAngularSpeed = Math.PI; // 1/2 rotation per second

    public static final int[] frontLeftIDs = { 5, 6, 11 };
    public static final int[] frontRightIDs = { 3, 4, 10 };
    public static final int[] backLeftIDs = { 7, 8, 12 };
    public static final int[] backRightIDs = { 1, 2, 9 };

    public static final double frontLeftOffset = -0.025634765625;
    public static final double frontRightOffset = 0.178466796875;
    public static final double backLeftOffset = -0.020751953125;
    public static final double backRightOffset = -0.437255859375;

    public static final int gyroID = 32;

    public static final SlewRateLimiter slewRateLimiterX = new SlewRateLimiter(3.5);
    public static final SlewRateLimiter slewRateLimiterY = new SlewRateLimiter(3.5);
    public static final SlewRateLimiter slewRateLimiterRot = new SlewRateLimiter(3);

    // SwerveModule Constants
    public static final Translation2d frontLeftLocation = new Translation2d(0.381, 0.381);
    public static final Translation2d frontRightLocation = new Translation2d(0.381, -0.381);
    public static final Translation2d backLeftLocation = new Translation2d(-0.381, 0.381);
    public static final Translation2d backRightLocation = new Translation2d(-0.381, -0.381);

    // Global Constants
    public static Drivetrain drivetrainClass = new Drivetrain();
    public static final LED ledClass = new LED();
    public static final Shooter shooterClass = new Shooter();
    public static final Dashboard dashboardClass = new Dashboard();

    public static ControlInterface Controls = new RebindDemo();

    // Auto Constants
    public final static PIDController xController = new PIDController(10.0, 0.0, 0.0);
    public final static PIDController yController = new PIDController(10.0, 0.0, 0.0);
    public final static PIDController rotController = new PIDController(7.5, 0.0, 0.0);
}
