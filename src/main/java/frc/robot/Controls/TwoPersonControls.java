package frc.robot.Controls;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Vision;

public class TwoPersonControls implements ControlInterface {

    XboxController driverController = new XboxController(0);
    XboxController secondaryController = new XboxController(1);

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
        boolean slowMode = (driverController.getRightBumperButton() | // Either bumper can be used for slow mode
                driverController.getLeftBumperButton());
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
        } else
            return false;
    }

    // SHOOTER CONTROLS

    @Override
    public boolean getShootButton() {
        return (secondaryController.getRightTriggerAxis() > 0.5);
    }

    @Override
    public boolean getIntakeButton() {
        return (secondaryController.getLeftTriggerAxis() > 0.5);
    }

    @Override
    public boolean getOuttakeButton() {
        return secondaryController.getBButton();
    }

    @Override
    public boolean getReverseShootButton() {
        return secondaryController.getStartButton();
    }

    @Override
    public double getAngleAdjust() {
        return (secondaryController.getLeftY());
    }

    

    @Override
    public boolean autoAlignButton() {
        return driverController.getAButton();
    }

    @Override
    public boolean autoAngleButton() {
        return secondaryController.getAButton();
    }

    @Override
    public void rumble(double strength, boolean leftRumble) {
        if (leftRumble) {
            driverController.setRumble(XboxController.RumbleType.kLeftRumble, strength);
            secondaryController.setRumble(XboxController.RumbleType.kLeftRumble, strength);
        } else {
            driverController.setRumble(XboxController.RumbleType.kRightRumble, strength);
            secondaryController.setRumble(XboxController.RumbleType.kRightRumble, strength);
        }
    }

    @Override
    public boolean allControlersConnected() {
        return (driverController.getButtonCount() > 0 && secondaryController.getButtonCount() > 0);
    }
}