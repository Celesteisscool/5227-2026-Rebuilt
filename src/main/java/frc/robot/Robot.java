// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  @Override
  public void robotInit() {
    Constants.ledClass.setLEDHVAColors();
    Dashboard.addEntry("angle", 0.0);
    DriverFeedback.setupFeedback();
  }

  @Override
  public void robotPeriodic() {
    Constants.ledClass.updateLED();
    DriverFeedback.updateFeedback();
  }

  @Override
  public void testInit() {
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {

  }

  @Override
  public void teleopPeriodic() {
    Constants.mecanumClass.driveFunction();
    Constants.shooterClass.shooterLoopLogic();
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Constants.ledClass.setLEDPurpleGold();
  }
}
