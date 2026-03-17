package frc.robot.Controls;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Vision;

public class OnePersonControls implements ControlInterface {

    XboxController driverController = new XboxController(0);
    // XboxController secondaryController = new XboxController(1);

    Timer gyroResetTimer = new Timer(); // Timer for reseting gyro

    // DRIVE CONTROLS

    double forwardSlowdown = 1 / 4;
    double sidewaysSlowdown = 1 / 2;
    double rotationSlowdown = 1 / 3;

    @Override
    public double getDriveX() {
        return -(driverController.getLeftY() * forwardSlowdown);
    }

    @Override
    public double getDriveY() {
        return (driverController.getLeftX() * sidewaysSlowdown);
    }

    @Override
    public double getDriveRot() {
        if (autoAlignButton()) {
            return Vision.getAutoAlignRotation();
        } else {
            return (driverController.getRightX() * rotationSlowdown);
        }
    }

    @Override
    public boolean getSlowMode() {
        boolean slowMode = (driverController.getLeftStickButton());
        return slowMode;
    }

    @Override
    public boolean resetGyro() {
        if (driverController.getStartButtonPressed()) {
            gyroResetTimer.reset();
            gyroResetTimer.start();

        }
        if (driverController.getStartButtonReleased()) {
            gyroResetTimer.stop();
        }

        if (gyroResetTimer.get() > 0.5) {
            gyroResetTimer.stop();
            gyroResetTimer.reset();
            return true; // Only reset gyro if start button is held for more than 0.5 seconds, to prevent
                         // accidental resets
        } else {
            return false;
        }
    }

    // SHOOTER CONTROLS

    @Override
    public boolean getShootButton() {
        return (driverController.getRightBumperButton());
    }

    @Override
    public boolean getIntakeButton() {
        return (driverController.getLeftBumperButton());
    }

    @Override
    public boolean getOuttakeButton() {
        return driverController.getBButton();
    }

    @Override
    public boolean getReverseShootButton() {
        return driverController.getBackButton();
    }

    @Override
    public double getAngleAdjust() {
        return (driverController.getRightTriggerAxis() - driverController.getLeftTriggerAxis()); // Adjusts angle based
                                                                                                 // on triggers, with a
                                                                                                 // max adjustment of
                                                                                                 // 0.5 degrees per loop
    }

    @Override
    public void rumble(double strength, boolean leftRumble) {
        if (leftRumble) {
            driverController.setRumble(XboxController.RumbleType.kLeftRumble, strength);
        } else {
            driverController.setRumble(XboxController.RumbleType.kRightRumble, strength);
        }
    }

    @Override
    public boolean autoAlignButton() {
        return driverController.getAButton();
    }

    @Override
    public boolean autoAngleButton() {
        return driverController.getXButton();
    }

    @Override
    public boolean allControlersConnected() {
        return (driverController.getButtonCount() > 0);
    }

    @Override
    public boolean zeroAngleButton() {
        return false;
    }

}