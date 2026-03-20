package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auto {
    public boolean runningAuto = false;

    public String[] autoList = { "Center", "Left Side", "Nothing" };

    String selectedAuto = "";

    private Timer autoTimer = new Timer();

    /** Read the selected auto name from the SmartDashboard chooser */
    public void chooseAuto() {
        // Read the string put on the dashboard under "Auto choices" by the chooser
        selectedAuto = SmartDashboard.getString("Auto choices", autoList[0]);
        System.out.println(selectedAuto);
        Classes.Controls = Classes.AutoControls;
        autoTimer.reset();
        autoTimer.start();
    }

    public double forwardInput = 0.0;
    public double sidewaysInput = 0.0;
    public double rotationInput = 0.0;
    public boolean breakingInput = false;
    public boolean shootInput = false;
    public boolean intakeInput = false;
    public boolean zeroAngleInput = false;

    public void autoState(
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

    }

    public void runAuto() {
        if (selectedAuto == "Center") {
            centerAuto();
        } else if (selectedAuto == "Left Side") {
            leftSideAuto();
        } else if (selectedAuto == "Nothing") {
            // Do nothing :>
        }
    }

    private void centerAuto() {
        autoState(0, -0.5, 0, 0, false, false, false, true);
        autoState(0.5, 0, 0, 0, false, false, false, false);
        autoState(1, 0, 0, 0, false, true, false, false);
        autoState(5, 0, 0, 0, false, false, false, false);
    }

    private void leftSideAuto() {
        Classes.mecanumClass.gyro.setYaw(90);

        autoState(0.1,0,0,0,false,false,false,true);
        autoState(1,0,0,0,false,true,false,true);
        autoState(10, 0, 0, 0, false, false, false, false);
    }
}
