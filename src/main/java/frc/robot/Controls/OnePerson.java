package frc.robot.Controls;

import edu.wpi.first.wpilibj.XboxController;

public class OnePerson implements ControlInterface {

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
    public double getDriveAngle() {
        // double rightX = driverController.getRightX();
        // double rightY = driverController.getRightY();
        // double angle = Math.atan2(rightX,rightY);
        // return Math.toDegrees(angle);
        return 0.0;
    }

    @Override
    public boolean useDriveAngle() {
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
        return driverController.getRawButton(0);
    }

    @Override
    public boolean getIntakeButton() {
        return driverController.getRawButton(1);
    }
}