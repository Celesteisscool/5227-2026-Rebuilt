// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  @Override
  public void robotInit() {
    DriverFeedback.setupDashboard();
  }

  @Override
  public void robotPeriodic() {
    DriverFeedback.updateFeedback();
    Classes.ledClass.updateLED();
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
    Classes.shooterClass.adjustAngle(0); // brake mode maybe???
  }

  @Override
  public void autonomousInit() {
    Classes.autoClass.chooseAuto(); // Use auto controls + setup selected auto
  }

  @Override
  public void autonomousPeriodic() {
    Classes.autoClass.runAuto();
  }

  @Override
  public void teleopInit() {
    Classes.Controls = Classes.NormalControls; // Use controllers :>
  }

  @Override
  public void teleopPeriodic() {
    Classes.mecanumClass.driveFunction();
    Classes.shooterClass.shooterLoopLogic();
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }
}
