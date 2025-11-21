package frc.robot.Controls;

public class ControlSwap1 implements ControlInterface {

    @Override
    public double getDriveX() {
        return 0.0;
    }   

    @Override
    public double getDriveY() {
        return 0.0;
    }   

    @Override
    public double getDriveRot() {
        return 0.0;
    }   

    @Override
    public boolean getSlowMode() {
        return false;
    }

    @Override
    public int getRobotRelativeDegrees() {
        return -1;
    }

    @Override
    public boolean getShooterButton() {
        return false;
    }

    @Override
    public boolean getIntakeButton() {
        return false;
    }

    @Override
    public double getAngleAdjust() {
        return 0.0;
    }
}
