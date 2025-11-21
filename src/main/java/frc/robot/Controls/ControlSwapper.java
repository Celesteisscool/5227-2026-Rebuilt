package frc.robot.Controls;

import edu.wpi.first.wpilibj.XboxController;

public class ControlSwapper implements ControlInterface {

    static XboxController driverController = new XboxController(0);    
    

    // This lets you dynamicly update your controls! 
    // By having a static method that returns the desired control scheme,
    // You can easily swap between control schemes based on button presses or other conditions.
    // While it is true that this can take up more space (2 files instead of one),
    // It greatly enhances code readability and maintainability.
    // (Anyways you can just put the files in a folder)
    public static ControlInterface control() { 
        if (driverController.getAButton()) {
            return new ControlSwap2();
        } else {
            return new ControlSwap1();
        }
    }

    @Override
    public double getDriveX() {
        return control().getDriveX();
    }   

    @Override
    public double getDriveY() {
        return control().getDriveY();
    }   

    @Override
    public double getDriveRot() {
        return control().getDriveRot();
    }   

    @Override
    public boolean getSlowMode() {
        return control().getSlowMode();
    }

    @Override
    public int getRobotRelativeDegrees() {
        return control().getRobotRelativeDegrees();
    }

    @Override
    public boolean getShooterButton() {
        return control().getShooterButton();
    }

    @Override
    public boolean getIntakeButton() {
        return control().getIntakeButton();
    }

    @Override
    public double getAngleAdjust() {
        return control().getAngleAdjust();
    }
}
