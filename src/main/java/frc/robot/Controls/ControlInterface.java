package frc.robot.Controls;

public interface ControlInterface {
    double getDriveX();
    double getDriveY();
    double getDriveRot();
    boolean getSlowMode();
    int getRobotRelativeDegrees();
    boolean getShooterButton();
    boolean getIntakeButton();
    double getAngleAdjust();
}

