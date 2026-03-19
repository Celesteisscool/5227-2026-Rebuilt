package frc.robot;

import org.photonvision.PhotonCamera;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;

public class Vision {
    static PhotonCamera LeftCam = new PhotonCamera("CamLeft");
    static PhotonCamera RightCam = new PhotonCamera("CamRight");

    // offset from camera to hub: -23.5 inches on the X axis (converted to meters)
    private final static Transform3d offsetToHub = new Transform3d(
            new Translation3d(-23.5 * 0.0254, 0.0, 0.0), // * 0.0254 Convert inches to meters
            new Rotation3d() // We dont need to offset the rotation, just the translation
    );

    public static double outputFromCode = 0.0;

    // Check a single camera's unread results for a hub tag and return it (or null)
    static boolean targetVisible = false;
    public static double angleToHub = 0.0;
    // Remember the last time we saw a valid target and its angle. This avoids
    // immediately zeroing motors when an intermediate poll finds no new unread
    // results (camera can be polled faster than frames arrive).
    private static long lastSeenMillis = 0;
    private static double lastAngleToHub = 0.0;
    private static double lastDistanceToHub = Double.NaN;
    // Grace period (ms) to continue using the last valid reading when no new
    // target is visible. Adjust if needed; 200ms is a small, safe value.
    private static final long LAST_SEEN_GRACE_MS = 200;
    // Small exponential smoothing on the rotation output to reduce jitter.
    // alpha in (0,1] where larger alpha follows new values more closely.
    // Lower alpha -> smoother / slower-to-follow output. Tunable.
    private static double smoothedRotation = 0.0;
    private static final double rotationSmoothing = 0.75;

    private static final double distanceSmoothing = 0.1;

    private static void findHubTagInCamera(PhotonCamera camera) {
        var results = camera.getAllUnreadResults();
        if (!results.isEmpty()) {
            // Camera processed a new frame since last
            // Get the last one in the list.
            var result = results.get(results.size() - 1);
            if (result.hasTargets()) {
                // At least one AprilTag was seen by the camera
                for (var target : result.getTargets()) {
                    int id = target.getFiducialId();
                    if (id == 10 || id == 26) {
                        // Found Tag 7, record its information
                        // Photon target yaw is in degrees relative to the camera's forward
                        var t = target.bestCameraToTarget.plus(offsetToHub);
                        double dx = t.getTranslation().getX(); // forward
                        double dy = t.getTranslation().getY(); // left/right
                        double angleRad = Math.atan2(dy, dx);
                        double angleDeg = Math.toDegrees(angleRad);
                        angleToHub = -angleDeg;

                        // Compute planar distance (meters) from the camera to the hub
                        double distance = Math.hypot(dx, dy);

                        // Record last valid sighting and apply exponential smoothing
                        lastAngleToHub = angleToHub;

                        if (Double.isNaN(lastDistanceToHub)) {
                            lastDistanceToHub = distance;
                        } else {
                            lastDistanceToHub = distanceSmoothing * distance
                                    + (1.0 - distanceSmoothing) * lastDistanceToHub;
                        }

                        lastSeenMillis = System.currentTimeMillis();
                        targetVisible = true;
                    }
                }
            }
        }
    }

    public static double getAutoAlignRotation() {
        // Clear visibility each call and check both cameras for a target
        targetVisible = false;
        findHubTagInCamera(LeftCam);
        findHubTagInCamera(RightCam);

        // If no target visible check whether we have a recent valid sighting.
        // If a sighting was seen within LAST_SEEN_GRACE_MS, use the last angle
        // rather than immediately returning 0. This prevents small gaps
        // between camera frames from zeroing outputs.
        if (!targetVisible) {
            long age = System.currentTimeMillis() - lastSeenMillis;
            if (age <= LAST_SEEN_GRACE_MS) {
                // reuse last known angle
                angleToHub = lastAngleToHub;
            } else {
                return 0.0;
            }
        }

        // Read current robot heading from the pigeon (use same sign convention as
        // Mecanum.getGyro)
        var gyroRot = Classes.mecanumClass.gyro.getRotation2d().unaryMinus();
        double currentHeadingDeg = Math.toDegrees(gyroRot.getRadians());

        // Desired heading is current heading plus the yaw to the target (angleToHub
        // comes from camera)
        double desiredHeadingDeg = currentHeadingDeg + angleToHub;

        // Compute smallest-angle error (normalize to [-180,180])
        double error = desiredHeadingDeg - currentHeadingDeg;
        // Normalize
        while (error > 180.0) {
            error -= 360.0;
        }
        while (error <= -180.0) {
            error += 360.0;
        }

        // Simple proportional controller to convert degrees error to rotation command
        final double kP = 0.01; // Tunable: gain from degrees -> rotation speed
        double output = kP * error;

        // Clamp to [-1, 1]
        if (output > 1.0)
            output = 1.0;
        if (output < -1.0)
            output = -1.0;

        outputFromCode = output;

        // Deadband: small outputs are considered zero
        if (Math.abs(output) < 0.1) {
            output = 0.0;
        }

        // Exponential smoothing to reduce jitter on the returned rotation
        smoothedRotation = rotationSmoothing * output + (1.0 - rotationSmoothing) * smoothedRotation;

        return smoothedRotation;
    }

    public static double getDistanceToHub() {
        // If we have a recent sighting, return that distance. Use the same
        // grace period as rotation so short camera gaps still provide a value.
        long age = System.currentTimeMillis() - lastSeenMillis;
        if (age <= LAST_SEEN_GRACE_MS) {
            // lastDistanceToHub is updated with exponential smoothing in the camera
            // callback; report that value directly.
            Dashboard.updateEntry("Debug Distance", lastDistanceToHub);
            return lastDistanceToHub;
        }

        return Double.NaN;
    }

    /**
     * Returns whether a hub tag should be considered visible. This uses the same
     * grace period (LAST_SEEN_GRACE_MS) as the rotation/distance logic so brief
     * gaps between camera frames don't cause flicker.
     */
    public static boolean isTargetVisible() {
        if (targetVisible) {
            return true;
        }
        long age = System.currentTimeMillis() - lastSeenMillis;
        return age <= LAST_SEEN_GRACE_MS;
    }
}
