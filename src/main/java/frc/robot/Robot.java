// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Optional;

import choreo.Choreo;
import choreo.trajectory.SwerveSample;
import choreo.trajectory.Trajectory;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends TimedRobot {

  Teleop teleop = new Teleop();

  private final Timer timer = new Timer();
  private final Optional<Trajectory<SwerveSample>> trajectory = Choreo.loadTrajectory("testPath");
  private final Drivetrain driveSubsystem = Constants.drivetrainClass;

  @Override
  public void robotInit() {
  }

  @Override
  public void robotPeriodic() {
    Constants.drivetrainClass.updateDashboard();
  }

  @Override
  public void autonomousInit() {
    if (trajectory.isPresent()) {
      // Get the initial pose of the trajectory
      Optional<Pose2d> initialPose = trajectory.get().getInitialPose(isRedAlliance());

      if (initialPose.isPresent()) {
        // Reset odometry to the start of the trajectory
        driveSubsystem.resetOdometry(initialPose.get());
      }
    }

    // Reset and start the timer when the autonomous period begins
    timer.restart();
  }

  @Override
  public void autonomousPeriodic() {
    if (trajectory.isPresent()) {
      // Sample the trajectory at the current time into the autonomous period
      Optional<SwerveSample> sample = trajectory.get().sampleAt(timer.get(), isRedAlliance());

      if (sample.isPresent()) {
        sample.ifPresent(driveSubsystem::followTrajectory);
      }
    }
  }

  private boolean isRedAlliance() {
    return DriverStation.getAlliance().orElse(Alliance.Blue).equals(Alliance.Red);
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
