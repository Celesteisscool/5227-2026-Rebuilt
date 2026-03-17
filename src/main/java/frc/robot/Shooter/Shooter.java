package frc.robot.Shooter;

import java.util.List;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Constants;
import frc.robot.Vision;

public class Shooter {
    // Shooter code goes here i dont want to break the robot

    SparkMax intakeMotor = new SparkMax(12, MotorType.kBrushless);
    SparkMax kickerMotor = new SparkMax(11, MotorType.kBrushless);

    SparkMax angleMotor = new SparkMax(14, MotorType.kBrushless);
    SparkMax shooterMotor = new SparkMax(13, MotorType.kBrushless);

    public double getShooterAngle() { return angleMotor.getEncoder().getPosition(); };


    public boolean shooting = false; // whether we are currently trying to shoot, used for driver feedback and other things
    public boolean intaking = false; // whether we are currently trying to intake, used for driver feedback and other things
    public boolean outtaking = false; // whether we are currently trying to outtake, used for driver feedback and other things
    public boolean reverseShoot = false; // whether we are currently trying to reverse shoot, used for driver feedback and other things

    public boolean atSpeed = false; // whether we are at speed or not, used for driver feedback and other things
    public double shooterSpeed = 0; // current shooter speed as percentage of max RPM, used for driver feedback and other things

    public double desiredShooterSpeed = 0; // the speed we want to be at, used for driver feedback and other things
    public double desiredShooterAngle = 0; // the angle we want to be at, used for driver feedback and other things

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

    private void applyShooterState(ShooterState state) {
        setAngle(state.hoodAngleDeg);
        runShooter(state.flywheelSpeed);
    }

    public void shooterLoopLogic() {

        shooting = false;
        intaking = false;
        outtaking = false;
        reverseShoot = false;

        desiredShooterSpeed = 0.0;
        desiredShooterAngle = 0.0;
        // INTAKE
        if (Constants.Controls.getIntakeButton()) {
            Double speed = 0.5;
            intaking = true;
            intakeMotor.set(speed);
            kickerMotor.set(speed);
        }
        // OUTTAKE
        else if (Constants.Controls.getOuttakeButton()) {
            Double speed = -0.5;
            outtaking = true;
            intakeMotor.set(speed);
            kickerMotor.set(speed);
        }
        // SHOOT
        else if (Constants.Controls.getShootButton()) {
            runShooter(0.4);
        }
        // REVERSE SHOOTER
        else if (Constants.Controls.getReverseShootButton()) {
            reverseShoot = true;
            Double speed = -0.75;
            intakeMotor.set(speed);
            kickerMotor.set(-speed);
        }
        // AUTO ANGLE AND SHOOT
        else if (Constants.Controls.autoAngleButton()) {
            setAngle(-2); // JUST TEST CODE RN
        }
        // DEFAULT TO NOT MOVING
        else {
            intakeMotor.set(0);
            kickerMotor.set(0);
            shooterMotor.set(0);
            angleMotor.set(0);
        }

    }

    public void runShooter(double speed) {
        
        // clamp input to safe range [-1, 1]
        speed = Math.max(-1.0, Math.min(1.0, speed));
        desiredShooterSpeed = speed;
        speed = speed * -1;
        
        
        // set shooter motor to requested percent output
        shooterMotor.set(speed);

        if (shooterAtSpeed(speed)) {
            shooting = true;
            // simple roller/kicker behavior: run them when shooting is requested
            double rollerSpeed = 0.75;
            intakeMotor.set(rollerSpeed);
            kickerMotor.set(-rollerSpeed);
        } else { // Turn off roller/kicker if we're not at speed, prevents jamming and shooting
                 // too early
            shooting = false;
            intakeMotor.set(0);
            kickerMotor.set(0);
        }
    }

    public void adjustAngle(double speed) { // used for limit switches.
        speed = Math.max(-1.0, Math.min(1.0, speed)); // clamp input to safe range [-1, 1]

        double maxSpeed = 0.02;
        speed = speed * maxSpeed; // Cap our speed at 0.02, done this way to give more control

        if (!angleSwitch.get() && (speed > 0)) { // if we are trying to move down and the switch is pressed, dont move
            angleMotor.set(0);
            angleMotor.getEncoder().setPosition(0);
        } else if (getShooterAngle() < -5.2 && (speed < 0)) {
            angleMotor.set(0);
        } else {
            angleMotor.set(speed);
        }
    }

    public void setAngle(double position) {
        desiredShooterAngle = position;
        double error = position - getShooterAngle();

        if (Math.abs(error) < 0.1) { // if we are close enough to the target, stop moving
            adjustAngle(0);
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

    public boolean shooterAtSpeed(double speed) { // checks if we are in a RANGE for our shooter, not just if its
                                                  // exactly equal
        double variance = 10; // 10% variance allowed
        shooterSpeed = (shooterMotor.getEncoder().getVelocity() / 5676); // get current shooter speed as
                                                                                // percentage of max RPM
        return (Math.abs(shooterSpeed - speed) <= (1 / variance));
    }
}
