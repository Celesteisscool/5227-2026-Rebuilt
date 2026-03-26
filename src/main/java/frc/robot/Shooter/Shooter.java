package frc.robot.Shooter;

import java.util.List;
import java.util.Optional;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Classes;
import frc.robot.Vision;

public class Shooter {
    SparkMax intakeMotor = new SparkMax(12, MotorType.kBrushless);
    SparkMax kickerMotor = new SparkMax(11, MotorType.kBrushless);

    TalonFX angleMotor = new TalonFX(14);
    TalonFXConfiguration angleConfig = new TalonFXConfiguration();

    Flywheel flywheel = new Flywheel();

    SparkMax spindexMotor = new SparkMax(15, MotorType.kBrushless);

    public Shooter() {
        angleConfig.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        angleMotor.getConfigurator().apply(angleConfig);
    }

    public ShooterStatus shooterStatus = new ShooterStatus();

    public double getShooterAngle() {
        return angleMotor.getPosition().getValueAsDouble();
    };

    DigitalInput angleSwitch = new DigitalInput(0);

    List<ShooterState> OldShooterValues = List.of(
            new ShooterState(2, -0.5, 0.4),
            new ShooterState(3, -1.75, 0.425),
            new ShooterState(3.5, -1.8, 0.43),
            new ShooterState(4, -2.3, 0.45),
            new ShooterState(5, -3.25, 0.52),
            new ShooterState(6, -3.25, 0.6));

    List<ShooterState> GuessedShooterValues = List.of(
            new ShooterState(2, -0.16, 2270.4),
            new ShooterState(3, -0.583, 2412.3),
            new ShooterState(3.5, -0.6, 2440.68),
            new ShooterState(4, -0.76767, 2554.2),
            new ShooterState(5, -1.083, 2951.52),
            new ShooterState(6, -1.083, 3405.6));

    /** gets values from vision and dashboard, along side reseting variables */
    private void updateVariables() {
        shooterStatus.distanceToHub = Vision.getDistanceToHub();

        shooterStatus.intaking = false;
        shooterStatus.outtaking = false;
        shooterStatus.reverseShoot = false;

        shooterStatus.desiredShooterSpeed = 0.0;
        shooterStatus.desiredShooterAngle = 0.0;
    }

    private void runIntake() {
        Double rollerSpeed = 0.6;
        Double systemTime = (System.currentTimeMillis() / 75.0); // Divided to give us slower changes

        // Set rollers on a pulse
        Double pulsedSpeed = Math.round((Math.sin(systemTime) + 1) / 2) * rollerSpeed;
        shooterStatus.intaking = true;
        intakeMotor.set(pulsedSpeed);
        kickerMotor.set(pulsedSpeed);

        spindexMotor.set(shooterStatus.globalSpindexSpeed);
    }

    private void runOuttake() {
        Double rollerSpeed = -0.5;
        shooterStatus.outtaking = true;
        intakeMotor.set(rollerSpeed);
        kickerMotor.set(rollerSpeed);

        spindexMotor.set(-shooterStatus.globalSpindexSpeed);
    }

    /** Runs the shooter using vision data */
    private void runShooter() {
        runShooter(Double.NaN);
    }

    /**
     * Runs the shooter, using a provided speed.
     * this function only runs with a input, so there is a runShooter() that passes
     * in NaN if there is no input
     */
    private void runShooter(double shooterSpeed) {
        double wantedOutputSpeed;
        if (shooterSpeed != Double.NaN) {
            // Use explicit provided speed when present
            wantedOutputSpeed = shooterSpeed;
        } else {
            // Otherwise compute based on distance
            ShooterState wantedState = ShooterInterpolator.interpolateBasedOnDistance(
                    shooterStatus.distanceToHub,
                    OldShooterValues);

            setHoodAngle(wantedState.hoodAngleDeg);

            wantedOutputSpeed = (wantedState.desiredSpeed + (shooterStatus.speedAdjust));

            // Clamp input to safe range [-1, 1]
            wantedOutputSpeed = Math.max(-1.0, Math.min(1.0, wantedOutputSpeed));
        }

        shooterStatus.desiredShooterSpeed = wantedOutputSpeed;
        flywheel.setMotorToRPM(wantedOutputSpeed);

        if (flywheel.shooterAtSpeed(wantedOutputSpeed)) {
            shooterStatus.shooting = true;
        }

        if (shooterStatus.shooting) {
            double rollerSpeed = 0.75;
            intakeMotor.set(rollerSpeed);
            kickerMotor.set(-rollerSpeed);

            spindexMotor.set(-shooterStatus.globalSpindexSpeed);
        } else {
            shooterStatus.shooting = false;
            intakeMotor.set(0);
            kickerMotor.set(0);
            spindexMotor.set(0);
        }

    }

    private void runShooterReversed() {
        shooterStatus.reverseShoot = true;
        Double rollerSpeed = -0.75;
        intakeMotor.set(rollerSpeed);
        kickerMotor.set(-rollerSpeed);
    }

    private void zeroAngleMotor() {
        setHoodAngle(10);
    }

    private void turnAllMotorsOff() {
        intakeMotor.set(0);
        kickerMotor.set(0);
        flywheel.shooterMotor.set(0);
        angleMotor.set(0);
        spindexMotor.set(0);
    }

    private void adjustSpeedManually() {
        double changeAmount = 100; // 100 RPM
        if (Classes.Controls.speedAdjustUp()) {
            shooterStatus.speedAdjust += changeAmount;
        }
        if (Classes.Controls.speedAdjustDown()) {
            shooterStatus.speedAdjust -= changeAmount;
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
        } else if (Classes.Controls.debugButton()) {
            runShooter(2000);
        } else {
            shooterStatus.shooting = false;
            turnAllMotorsOff();
        }
        flywheel.getMotorRPM(); // Update shooter speed

        adjustSpeedManually();

    }

    public void adjustAngle(double speed) { // used for limit switches.
        speed = Math.max(-1.0, Math.min(1.0, speed)); // clamp input to safe range [-1, 1]

        double maxSpeed = 0.015;
        speed = speed * maxSpeed;

        if (!angleSwitch.get() && (speed > 0)) { // if we are trying to move down and the switch is pressed, dont move
            angleMotor.set(0);
            angleMotor.setPosition(0);
        } else if (getShooterAngle() <= -2.6 && (speed < 0)) {
            angleMotor.set(0);
        } else {
            angleMotor.set(speed);
        }
    }

    public void setHoodAngle(double position) {
        shooterStatus.atAngle = false;

        shooterStatus.desiredShooterAngle = position;
        double error = position - getShooterAngle();
        if (Math.abs(error) <= 0.075) { // if we are close enough to the target, stop moving
            adjustAngle(0);
            shooterStatus.atAngle = true;
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
}
