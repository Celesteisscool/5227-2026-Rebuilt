package frc.robot.Shooter;

import java.util.List;
import edu.wpi.first.math.MathUtil;

public class ShooterInterpolator {

  public static ShooterState interpolate(
      List<ShooterState> table,
      double distanceMeters) {

    if (distanceMeters <= table.get(0).distanceMeters) {
      return table.get(0);
    }

    for (int i = 0; i < table.size() - 2; i++) {
      ShooterState a = table.get(i);
      ShooterState b = table.get(i + 1);

      if (distanceMeters <= b.distanceMeters) {
        double t =
            (distanceMeters - a.distanceMeters) /
            (b.distanceMeters - a.distanceMeters);

        return new ShooterState(
            distanceMeters,
            MathUtil.interpolate(a.hoodAngleDeg, b.hoodAngleDeg, t),
            MathUtil.interpolate(a.flywheelSpeed, b.flywheelSpeed, t));
      }
    }

    return table.get(table.size() - 1);
  }
}