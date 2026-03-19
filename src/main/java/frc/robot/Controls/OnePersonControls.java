package frc.robot.Controls;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.Vision;

public class OnePersonControls implements ControlInterface {

    XboxController driverController = new XboxController(0);
    // XboxController secondaryController = new XboxController(1);

    Timer gyroResetTimer = new Timer(); // Timer for reseting gyro

    // DRIVE CONTROLS

    // Use floating-point literals so these are not truncated to zero by integer division
    double forwardSlowdown = 1.0 / 4.0; // 0.25
    double sidewaysSlowdown = 1.0 / 2.0; // 0.5
    double rotationSlowdown = 1.0 / 3.0; // ~0.333...

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
        double visionRotate = Vision.getAutoAlignRotation();
        if (autoAlignButton()) {
            return visionRotate;
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
        if (driverController.getBackButtonPressed()) {
            gyroResetTimer.reset();
            gyroResetTimer.start();

        }
        if (driverController.getBackButtonReleased()) {
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
        return (driverController.getRightTriggerAxis() > 0.5);
    }

    @Override
    public boolean getIntakeButton() {
        return (driverController.getLeftTriggerAxis() > 0.5);
    }

    @Override
    public boolean getOuttakeButton() {
        return driverController.getBButton();
    }

    @Override
    public boolean getReverseShootButton() {
        return driverController.getStartButton();
    }

    @Override
    public double getAngleAdjust() {
        return 0.0;
        // return (secondaryController.getLeftY());
    }

    

    @Override
    public boolean autoAlignButton() {
        return driverController.getAButton();
    }

    @Override
    public boolean autoAngleButton() {
        return false; // Used for debuging
        // return secondaryController.getAButton();
    }

    @Override
    public boolean zeroAngleButton() {
        return driverController.getXButton();
    }

    @Override
    public void rumble(double strength, boolean leftRumble) {
        if (leftRumble) {
            driverController.setRumble(XboxController.RumbleType.kLeftRumble, strength);
            // secondaryController.setRumble(XboxController.RumbleType.kLeftRumble, strength);
        } else {
            driverController.setRumble(XboxController.RumbleType.kRightRumble, strength);
            // secondaryController.setRumble(XboxController.RumbleType.kRightRumble, strength);
        }
    }

    @Override
    public boolean allControlersConnected() {
        return (driverController.getButtonCount() > 0);
    }
}