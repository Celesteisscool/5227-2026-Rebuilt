package frc.robot.Controls;

public interface ControlInterface {
    double getDriveX(); 
    double getDriveY(); 
    double getDriveRot(); 
    double getDriveAngle(); // -180 -> 180
    boolean useDriveAngle(); // If true, uses the angle. If false, uses the rot inputs
    boolean getSlowMode(); 
    int getRobotRelativeDegrees();
    boolean getShooterButton();
    boolean getIntakeButton();   
}