package frc.robot.Controls;

import frc.robot.Classes;

public class AutoControls implements ControlInterface {
    // DRIVE CONTROLS

    // Use floating-point literals so these are not truncated to zero by integer
    // division
    double forwardSlowdown = 1.0 / 4.0; // 0.25
    double sidewaysSlowdown = 1.0 / 2.0; // 0.5
    double rotationSlowdown = 1.0 / 3.0; // ~0.333...

    @Override
    public double getDriveX() {
        return -(Classes.autoClass.forwardInput * forwardSlowdown);
    }

    @Override
    public double getDriveY() {
        return (Classes.autoClass.sidewaysInput * sidewaysSlowdown);
    }

    @Override
    public double getDriveRot() {
        return (Classes.autoClass.rotationInput * rotationSlowdown);
    }

    @Override
    public boolean getSlowMode() {
        return false;
    }

    @Override
    public boolean getBreakMode() {
        return Classes.autoClass.breakingInput;
    }

    @Override
    public boolean resetGyro() {
        return false;
    }

    @Override
    public boolean autoAlignButton() {
        return false;
    }

    // SHOOTER CONTROLS

    @Override
    public boolean getShootButton() {
        return Classes.autoClass.shootInput;
    }

    @Override
    public boolean getIntakeButton() {
        return Classes.autoClass.intakeInput;
    }

    @Override
    public boolean getReverseShootButton() {
        return false;
    }

    @Override
    public boolean getOuttakeButton() {
        return false;
    }

    @Override
    public double getAngleAdjust() {
        return 0.0;
    }

    @Override
    public boolean autoAngleButton() {
        return false;
    }

    @Override
    public boolean zeroAngleButton() {
        return Classes.autoClass.zeroAngleInput;
    }

    // Helper Functions
    @Override
    public void rumble(double strength, boolean leftRumble) {
        // Nothing
    }

    @Override
    public boolean allControlersConnected() {
        return true;
    }

}