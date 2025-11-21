package frc.robot.Controls;

public class ControlSwap2 implements ControlInterface {   
    
    @Override
    public double getDriveX() {
        return 1.0;
    }   

    @Override
    public double getDriveY() {
        return 1.0;
    }   

    @Override
    public double getDriveRot() {
        return 1.0;
    }   

    @Override
    public boolean getSlowMode() {
        return true;
    }

    @Override
    public int getRobotRelativeDegrees() {
        return 360;
    }

    @Override
    public boolean getShooterButton() {
        return true;
    }

    @Override
    public boolean getIntakeButton() {
        return true;
    }

    @Override
    public double getAngleAdjust() {
        return 1.0;
    }
}
