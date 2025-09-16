package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;

import com.revrobotics.spark.SparkMax;

public class Shooter {
    SparkMax shooterMotorA = new SparkMax(21, MotorType.kBrushless); 
    SparkMax shooterMotorB = new SparkMax(22, MotorType.kBrushless);

    SparkMax intakeMotor = new SparkMax(23, MotorType.kBrushless);

    Servo hoodServo = new Servo(3);

    double desiredAngle = 0;
    

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


    public void shooterWithHoodServo() {
        if (Dashboard.getEntry("Hood Angle", Double.class) == null) {
            Dashboard.addEntry("Hood Angle", Double.class);
        } 

        desiredAngle += Constants.Controls.getAngleAdjust(); //Should be -1 to 1, this might need a mult
        desiredAngle = Math.min(90, Math.max(0, desiredAngle)); // Clamps from 0 to 90, not sure what will get reported
        hoodServo.setAngle(desiredAngle); // Updates the setpoint
        Dashboard.updateEntry("Hood Angle", hoodServo.getAngle());
    }
}
