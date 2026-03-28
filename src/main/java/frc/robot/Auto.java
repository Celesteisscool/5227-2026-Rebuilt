package frc.robot;

import edu.wpi.first.wpilibj.Timer;

public class Auto {
    public boolean runningAuto = false;

    public String[] autoList = {"Nothing", "Center", "Backup" };

    String selectedAuto = "";

    public boolean forceShoot = false;
    public boolean autoRan = false;

    private Timer autoTimer = new Timer();

    /** Read the selected auto name from the SmartDashboard chooser */
    public void chooseAuto() {
        // Read the string put on the dashboard under "Auto choices" by the chooser
        selectedAuto = DriverFeedback.getSelectedAuto();
        System.out.println(selectedAuto);
        Classes.Controls = Classes.AutoControls;
        autoTimer.reset();
        autoTimer.start();
        autoRan = true;
    }

    public void runAuto() {
        if (selectedAuto == "Center") {
            centerAutoRam();
        } else if (selectedAuto == "Backup") {
            backupAuto();
        } else if (selectedAuto == "Nothing") {
            // Do nothing :>
        }
        Classes.mecanumClass.driveFunction(); // Update these with our new values :>
        Classes.shooterClass.shooterLoopLogic();
    }


    private void centerAutoRam() {
        double visionRotate = Vision.getAutoAlignRotation(); // run this to update vision :<
        // Move back + move hood down
        autoState(0, -0.5, 0, 0, false, false, false, true);

        // Stop moving and coast
        autoState(1, 0, 0, 0, false, false, false, true);

        // Shoot our balls
        autoState(2, 0, 0, 0, false, true, false, false);

        // Stop shooting
        autoState(8, 0, 0, 0, false, false, false, false);
    }

    private void backupAuto() {
        // Move back + move hood down
        autoState(0, -0.5, 0, 0, false, false, false, true);

        // Stop moving and coast
        autoState(1, 0, 0, 0, true, false, false, true);

        // stop breaking
        autoState(2, 0, 0, 0, false, false, false, false);

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
            forwardInput = -forward; // inverting so that it makes WAY more sense.
            sidewaysInput = sideways;
            rotationInput = rotation;
            breakingInput = breaking;
            shootInput = shoot;
            intakeInput = intake;
            zeroAngleInput = zeroAngle;
        }
        forceShoot = false;

    }
}
