// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {  
  @Override
  public void robotInit() {
    Constants.ledClass.setLEDHVAColors();
    Constants.ledClass.updateLED();
  }
  
  @Override
  public void robotPeriodic() {
  }
  
  @Override
  public void testInit() {
    Dashboard.addEntry("yay", false);
    Dashboard.addEntry("xAxis", 0.0);
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
		Constants.ledClass.updateLED();
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
