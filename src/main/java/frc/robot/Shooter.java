package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

public class Shooter {
    SparkMax shooterMotorA = new SparkMax(21, MotorType.kBrushless); 
    SparkMax shooterMotorB = new SparkMax(22, MotorType.kBrushless);

    SparkMax intakeMotor = new SparkMax(23, MotorType.kBrushless);


    public void normalShooter() {
        if (Constants.Controls.getShooterButton()) {
            shooterMotorA.set(0.5);
            shooterMotorB.set(0.5);
        } else {
            shooterMotorA.set(0);
            shooterMotorB.set(0);
        }

        if (Constants.Controls.getIntakeButton()) {
            intakeMotor.set(0.5);
        } else {
            intakeMotor.set(0);
        }
    }

    public void flywheelShooter() {
        if (Constants.Controls.getShooterButton()) {
            shooterMotorA.set(1);
        } else if (shooterMotorA.getEncoder().getVelocity() > 6000) {
            shooterMotorB.set(0.5);
        }
        else {
            shooterMotorA.set(0);
            shooterMotorB.set(0);
        }

        if (Constants.Controls.getIntakeButton()) {
            intakeMotor.set(0.5);
        } else {
            intakeMotor.set(0);
        }
    }
}
