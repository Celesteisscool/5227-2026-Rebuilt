package frc.robot.Controls;

public interface ControlInterface {
    // Drive Controls
    double getDriveX(); 
    double getDriveY(); 
    double getDriveRot(); 
    boolean getSlowMode(); 

    boolean resetGyro();

    // Shooter Controls
    boolean getShooterButton();
    boolean getReverseShooterButton();

    boolean getIntakeButton();   
    boolean getOuttakeButton();

    double getAngleAdjust(); // for adjusting the angle of the shooter
}