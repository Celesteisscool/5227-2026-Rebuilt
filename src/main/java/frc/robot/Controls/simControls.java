package frc.robot.Controls;

import edu.wpi.first.wpilibj.Joystick;

public class simControls implements ControlInterface {

    Joystick driverController = new Joystick(0);    
   
    @Override
    public double getDriveX() {
        return driverController.getX();
    }   

    @Override
    public double getDriveY() {
        return driverController.getY();
    }   

    @Override
    public double getDriveRot() {
        return driverController.getZ();
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
        return driverController.getRawButton(0);
    }

    @Override
    public boolean getIntakeButton() {
        return driverController.getRawButton(1);
    }

    @Override
    public double getAngleAdjust() {
        return 0;
    }
}
