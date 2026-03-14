package frc.robot.Controls;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Constants;

public class TwoPersonControls implements ControlInterface {

    XboxController driverController = new XboxController(0);    
    XboxController secondaryController = new XboxController(1);    
   
    // DRIVE CONTROLS

    @Override
    public double getDriveX() {
        if (driverController.getAButton()) {
            return 0.2;
        }
        return driverController.getLeftY() / 5;
        // return ; // Might have to swap LeftX / LeftY 
    }   

    @Override
    public double getDriveY() {
        if (driverController.getBButton()) {
            return 0.2;
        }
        return driverController.getLeftX() / 5;
    }   

    @Override
    public double getDriveRot() {
        return driverController.getRightX() / 5;
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