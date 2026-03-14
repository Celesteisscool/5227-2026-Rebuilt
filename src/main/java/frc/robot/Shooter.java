package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;


public class Shooter {
    // Shooter code goes here i dont want to break the robot

    SparkMax IntakeMotor = new SparkMax(12, MotorType.kBrushless);
    SparkMax KickerMotor = new SparkMax(11, MotorType.kBrushless);

    public void shooterLogic() {

        // INTAKE 
        if (Constants.Controls.getIntakeButton()) {
            Double speed = 0.5;
            IntakeMotor.set(speed);
            KickerMotor.set(speed);
        }
        // OUTTAKE 
        else if (Constants.Controls.getOuttakeButton()) {
            Double speed = -0.5;
            IntakeMotor.set(speed);
            KickerMotor.set(speed);
        }
        // SHOOT
        else if (Constants.Controls.getShooterButton()) {
            Double speed = 0.75;
            IntakeMotor.set(speed);
            KickerMotor.set(-speed);
        }
        else {
            IntakeMotor.set(0);
            KickerMotor.set(0);
        }
    }
}
