package frc.robot.Controls;

public interface ControlInterface {
    // Drive Controls
    double getDriveX(); 
    double getDriveY(); 
    double getDriveRot(); 
    boolean getSlowMode(); 

    // Shooter Controls
    boolean getShooterButton();
    boolean getIntakeButton();   
}