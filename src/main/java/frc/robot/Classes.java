package frc.robot;

import frc.robot.Controls.AutoControls;
import frc.robot.Controls.ControlInterface;
import frc.robot.Controls.OnePersonControls;
import frc.robot.Controls.TwoPersonControls;
import frc.robot.Shooter.Music;
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
    public static final Auto autoClass = new Auto();

    public static final Music musicClass = new Music();

    // Set up which interface we want to use
    
    public static ControlInterface NormalControls = new TwoPersonControls();
    public static ControlInterface AutoControls = new AutoControls();

    public static ControlInterface Controls = NormalControls;
    // public static ControlInterface Controls = new OnePersonControls();
}
