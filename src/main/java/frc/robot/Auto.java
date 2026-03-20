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

    public void runAuto() {
        if (selectedAuto == "Center") {
            centerAuto();
        } else if (selectedAuto == "Left Side") {
            leftSideAuto();
        } else if (selectedAuto == "Right Side") {
            rightSideAuto();
        } else if (selectedAuto == "Nothing") {
            // Do nothing :>
        }
        Classes.mecanumClass.driveFunction(); // Update these with our new values :>
        Classes.shooterClass.shooterLoopLogic();
    }

    private void centerAuto() {
        autoState(0, -0.5, 0, 0, false, false, false, true);
        autoState(0.5, 0, 0, 0, false, false, false, false);
        autoState(1, 0, 0, 0, false, true, false, false);
        autoState(5, 0, 0, 0, false, false, false, false);
    }

    private void sideAuto(double gyroDegrees) {
        Classes.mecanumClass.gyro.setYaw(90);

        autoState(0.1, 0, 0, 0, false, false, false, true);

        autoState(1, 0, 0, 0, false, false, false, false);
        autoShooterState(1, Classes.shooterClass.getWantedShooterState(3.65));

        autoState(10, 0, 0, 0, false, false, false, false);
    }

    private void leftSideAuto() {
        sideAuto(90);
    }

    private void rightSideAuto() {
        sideAuto(-90);
    }
}
