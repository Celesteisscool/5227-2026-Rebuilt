package frc.robot.Controls;

import edu.wpi.first.wpilibj.XboxController;

public class RebindDemo implements ControlInterface {
    // Demo to show potential for "Rebinding" controls based on some variable 
    // (in the future, sensors / buttons)

    // This demo aims to show how you can do stuff such as curve and clamp your input "control" side, 
    // preventing bulky code robot side.

    // Doing operations on inputs in the "controls" is perfered to the robot code, as you can go back later
    // and change out your "control" bindings later and have more dynamic maping

    XboxController driverController = new XboxController(0);    
   
    Boolean driveMode = !driverController.getLeftBumperButton(); 
    Boolean slowMode = driverController.getRightBumperButton(); 
    // Returns true when "driving mode", and false when in "mechanism" mode

    /**
   * Curves and clamps sticks.
   * Uses the clamping of -1 <-> 1.
   * Uses the curve of sqrt(x) in "fast mode",
   * Uses the curve of pow(x, 2) in "slow mode".
   */
    private double ccSticks(double input) {
        boolean sign = input > 0; //checks if the number is positive. True for +, False for -
        input = Math.min(Math.max(input, -1), 1); //Clamps
        
        if (!slowMode) { //Uses our default curve 
            if (sign) {input = Math.sqrt(input);}
            else {input = -Math.sqrt(input);}
        } 
        
        else if (slowMode) { // Uses a more relaxed curve, providing more control at the begining.
            input = Math.pow(input, 2);
        }

        return input;
    }

    @Override
    public double getDriveX() {
        if (driveMode) {return ccSticks(driverController.getLeftX());}
        else {return 0;} 
    }   

    @Override
    public double getDriveY() {
        if (driveMode) {return ccSticks(driverController.getLeftY());}
        else {return 0;}
    }

    @Override
    public double getDriveRot() {
        if (driveMode) {return ccSticks(driverController.getRightX());}
        else {return 0;}
    }

    @Override
    public boolean getSlowMode() {
        return slowMode;
    }

    @Override
    public int getRobotRelativeDegrees() {
        if (driveMode) {return driverController.getPOV();}
        else {return -1;}
    }

    @Override
    public boolean getShooterButton() {
        if (!driveMode) {return driverController.getAButton();}
        else {return false;}
    }

    @Override
    public boolean getIntakeButton() {
        if (!driveMode) {return driverController.getBButton();}
        else {return false;}
    }

    @Override
    public double getAngleAdjust() {
        if (!driveMode) {return driverController.getRightY();}
        else {return 0;}
    }
}
