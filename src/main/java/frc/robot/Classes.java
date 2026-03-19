package frc.robot;

import frc.robot.Controls.ControlInterface;
import frc.robot.Controls.OnePersonControls;
import frc.robot.Controls.TwoPersonControls;
import frc.robot.Shooter.Shooter;

/**
 * Holds all the classes that we need to use globally
 */
public class Classes {
    // Global classes
    public static Mecanum mecanumClass = new Mecanum();
    public static final LED ledClass = new LED();
    public static final Shooter shooterClass = new Shooter();
    public static final Dashboard dashboardClass = new Dashboard();
    public static final DriverFeedback driverFeedbackClass = new DriverFeedback();

    // Set up which interface we want to use
    public static ControlInterface Controls = new OnePersonControls();
}
