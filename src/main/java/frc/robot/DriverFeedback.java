package frc.robot;

import java.util.Optional;

import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotController;

public class DriverFeedback {

    static String driverMessage = "You got this! GLHF :D"; // Mesage when we dont have a "period" (end of auto and end
                                                           // of teleop)

    // ALERTS HERE //
    static Alert reverseAlert = new Alert("Reverse shooting occurred", AlertType.kWarning);
    static Alert disconAlert = new Alert("Controller(s) Disconnected", AlertType.kError);
    static Alert gyroReset = new Alert("Gyro Reset", AlertType.kWarning);

    static Alert maxImpactAlert = new Alert("", AlertType.kWarning);
    static double maxImpact = 0;

    /** Sends alerts if needed */
    private static void sendAlerts() {
        // These are done in a way so that the alerts are sticky.
        if (Classes.shooterClass.reverseShoot) { // Checks if we are "reversing" our shooter
            reverseAlert.set(true); // Show the alert
        }

        double impact = getGyroImpact(); // checks the g force applied to our robot
        if (impact > 2 && impact > maxImpact) { // check if its above 2g's and if its more than the current hardest hit
            maxImpact = impact; // Update max
            maxImpactAlert.setText("Maximum impact detected: " + impact); // Change alert text
            maxImpactAlert.set(true); // Show the alert
        }

        if (!Classes.Controls.allControlersConnected()) { // Checks if our controllers *arent* connected
            disconAlert.set(true); // Show the alert
        } else {
            disconAlert.set(false); // Hide the alert
        }

        if (Classes.Controls.resetGyro()) { // Check if the button to reset our control got pressed
            gyroReset.set(true); // Shows the alert
        }
    }

    /** Sets up our dashboard entries */
    public static void setupDashboard() {
        // General Info //
        Dashboard.addEntry("Time Left In Period", -1.0);
        Dashboard.addEntry("Period", getPeriodString());
        Dashboard.addEntry("Is Hub Active?", true);
        Dashboard.addEntry("Hub Tag Visible", false);
        Dashboard.addEntry("Voltage", -1.0);

        // Shooter Info //
        Dashboard.addEntry("Shooter Speed", 0.0);
        Dashboard.addEntry("Desired Speed", 0.0);

        Dashboard.addEntry("Shooter Angle", 0.0);
        Dashboard.addEntry("Desired Angle", 0.0);
        Dashboard.addEntry("At Angle", false);

        Dashboard.addEntry("Debug Speed", 0.0);
        Dashboard.addEntry("Debug Angle", 0.0);
        Dashboard.addEntry("Debug Distance", 0.0);

        Dashboard.addEntry("Intaking", false);
        Dashboard.addEntry("Outtaking", false);
        Dashboard.addEntry("Shooting", false);
        Dashboard.addEntry("Reversing", false);

    }

    /** Updates our dashboard with the values needed. */
    public static void updateFeedback() {
        // DASHBOARD UPDATES //

        // General Info //
        Dashboard.updateEntry("Time Left In Period", timeLeftInPeriod());
        Dashboard.updateEntry("Period", getPeriodString());
        Dashboard.updateEntry("Is Hub Active?", isHubActive());
        Dashboard.updateEntry("Hub Tag Visible", Vision.isTargetVisible());
        Dashboard.updateEntry("Voltage", RobotController.getBatteryVoltage());

        // Shooter Info //
        Dashboard.updateEntry("Shooter Speed", Classes.shooterClass.shooterSpeed);
        Dashboard.updateEntry("Desired Speed", Classes.shooterClass.desiredShooterSpeed);

        Dashboard.updateEntry("Shooter Angle", Classes.shooterClass.getShooterAngle());
        Dashboard.updateEntry("Desired Angle", Classes.shooterClass.desiredShooterAngle);
        Dashboard.updateEntry("At Angle", Classes.shooterClass.atAngle);

        Dashboard.updateEntry("Intaking", Classes.shooterClass.intaking);
        Dashboard.updateEntry("Outtaking", Classes.shooterClass.outtaking);
        Dashboard.updateEntry("Shooting", Classes.shooterClass.shooting);
        Dashboard.updateEntry("Reversing", Classes.shooterClass.reverseShoot);

        // RUMBLE FEEDBACK //
        if (closeToShift() && isHubActive()) { // if the hub is active, it is most likely going to be inactive
            Classes.Controls.rumble(0.5, true); // rumble just a bit
            Classes.Controls.rumble(0.5, false);
        } else if (closeToShift() && !isHubActive()) { // if the hub is inactive, it will be active next shift
            Classes.Controls.rumble(1.0, true); // rumble ALOT
            Classes.Controls.rumble(1.0, false);
        } else {
            Classes.Controls.rumble(0.0, true); // turn off the rumble
            Classes.Controls.rumble(0.0, false);
        }

        Classes.ledClass.setLEDOff();
        // LEDS //
        if (Classes.shooterClass.shooting){
            Classes.ledClass.setLEDGreen();
        }
        else if (Classes.shooterClass.atAngle) {
            Classes.ledClass.setLEDYellow();
        }
        else if (Classes.Controls.getShootButton()){
            Classes.ledClass.setLEDRed();
        }
        // ALERTS //
        sendAlerts();
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
        Pigeon2 gyro = Classes.mecanumClass.gyro;
        double x = gyro.getAccelerationX().getValueAsDouble();
        double y = gyro.getAccelerationY().getValueAsDouble();
        double impact = Math.hypot(x, y);
        return impact;
    }

}
