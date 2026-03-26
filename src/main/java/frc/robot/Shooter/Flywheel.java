package frc.robot.Shooter;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.Classes;

/**
 * Provides control for the flywheel motor. Used so that we can have a pid
 * controller, while still keeping the code relativly simple.
 */
public class Flywheel {
    TalonFX shooterMotor = new TalonFX(13);

    TalonFXConfiguration shooterConfig = new TalonFXConfiguration();
    Slot0Configs shooterSlot0Configs = shooterConfig.Slot0;

    final VelocityVoltage velocityRequest = new VelocityVoltage(0);

    public Flywheel() {
        shooterSlot0Configs.kV = 0.11; // ReCalc kV
        shooterSlot0Configs.kA = 4.19; // ReCalc kA (Handles inertia during spin-up)
        shooterSlot0Configs.kP = 0.27; // ReCalc kP
        shooterSlot0Configs.kI = 0.0;
        shooterSlot0Configs.kD = 0.0;

        shooterMotor.getConfigurator().apply(shooterConfig);
    }


    public void setMotorToRPM(double targetRPM) {
        Classes.shooterClass.shooterStatus.desiredShooterSpeed = targetRPM;
        double targetRPS = targetRPM / 60.0;
        shooterMotor.setControl(velocityRequest.withVelocity(-targetRPS));
    }


    public double getMotorRPM() {
        double shooterRPS = shooterMotor.getVelocity().getValueAsDouble();
        double shooterRPM = shooterRPS * 60.0;
        Classes.shooterClass.shooterStatus.shooterSpeed = shooterRPM;
        return shooterRPM;
    }

    public boolean shooterAtSpeed(double desiredRPM) {
        return Math.abs(desiredRPM - (-getMotorRPM())) < 200.0;
    }

    
}
