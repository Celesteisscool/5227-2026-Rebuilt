package frc.robot.Controls;

import edu.wpi.first.wpilibj.XboxController;

public class TwoPersonControls implements ControlInterface {

    XboxController driverController = new XboxController(0);    
    XboxController secondaryController = new XboxController(1);    
   
    // DRIVE CONTROLS

    @Override
    public double getDriveX() {
        return driverController.getLeftX(); // Might have to swap LeftX / LeftY 
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
        boolean slowMode = (
            driverController.getRightBumperButton() | // Either bumper can be used for slow mode
            driverController.getLeftBumperButton() );
        return slowMode;
    }

    // SHOOTER CONTROLS

    @Override
    public boolean getShooterButton() {
        return secondaryController.getAButton();
    }

    @Override
    public boolean getIntakeButton() {
        return secondaryController.getBButton();
    }
}