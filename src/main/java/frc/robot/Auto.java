package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Shooter.ShooterState;

public class Auto {
    public boolean runningAuto = false;

    public String[] autoList = { "Center", "Left Side", "Right Side", "Nothing" };

    String selectedAuto = "";

    public boolean forceShoot = false;
    public boolean autoRan = false;

    private Timer autoTimer = new Timer();

    /** Read the selected auto name from the SmartDashboard chooser */
    public void chooseAuto() {
        // Read the string put on the dashboard under "Auto choices" by the chooser
        selectedAuto = SmartDashboard.getString("Auto choices", autoList[0]);
        System.out.println(selectedAuto);
        Classes.Controls = Classes.AutoControls;
        autoTimer.reset();
        autoTimer.start();
        autoRan = true;

        if (selectedAuto == "Left Side") {
            Classes.mecanumClass.gyro.setYaw(90); // Might have to swap these :<
        }
        if (selectedAuto == "Right Side") {
            Classes.mecanumClass.gyro.setYaw(-90); // Might have to swap these :<
        }

    }

    public void runAuto() {
        if (selectedAuto == "Center") {
            centerAuto();
        } else if (selectedAuto == "Left Side" | selectedAuto == "Right Side") {
            sideAuto();
        } else if (selectedAuto == "Nothing") {
            // Do nothing :>
        }
        Classes.mecanumClass.driveFunction(); // Update these with our new values :>
        Classes.shooterClass.shooterLoopLogic();
    }

    private void centerAuto() {
        // Move back + move hood down
        autoState(0, -0.5, 0, 0, false, false, false, true);

        // Stop moving and coast
        autoState(0.5, 0, 0, 0, false, false, false, false);

        // Shoot our balls
        autoState(1, 0, 0, 0, false, true, false, false);

        // Stop shooting
        autoState(10, 0, 0, 0, false, false, false, false);
    }

    private void sideAuto() {
        // Move angle down
        autoState(0, 0, 0, 0, false, false, false, true);

        // Stop moving angle
        autoState(1, 0, 0, 0, false, false, false, false);

        // Shoot
        autoShooterState(1.1, Classes.shooterClass.getWantedShooterState(3.65));

        // Stop Shooting
        autoState(10, 0, 0, 0, false, false, false, false);
    }

    public double forwardInput = 0.0;
    public double sidewaysInput = 0.0;
    public double rotationInput = 0.0;
    public boolean breakingInput = false;
    public boolean shootInput = false;
    public boolean intakeInput = false;
    public boolean zeroAngleInput = false;

    private void autoState(
            double time,
            double forward,
            double sideways,
            double rotation,
            boolean breaking,
            boolean shoot,
            boolean intake,
            boolean zeroAngle) {
        if (autoTimer.get() >= time) {
            forwardInput = forward;
            sidewaysInput = sideways;
            rotationInput = rotation;
            breakingInput = breaking;
            shootInput = shoot;
            intakeInput = intake;
            zeroAngleInput = zeroAngle;
        }
        forceShoot = false;

    }

    private void autoShooterState(double time, ShooterState state) {
        if (autoTimer.get() >= time) {
            Classes.shooterClass.applyShooterState(state);
            forceShoot = true;
        }
    }
}
