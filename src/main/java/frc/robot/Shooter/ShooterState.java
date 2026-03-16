package frc.robot.Shooter;

public class ShooterState {
  public final double distanceMeters;
  public final double hoodAngleDeg;
  public final double flywheelSpeed;

  public ShooterState(double distanceMeters,
                      double hoodAngleDeg,
                      double flywheelSpeed) {
    this.distanceMeters = distanceMeters;
    this.hoodAngleDeg = hoodAngleDeg;
    this.flywheelSpeed = flywheelSpeed;
  }
}