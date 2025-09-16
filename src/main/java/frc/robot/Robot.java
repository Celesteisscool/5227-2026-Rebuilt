// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {

  Teleop teleop = new Teleop();
  
  @Override
  public void robotInit() {
  }

  @Override
  public void robotPeriodic() {
    Constants.drivetrainClass.updateDashboard();
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
    teleop.teleopPeriodic();
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    Constants.ledClass.setLEDYELLOW();
    Constants.ledClass.updateLED();
  }
}
