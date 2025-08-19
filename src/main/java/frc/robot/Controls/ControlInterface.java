package frc.robot.Controls;

public interface ControlInterface {
    double getDriveX();
    double getDriveY();
    double getDriveRot();
    boolean getSlowMode();
    double getRobotRelativeDegrees();
}

