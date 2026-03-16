package frc.robot;

import java.util.Optional;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

// This class will be stuff for the drivers, like on screen information
public class Drivestation {

    public static void setupDrivestation() {
        Dashboard.addEntry("timeLeftInPeriod", 0.0);
        Dashboard.addEntry("Warning", false);
    }

    public static void updateDrivestation() {
        Dashboard.updateEntry("timeLeftInPeriod", timeLeftInPeriod());
        Dashboard.updateEntry("Warning", isWarning());
    }

    private static double timeLeftInPeriod() {
        double matchTime = DriverStation.getMatchTime();
        boolean isAutonomous = DriverStation.isAutonomousEnabled();

        double timerOffset = 0;

        if (!isAutonomous) {
            if (matchTime > 130) {
                timerOffset = 130;
            } else if (matchTime > 105) {
                timerOffset = 105;
            } else if (matchTime > 80) {
                timerOffset = 80;
            } else if (matchTime > 55) {
                timerOffset = 55;
            } else if (matchTime > 30) {
                timerOffset = 30;
            } else {
                timerOffset = 0;
            }
            double output = matchTime - timerOffset;
            return Math.round(output * 10.0) / 10.0; // Rounds to 1 decimal place
        } else {
            return Math.round(matchTime * 10.0) / 10.0;
        }
    }

    private static boolean isWarning() { // Will get used for rumble
        double timeLeft = timeLeftInPeriod();
        if (timeLeft <= 6 && timeLeft > 4 && timeLeft != -1) {
            return true;
        }
        return false;
    }

    private boolean isHubActive() { // Example code
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
