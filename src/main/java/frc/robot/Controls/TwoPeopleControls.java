package frc.robot.Controls;

import edu.wpi.first.wpilibj.XboxController;

public class TwoPeopleControls implements ControlInterface {

    XboxController driverController = new XboxController(0);    
    XboxController secondaryController = new XboxController(1);    
   
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
        return driverController.getLeftX();
    }   

    @Override
    public boolean getSlowMode() {
        return driverController.getRightBumperButton();
    }

    @Override
    public double getRobotRelativeDegrees() {
        return driverController.getPOV();
    }

    @Override
    public boolean getShooterButton() {
        return secondaryController.getAButton();
    }

    @Override
    public boolean getIntakeButton() {
        return secondaryController.getBButton();
    }

    @Override
    public double getAngleAdjust() {
        return secondaryController.getLeftTriggerAxis() - secondaryController.getRightTriggerAxis();
    }
}
