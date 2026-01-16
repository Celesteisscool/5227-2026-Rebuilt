package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.MecanumDriveKinematics;

public class Mecanum {
    // Locations of the wheels relative to the robot center.
    Translation2d m_frontLeftLocation = new Translation2d(0.381, 0.381);
    Translation2d m_frontRightLocation = new Translation2d(0.381, -0.381);
    Translation2d m_backLeftLocation = new Translation2d(-0.381, 0.381);
    Translation2d m_backRightLocation = new Translation2d(-0.381, -0.381);
    // Creating my kinematics object using the wheel locations.
    MecanumDriveKinematics m_kinematics = new MecanumDriveKinematics(
      m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation
    );

    public static void testFunctionYay() {
      // PUT CODE HERE PLEASE :>
    }

}
