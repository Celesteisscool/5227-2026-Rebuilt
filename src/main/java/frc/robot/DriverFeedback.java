package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

public class DriverFeedback {
    static String driverMessage = "You got this! GLHF :D";

    public static void setupFeedback() {
        Dashboard.addEntry("Time Left In Period", 0.0);
        Dashboard.addEntry("Shooter Angle", 0.0);
        Dashboard.addEntry("Is Hub Active?", true);

        Dashboard.addEntry("Intaking", false);
        Dashboard.addEntry("Outtaking", false);
        Dashboard.addEntry("Reversing", false);

        Dashboard.addEntry("Shooting", false);
        Dashboard.addEntry("Shooter Speed", 0.0);

        Dashboard.addEntry("Hub Tag Visible", false);

        Dashboard.addEntry("Period", driverMessage);

        Dashboard.addEntry("Voltage", 0.0);
    }

    public static void updateFeedback() {
        // DASHBOARD UPDATES //
        Dashboard.updateEntry("Time Left In Period", timeLeftInPeriod());
        Dashboard.updateEntry("Is Hub Active?", isHubActive());

        Dashboard.updateEntry("Intaking", Constants.shooterClass.intaking);
        Dashboard.updateEntry("Outtaking", Constants.shooterClass.outtaking);
        Dashboard.updateEntry("Shooting", Constants.shooterClass.shooting);
        Dashboard.updateEntry("Reversing", Constants.shooterClass.reverseShoot);

        Dashboard.updateEntry("Shooter Speed", Constants.shooterClass.shooterSpeed);
        Dashboard.updateEntry("Desired Speed", Constants.shooterClass.desiredShooterSpeed);

        Dashboard.updateEntry("Shooter Angle", Constants.shooterClass.getShooterAngle());
        Dashboard.updateEntry("Desired Angle", Constants.shooterClass.desiredShooterAngle);

        Dashboard.updateEntry("Hub Tag Visible", Vision.hubVisible);

        Dashboard.updateEntry("Period", getPeriodString());

        Dashboard.updateEntry("Voltage", RobotController.getBatteryVoltage());

        // RUMBLE FEEDBACK //
        if (closeToShift() && isHubActive()) {
            Constants.Controls.rumble(0.5, true);
            Constants.Controls.rumble(0.5, false);
        } else if (closeToShift() && !isHubActive()) {
            Constants.Controls.rumble(1.0, true);
            Constants.Controls.rumble(1.0, false);
        } else {
            Constants.Controls.rumble(0.0, true);
            Constants.Controls.rumble(0.0, false);
        }

        // LEDS //
        // if (Constants.shooterClass.shooting) {
        // Constants.ledClass.setLEDGREEN(Constants.ledClass.hopper);
        // } else {
        // Constants.ledClass.setLEDOff(Constants.ledClass.hopper);
        // }
    }

    private static String getPeriodString() {
        double matchTime = DriverStation.getMatchTime();
        boolean isAutonomous = DriverStation.isAutonomousEnabled();

        if (isAutonomous) {
            return "Auto : 20";
        } else {
            if (matchTime > 130) {
                return "1/6 : 10";
            } else if (matchTime > 105) {
                return "2/6 : 25";
            } else if (matchTime > 80) {
                return "3/6 : 25";
            } else if (matchTime > 55) {
                return "4/6 : 25";
            } else if (matchTime > 30) {
                return "5/6 : 25";
            } else if (matchTime != -1) {
                return "6/6 : 30";
            } else {
                return driverMessage;
            }
        }
    }

    private static double timeLeftInPeriod() {
        double matchTime = DriverStation.getMatchTime();
        boolean isAutonomous = DriverStation.isAutonomousEnabled();

        double timerOffset = 0;

        if (!isAutonomous) {
            if (matchTime > 130) {
                timerOffset = 130; // Transition period
            } else if (matchTime > 105) {
                timerOffset = 105; // Shift 1
            } else if (matchTime > 80) {
                timerOffset = 80; // Shift 2
            } else if (matchTime > 55) {
                timerOffset = 55; // Shift 3
            } else if (matchTime > 30) {
                timerOffset = 30; // Shift 4
            } else {
                timerOffset = 0; // End game
            }
            double output = matchTime - timerOffset;
            return Math.round(output * 10.0) / 10.0; // Rounds to 1 decimal place
        } else {
            return Math.round(matchTime * 10.0) / 10.0;
        }
    }

    private static boolean closeToShift() {
        double timeLeft = timeLeftInPeriod();
        if ((timeLeft <= 5.5) && (timeLeft >= 4.5) && (timeLeft != -1)) {
            return true;
        }
        return false;
    }

    private static boolean isHubActive() { // Code from WPILIB <3
        Optional<Alliance> alliance = DriverStation.getAlliance();
        // If we have no alliance, we cannot be enabled, therefore no hub.
        if (alliance.isEmpty()) {
            return false;
        }
        // Hub is always enabled in autonomous.
        if (DriverStation.isAutonomousEnabled()) {
            return true;
        }
        // At this point, if we're not teleop enabled, there is no hub.
        if (!DriverStation.isTeleopEnabled()) {
            return false;
        }

        // We're teleop enabled, compute.
        double matchTime = DriverStation.getMatchTime();
        String gameData = DriverStation.getGameSpecificMessage();
        // If we have no game data, we cannot compute, assume hub is active, as its
        // likely early in teleop.
        if (gameData.isEmpty()) {
            return true;
        }
        boolean redInactiveFirst = false;
        switch (gameData.charAt(0)) {
            case 'R' -> redInactiveFirst = true;
            case 'B' -> redInactiveFirst = false;
            default -> {
                // If we have invalid game data, assume hub is active.
                return true;
            }
        }

        // Shift was is active for blue if red won auto, or red if blue won auto.
        boolean shift1Active = switch (alliance.get()) {
            case Red -> !redInactiveFirst;
            case Blue -> redInactiveFirst;
        };

        if (matchTime > 130) {
            // Transition shift, hub is active.
            return true;
        } else if (matchTime > 105) {
            // Shift 1
            return shift1Active;
        } else if (matchTime > 80) {
            // Shift 2
            return !shift1Active;
        } else if (matchTime > 55) {
            // Shift 3
            return shift1Active;
        } else if (matchTime > 30) {
            // Shift 4
            return !shift1Active;
        } else {
            // End game, hub always active.
            return true;
        }
    }

}
