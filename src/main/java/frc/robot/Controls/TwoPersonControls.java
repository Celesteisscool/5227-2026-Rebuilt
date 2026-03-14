package frc.robot.Controls;

import edu.wpi.first.wpilibj.XboxController;

public class TwoPersonControls implements ControlInterface {

    XboxController driverController = new XboxController(0);    
    XboxController secondaryController = new XboxController(1);    
   
    // DRIVE CONTROLS

    double forwardSlowdown = 4;
    double sidewaysSlowdown = 2;
    double rotationSlowdown = 3;

    @Override
    public double getDriveX() {
        return - (driverController.getLeftY() / forwardSlowdown);
        // return ; // Might have to swap LeftX / LeftY 
    }   

    @Override
    public double getDriveY() {
        if (driverController.getBButton()) {
            return 0.2;
        }
        return (driverController.getLeftX() / sidewaysSlowdown);
    }   

    @Override
    public double getDriveRot() {
        return (driverController.getRightX() / rotationSlowdown);
    }   

    @Override
    public boolean getSlowMode() {
        boolean slowMode = (
            driverController.getRightBumperButton() | // Either bumper can be used for slow mode
            driverController.getLeftBumperButton() );
        return slowMode;
    }

    @Override
    public boolean resetGyro() {
        return driverController.getYButton();
    }

    // SHOOTER CONTROLS

    @Override
    public boolean getShooterButton() {
        return (secondaryController.getRightTriggerAxis() > 0.5);
    }

    @Override
    public boolean getIntakeButton() {
        return secondaryController.getLeftTriggerAxis() > 0.5;
    }

    @Override 
    public boolean getOuttakeButton() {
        return secondaryController.getBButton();
    }

    @Override
    public boolean getReverseShooterButton() {
        return secondaryController.getStartButton();
    }

    @Override
    public double getAngleAdjust() {
        return (secondaryController.getLeftY()) * 0.02; // VERY SLOW!!!
    }

    
}