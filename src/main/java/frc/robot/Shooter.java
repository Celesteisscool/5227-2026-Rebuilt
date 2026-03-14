package frc.robot;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;


public class Shooter {
    // Shooter code goes here i dont want to break the robot

    SparkMax IntakeMotor = new SparkMax(12, MotorType.kBrushless);
    SparkMax KickerMotor = new SparkMax(11, MotorType.kBrushless);

    SparkMax AngleMotor = new SparkMax(14, MotorType.kBrushless);
    SparkMax ShooterMotor = new SparkMax(13, MotorType.kBrushless);

    DigitalInput angleSwitch = new DigitalInput(0);

    public void shooterLoopLogic() {

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
            runShooter(0.4); 
        }

        // REVERSE SHOOTER
        else if (Constants.Controls.getReverseShooterButton()) {
            Double speed = -0.75;
            IntakeMotor.set(speed);
            KickerMotor.set(-speed);
        }
        // DEFAULT TO NOT MOVING
        else {
            IntakeMotor.set(0);
            KickerMotor.set(0);
            ShooterMotor.set(0);
        }

        adjustAngle(Constants.Controls.getAngleAdjust());
    }



    public void runShooter(double speed) { 
        speed = speed * -1;
        // clamp input to safe range [-1, 1]
        speed = Math.max(-1.0, Math.min(1.0, speed));


        // set shooter motor to requested percent output
        ShooterMotor.set(speed);


        if (atSpeed(speed)) {
            // simple roller/kicker behavior: run them when shooting is requested
            double rollerSpeed = 0.75;
            IntakeMotor.set(rollerSpeed);
            KickerMotor.set(-rollerSpeed);
        }
    }




    public void adjustAngle(double speed) { // used for limit switches.
        if (!angleSwitch.get() && (speed > 0)) { // if we are trying to move down and the switch is pressed, dont move
            AngleMotor.set(0);
            AngleMotor.getEncoder().setPosition(0);
        }
        else if (AngleMotor.getEncoder().getPosition() < -5.2 && (speed < 0)) {
            AngleMotor.set(0);
        }
        else { 
            AngleMotor.set(speed);
        }
        Dashboard.updateEntry("angle", AngleMotor.getEncoder().getPosition());
        
    }

    public boolean atSpeed(double speed) { // checks if we are in a RANGE for our shooter, not just if its exactly equal
        double variance = 10; // 10% variance allowed
        double shooterSpeed = ( ShooterMotor.getEncoder().getVelocity() / 5676 ); // get current shooter speed as percentage of max RPM
        return (Math.abs(shooterSpeed - speed) <= (1/variance));
    }
}
