package frc.robot.Shooter;

import java.util.List;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import frc.robot.Dashboard;
import frc.robot.Vision;

public class Shooter {
    // Shooter code goes here i dont want to break the robot

    SparkMax IntakeMotor = new SparkMax(12, MotorType.kBrushless);
    SparkMax KickerMotor = new SparkMax(11, MotorType.kBrushless);

    SparkMax AngleMotor = new SparkMax(14, MotorType.kBrushless);
    SparkMax ShooterMotor = new SparkMax(13, MotorType.kBrushless);

    DigitalInput angleSwitch = new DigitalInput(0);

    private ShooterState getWantedShooterState() { // Returns the interpolated value
        List<ShooterState> FakeValues = List.of( // CHANGE THESE!!!
                new ShooterState(1.5, 45, 0.3),
                new ShooterState(3.0, 35, 0.4),
                new ShooterState(4.5, 25, 0.5),
                new ShooterState(6.0, 18.0, 0.6));
        if (Vision.getDistanceToHub() == Double.NaN) {
            return new ShooterState(0, 0, 0); // default value if we dont see the hub, change this if you want
        }
        return ShooterInterpolator.interpolate(FakeValues, Vision.getDistanceToHub());
    }

    

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
        else if (Constants.Controls.getShootButton()) {
            runShooter(0.4);
        }
        // REVERSE SHOOTER
        else if (Constants.Controls.getReverseShootButton()) {
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

    }

    public void runShooter(double speed) {
        speed = speed * -1;
        // clamp input to safe range [-1, 1]
        speed = Math.max(-1.0, Math.min(1.0, speed));

        // set shooter motor to requested percent output
        ShooterMotor.set(speed);

        if (shooterAtSpeed(speed)) {
            // simple roller/kicker behavior: run them when shooting is requested
            double rollerSpeed = 0.75;
            IntakeMotor.set(rollerSpeed);
            KickerMotor.set(-rollerSpeed);
        } else { // Turn off roller/kicker if we're not at speed, prevents jamming and shooting
                 // too early
            IntakeMotor.set(0);
            KickerMotor.set(0);
        }
    }

    public void adjustAngle(double speed) { // used for limit switches.
        speed = Math.max(-1.0, Math.min(1.0, speed)); // clamp input to safe range [-1, 1]

        double maxSpeed = 0.02;
        speed = speed * maxSpeed; // Cap our speed at 0.02, done this way to give more control

        if (!angleSwitch.get() && (speed > 0)) { // if we are trying to move down and the switch is pressed, dont move
            AngleMotor.set(0);
            AngleMotor.getEncoder().setPosition(0);
        } else if (AngleMotor.getEncoder().getPosition() < -5.2 && (speed < 0)) {
            AngleMotor.set(0);
        } else {
            AngleMotor.set(speed);
        }
        Dashboard.updateEntry("angle", AngleMotor.getEncoder().getPosition());
    }

    public void moveAngleTo(double position) {
        double currentPosition = AngleMotor.getEncoder().getPosition();
        double error = position - currentPosition;

        if (Math.abs(error) < 0.1) { // if we are close enough to the target, stop moving
            adjustAngle(0);
            return;
        }
        adjustAngle(error);
    }

    public boolean shooterAtSpeed(double speed) { // checks if we are in a RANGE for our shooter, not just if its
                                                  // exactly equal
        double variance = 10; // 10% variance allowed
        double shooterSpeed = (ShooterMotor.getEncoder().getVelocity() / 5676); // get current shooter speed as
                                                                                // percentage of max RPM
        return (Math.abs(shooterSpeed - speed) <= (1 / variance));
    }
}
