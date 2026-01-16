package frc.robot;

import frc.robot.Controls.ControlInterface;
import frc.robot.Controls.TwoPersonControls;

public class Constants {

    public static final int gyroID = 32;

    // Global Constants
    public static       Mecanum    mecanumClass    = new Mecanum();
    public static final LED        ledClass        = new LED();
    public static final Shooter    shooterClass    = new Shooter();
    public static final Dashboard  dashboardClass  = new Dashboard();

    // Set up which interface we want to use
    public static ControlInterface Controls = new TwoPersonControls();
}
