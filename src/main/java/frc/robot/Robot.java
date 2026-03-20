// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  @Override
  public void robotInit() {
    // Constants.ledClass.setLEDHVAColors(Constants.ledClass.hopper);
    DriverFeedback.setupDashboard();
  }

  @Override
  public void robotPeriodic() {
    DriverFeedback.updateFeedback();
    // Classes.ledClass.updateLED();
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
    Classes.mecanumClass.driveFunction();
    Classes.shooterClass.shooterLoopLogic();
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    // Classes.ledClass.setLEDDisable();  
  }
}
