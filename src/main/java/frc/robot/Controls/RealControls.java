package frc.robot.Controls;

import edu.wpi.first.wpilibj.XboxController;

public class RealControls implements ControlInterface {

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
}
