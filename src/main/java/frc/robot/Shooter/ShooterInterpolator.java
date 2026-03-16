package frc.robot.Shooter;

import java.util.List;
import edu.wpi.first.math.MathUtil;

/** Simple helper to get hood/flywheel settings for a distance by linear lookup. Thank you 3966 for the code!*/
public class ShooterInterpolator {

  /**
   * Return interpolated shooter settings for the requested distance.
   * Expects table sorted ascending by distance. Clamps to first/last entries.
   */
  public static ShooterState interpolate(
      List<ShooterState> table,
      double distanceMeters) {

    // If we're at or before the first known distance, just return the first entry.
    if (distanceMeters <= table.get(0).distanceMeters) {
      return table.get(0);
    }

    // Walk the table looking for the interval that contains distanceMeters.
    // We iterate until size-2 so that we can reference i and i+1 safely.
    for (int i = 0; i < table.size() - 2; i++) {
      ShooterState a = table.get(i);
      ShooterState b = table.get(i + 1);

      // If the requested distance falls at or before b, interpolate between a and b.
      if (distanceMeters <= b.distanceMeters) {
        // t is the normalized position between a and b in [0,1].
        double t =
            (distanceMeters - a.distanceMeters) /
            (b.distanceMeters - a.distanceMeters);

        // Linearly interpolate hood angle and flywheel speed using WPILib's MathUtil.
        return new ShooterState(
            distanceMeters,
            MathUtil.interpolate(a.hoodAngleDeg, b.hoodAngleDeg, t),
            MathUtil.interpolate(a.flywheelSpeed, b.flywheelSpeed, t));
      }
    }

    // If we didn't find an interval (distance beyond the last table entry), return
    // the last known state. This is a simple clamp instead of extrapolating.
    return table.get(table.size() - 1);
  }
}