package frc.robot.Shooter;

import java.util.List;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Classes;
import frc.robot.Vision;

public class Shooter {
    SparkMax intakeMotor = new SparkMax(12, MotorType.kBrushless);
    SparkMax kickerMotor = new SparkMax(11, MotorType.kBrushless);

    TalonFX angleMotor = new TalonFX(14);
    TalonFX shooterMotor = new TalonFX(13);

    SparkMax spindexMotor = new SparkMax(15, MotorType.kBrushless);

    public double getShooterAngle() {
        return angleMotor.getPosition().getValueAsDouble();
    };

    public boolean shooting = false; // whether we are currently trying to shoot
    public boolean intaking = false; // whether we are currently trying to intake
    public boolean outtaking = false; // whether we are currently trying to outtake
    public boolean reverseShoot = false; // whether we are currently trying to reverse shoot
    public boolean atAngle = false;

    public boolean atSpeed = false; // whether we are at speed or not, used for driver feedback and other things
    public double shooterSpeed = 0; // current shooter speed as percentage of max RPM

    public double desiredShooterSpeed = 0; // the speed we want to be at, used for driver feedback and other things
    public double desiredShooterAngle = 0; // the angle we want to be at, used for driver feedback and other things

    public double speedAdjust = 0.0; // start at +0%

    private double globalSpindexSpeed = 0.2; // im lazy :>

    private Double distanceToHub = Double.NaN;

    DigitalInput angleSwitch = new DigitalInput(0);

    public ShooterState interpolateBasedOnDistance(double distance) {
        List<ShooterState> OldShooterValues = List.of(
                new ShooterState(2, -0.5, 0.4),
                new ShooterState(3, -1.75, 0.425),
                new ShooterState(3.5, -1.8, 0.43),
                new ShooterState(4, -2.3, 0.45),
                new ShooterState(5, -3.25, 0.52),
                new ShooterState(6, -3.25, 0.6));
        if (distance == Double.NaN) {
            return new ShooterState(0, 0, 0); // default value if we dont see the hub, change this if you want
        }
        return ShooterInterpolator.interpolate(OldShooterValues, distance);
    }

    public void applyShooterState(ShooterState state) {
        setAngle(state.hoodAngleDeg);
        if (atAngle) {
            runShooter(state.flywheelSpeed + (speedAdjust / 100.0));
        } else {
            intakeMotor.set(0);
            kickerMotor.set(0);
            shooterMotor.set(0);
        }
    }

    /** gets values from vision and dashboard, along side reseting variables */
    private void updateVariables() {
        distanceToHub = Vision.getDistanceToHub();

        shooting = false;
        intaking = false;
        outtaking = false;
        reverseShoot = false;

        desiredShooterSpeed = 0.0;
        desiredShooterAngle = 0.0;
    }

    private void runIntake() {
        Double rollerSpeed = 0.6;
        Double systemTime = (System.currentTimeMillis() / 75.0); // Divided to give us slower changes

        // Set rollers on a pulse
        Double pulsedSpeed = Math.round((Math.sin(systemTime) + 1) / 2) * rollerSpeed;
        intaking = true;
        intakeMotor.set(pulsedSpeed);
        kickerMotor.set(pulsedSpeed);

        spindexMotor.set(globalSpindexSpeed);
    }

    private void runOuttake() {
        Double rollerSpeed = -0.5;
        outtaking = true;
        intakeMotor.set(rollerSpeed);
        kickerMotor.set(rollerSpeed);

        spindexMotor.set(-globalSpindexSpeed);
    }

    private void runShooter() {
        applyShooterState(
                interpolateBasedOnDistance(distanceToHub));
    }

    private void runShooterReversed() {
        reverseShoot = true;
        Double rollerSpeed = -0.75;
        intakeMotor.set(rollerSpeed);
        kickerMotor.set(-rollerSpeed);
    }

    private void zeroAngleMotor() {
        setAngle(10);
    }

    private void turnAllMotorsOff() {
        intakeMotor.set(0);
        kickerMotor.set(0);
        shooterMotor.set(0);
        angleMotor.set(0);
        spindexMotor.set(0);
    }

    private void adjustSpeedManually() {
        double changeAmount = 2;
        if (Classes.Controls.speedAdjustUp()) {
            speedAdjust += changeAmount;
        }
        if (Classes.Controls.speedAdjustDown()) {
            speedAdjust -= changeAmount;
        }
    }

    public void shooterLoopLogic() {
        updateVariables();

        if (Classes.Controls.getIntakeButton()) {
            runIntake();
        } else if (Classes.Controls.getOuttakeButton()) {
            runOuttake();
        } else if (Classes.Controls.getShootButton()) {
            runShooter();
        } else if (Classes.Controls.getReverseShootButton()) {
            runShooterReversed();
        } else if (Classes.Controls.zeroAngleButton()) {
            zeroAngleMotor();
        } else {
            turnAllMotorsOff();
        }

        adjustSpeedManually();

    }

    public void runShooter(double wantedSpeed) {

        // clamp input to safe range [-1, 1]
        wantedSpeed = Math.max(-1.0, Math.min(1.0, wantedSpeed));

        desiredShooterSpeed = wantedSpeed;
        wantedSpeed = wantedSpeed * -1;

        // set shooter motor to requested percent output
        if (getShooterSpeed() <= (wantedSpeed - 0.15)) {
            shooterMotor.set(1);
        } else {
            shooterMotor.set(wantedSpeed);
        }

        if (shooterAtSpeed(wantedSpeed)) {
            shooting = true;
            // simple roller/kicker behavior: run them when shooting is requested
            double rollerSpeed = 0.75;
            intakeMotor.set(rollerSpeed);
            kickerMotor.set(-rollerSpeed);

            spindexMotor.set(-globalSpindexSpeed); // Set our spindexer
        } else { // Turn off roller/kicker if we're not at speed, prevents jamming and shooting
                 // too early
            shooting = false;
            intakeMotor.set(0);
            kickerMotor.set(0);
        }

    }

    public void adjustAngle(double speed) { // used for limit switches.
        speed = Math.max(-1.0, Math.min(1.0, speed)); // clamp input to safe range [-1, 1]

        double maxSpeed = 0.03;
        speed = speed * maxSpeed; // Cap our speed at 0.03, done this way to give more control

        if (!angleSwitch.get() && (speed > 0)) { // if we are trying to move down and the switch is pressed, dont move
            angleMotor.set(0);
            angleMotor.setPosition(0);
        } else if (getShooterAngle() < -5.2 && (speed < 0)) {
            angleMotor.set(0);
        } else {
            angleMotor.set(speed);
        }
    }

    public void setAngle(double position) {
        atAngle = false;

        desiredShooterAngle = position;
        double error = position - getShooterAngle();
        if (Math.abs(error) <= 0.075) { // if we are close enough to the target, stop moving
            adjustAngle(0);
            atAngle = true;
            return;
        }

        if (error > 0) { // if we need to move up, set speed positive, otherwise negative
            adjustAngle(1);
        } else if (error < 0) {
            adjustAngle(-1);
        } else {
            adjustAngle(0);
        }

    }

    private double getShooterSpeed() {
        return (shooterMotor.getVelocity().getValueAsDouble() / 512);
    }

    public boolean shooterAtSpeed(double speed) { // checks if we are in a RANGE for our shooter, not just if its
                                                  // exactly equal
        double variance = 10.0; // 10% variance allowed
        shooterSpeed = getShooterSpeed();
        return (Math.abs(shooterSpeed - speed) <= (1.0 / variance));
    }
}
