package frc.robot;

import java.util.Optional;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotController;

public class DriverFeedback {
    static String driverMessage = "You got this! GLHF :D";

    // ALERTS HERE //
    static Alert v10Alert = new Alert("Battery below 10v", AlertType.kWarning);
    static Alert v8Alert = new Alert("Battery below 8v", AlertType.kError);
    static Alert reverseAlert = new Alert("Reverse shooting occurred", AlertType.kWarning);
    static Alert shootWithoutHubAlert = new Alert("Shooting without an april tag visible", AlertType.kWarning);
    static Alert maxImpactAlert = new Alert("", AlertType.kWarning);
    static double maxImpact = 0;
    static Alert disconnAlert = new Alert("Controller Disconnected", AlertType.kError);

    private static void updateAlerts() {
        // These are done this way so that the alerts are sticky
        if (RobotController.getBatteryVoltage() <= 10) {
            v10Alert.set(true);
        }
        if (RobotController.getBatteryVoltage() <= 8) {
            v8Alert.set(true);
        }
        if (Constants.shooterClass.reverseShoot) {
            reverseAlert.set(true);
        }
        if (Constants.shooterClass.shooting && !Vision.hubVisible) {
            shootWithoutHubAlert.set(true);
        }

        double impact = getGyroImpact();
        if (impact > 2 && impact > maxImpact) {
            maxImpact = impact;
            maxImpactAlert.setText("Maximum impact detected: " + impact);
            maxImpactAlert.set(true);
        } 

        if (!Constants.Controls.allControlersConnected()) {
            disconnAlert.set(true);
        } else {
            disconnAlert.set(false);
        }
    }

    public static void setupDashboard() { // Setup our dashboard entries
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

        // ALERTS //
        updateAlerts();
    }

    ////////////////////
    // HELPER CLASSES //
    ////////////////////

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

    private static double getGyroImpact() {
        Pigeon2 gyro = Constants.mecanumClass.gyro;
        double x = gyro.getAccelerationX().getValueAsDouble();
        double y = gyro.getAccelerationY().getValueAsDouble();
        double impact = Math.hypot(x, y);
        return impact;
    }

}
