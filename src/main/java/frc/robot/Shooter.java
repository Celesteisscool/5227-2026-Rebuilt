package frc.robot;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.DigitalInput;

public class Shooter {
    private final SparkMax angleMotor = new SparkMax(5, MotorType.kBrushless);
    private final SparkMaxConfig angleConfig;
    private final DigitalInput angleLimitSwitch = new DigitalInput(0);

    private final SparkMax shootMotor = new SparkMax(6, MotorType.kBrushless);
    private final SparkMaxConfig shotConfig;
    // SparkClosedLoopController shotController = shootMotor.getClosedLoopController();


    private final SparkMax intakeMotor = new SparkMax(7, MotorType.kBrushless);
    private final SparkMax feederMotor = new SparkMax(8, MotorType.kBrushless);


    public Shooter() {
        angleConfig = new SparkMaxConfig();
        shotConfig = new SparkMaxConfig();
        // shotConfig.closedLoop.pid(0.01, 0, 0.001);
        
        Dashboard.addEntry("Shooter Angle", 0.0);
        Dashboard.addEntry("Shooter Speed", 0.0);
        Dashboard.addEntry("Desired Speed", 0.0);
    }

    public void changeAngle(double adjustAmount) {
        adjustAmount = adjustAmount / 10; // Scale down for finer control
        if (angleLimitSwitch.get() && adjustAmount < 0) {
            angleMotor.getEncoder().setPosition(0); // Reset position when limit switch is hit
            angleMotor.set(0);
        } else {
            angleMotor.set(adjustAmount);
        }
        Dashboard.updateEntry("Shooter Angle", angleMotor.getEncoder().getPosition());
    }

    public double runShooter(double speed) {
        shootMotor.set(speed/5676); // Assuming 5676 is max RPM for normalization
        Dashboard.updateEntry("Desired Speed", speed);
        Dashboard.updateEntry("Shooter Speed", shootMotor.getEncoder().getVelocity());

        return shootMotor.getEncoder().getVelocity();
    }

    public void shooterFunction() {
        if (Constants.Controls.getShooterButton()) {
            double speed = runShooter(3000); // Example speed value
            if (speed > 2900 && speed < 3100) { // Check if within acceptable range
                feederMotor.set(-0.5); 
                intakeMotor.set(0.5);
            } else {
                feederMotor.set(0);
                intakeMotor.set(0);
            }
        }
        else if (Constants.Controls.getIntakeButton()) {
            runShooter(0);
            intakeMotor.set(0.5);
            feederMotor.set(0.5);
        } else {
            runShooter(0);
            intakeMotor.set(0);
            feederMotor.set(0);            
        }
    }
}
