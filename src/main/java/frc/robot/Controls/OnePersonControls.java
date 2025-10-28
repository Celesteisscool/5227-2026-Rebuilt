package frc.robot.Controls;

import edu.wpi.first.wpilibj.XboxController;

public class OnePersonControls implements ControlInterface {

    XboxController driverController = new XboxController(0);    
   
    @Override
    public double getDriveX() {
        return driverController.getLeftX();
    }   

    @Override
    public double getDriveY() {
        return driverController.getLeftY();
    }   

    @Override
    public double getDriveRot() {
        return driverController.getRightX();
    }   

    @Override
    public boolean getSlowMode() {
        return driverController.getRightBumperButton();
    }

    @Override
    public int getRobotRelativeDegrees() {
        return driverController.getPOV();
    }

    @Override
    public boolean getShooterButton() {
        return driverController.getAButton();
    }

    @Override
    public boolean getIntakeButton() {
        return driverController.getBButton();
    }

    @Override
    public double getAngleAdjust() {
        return driverController.getLeftTriggerAxis() - driverController.getRightTriggerAxis();
    }
}
