package frc.robot.Controls;

import edu.wpi.first.wpilibj.XboxController;

public class TwoPersonControls implements ControlInterface {

    XboxController driverController = new XboxController(0);    
    XboxController secondaryController = new XboxController(0);    
   
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
    public double getDriveAngle() { // Not used in this control mode
        return -1;
    }

    @Override
    public boolean useDriveAngle() { // Not used in this control mode
        return false;
    }

    @Override
    public boolean getSlowMode() {
        boolean slowMode = (
            driverController.getRightBumperButton() | 
            driverController.getLeftBumperButton() );
        return slowMode;
    }

    

    @Override
    public int getRobotRelativeDegrees() {
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
}