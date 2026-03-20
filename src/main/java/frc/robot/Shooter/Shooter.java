package frc.robot.Shooter;

import java.util.List;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.Classes;
import frc.robot.Dashboard;
import frc.robot.Vision;

public class Shooter {
    SparkMax intakeMotor = new SparkMax(12, MotorType.kBrushless);
    SparkMax kickerMotor = new SparkMax(11, MotorType.kBrushless);

    SparkMax angleMotor = new SparkMax(14, MotorType.kBrushless);
    SparkMax shooterMotor = new SparkMax(13, MotorType.kBrushless);

    public double getShooterAngle() {
        return angleMotor.getEncoder().getPosition();
    };

    public boolean shooting = false; // whether we are currently trying to shoot, used for driver feedback and other
                                     // things
    public boolean intaking = false; // whether we are currently trying to intake, used for driver feedback and other
                                     // things
    public boolean outtaking = false; // whether we are currently trying to outtake, used for driver feedback and
                                      // other things
    public boolean reverseShoot = false; // whether we are currently trying to reverse shoot, used for driver feedback
                                         // and other things
    public boolean atAngle = false;

    public boolean atSpeed = false; // whether we are at speed or not, used for driver feedback and other things
    public double shooterSpeed = 0; // current shooter speed as percentage of max RPM, used for driver feedback and
                                    // other things

    public double desiredShooterSpeed = 0; // the speed we want to be at, used for driver feedback and other things
    public double desiredShooterAngle = 0; // the angle we want to be at, used for driver feedback and other things

    DigitalInput angleSwitch = new DigitalInput(0);

    private ShooterState getWantedShooterState(double distance) { // Returns the interpolated value
        List<ShooterState> ShooterValues = List.of(
                new ShooterState(2, -0.5, 0.4),
                new ShooterState(3, -1.75, 0.425),
                new ShooterState(3.5, -1.8, 0.43),
                new ShooterState(4, -2.3,0.45),
                new ShooterState(5, -3.25, 0.52),
                new ShooterState(6, -3.25, 0.6)
                );
        if (distance == Double.NaN) {
            return new ShooterState(0, 0, 0); // default value if we dont see the hub, change this if you want
        }
        return ShooterInterpolator.interpolate(ShooterValues, distance);
    }

    private void applyShooterState(ShooterState state) {
        setAngle(state.hoodAngleDeg);
        if (atAngle) {
            runShooter(state.flywheelSpeed);
        } else {
            intakeMotor.set(0);
            kickerMotor.set(0);
            shooterMotor.set(0);
        }
    }

    public void shooterLoopLogic() {

        Vision.getDistanceToHub();
        shooting = false;
        intaking = false;
        outtaking = false;
        reverseShoot = false;

        desiredShooterSpeed = 0.0;
        desiredShooterAngle = 0.0;
        // INTAKE
        if (Classes.Controls.getIntakeButton()) {
            Double speed = 0.6;
            Double time = (System.currentTimeMillis() / 100.0);
            Double smooth = Math.round((Math.sin(time)+1)/2) * -speed;
            speed = 0.6 + smooth;

            intaking = true;
            intakeMotor.set(speed);
            kickerMotor.set(speed);
        }
        // OUTTAKE
        else if (Classes.Controls.getOuttakeButton()) {
            Double speed = -0.5;
            outtaking = true;
            intakeMotor.set(speed);
            kickerMotor.set(speed);
        }
        // SHOOT
        else if (Classes.Controls.getShootButton()) {
            applyShooterState(getWantedShooterState(Vision.getDistanceToHub()));
        }
        // REVERSE SHOOTER
        else if (Classes.Controls.getReverseShootButton()) {
            reverseShoot = true;
            Double speed = -0.75;
            intakeMotor.set(speed);
            kickerMotor.set(-speed);
        }
        // AUTO ANGLE AND SHOOT
        else if (Classes.Controls.autoAngleButton()) {
            double speed = (double) Dashboard.getEntry("Debug Speed");
            double angle = (double) Dashboard.getEntry("Debug Angle");
            
            applyShooterState(new ShooterState(0, angle, speed));
        } else if (Classes.Controls.zeroAngleButton()) {
            setAngle(10); // Jams us WAY down.
        } else if (Math.abs(Classes.Controls.getAngleAdjust()) > 0.1) {
            adjustAngle(Classes.Controls.getAngleAdjust());
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

        double maxSpeed = 0.03;
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

    public boolean shooterAtSpeed(double speed) { // checks if we are in a RANGE for our shooter, not just if its
                                                  // exactly equal
        double variance = 10.0; // 10% variance allowed
        shooterSpeed = (shooterMotor.getEncoder().getVelocity() / 5676.0); // get current shooter speed as
                                                                         // percentage of max RPM
        return (Math.abs(shooterSpeed - speed) <= (1.0 / variance));
    }
}
