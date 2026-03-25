package frc.robot.Shooter;

public class ShooterState {
  // Again, thanks 3966 for the code!
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