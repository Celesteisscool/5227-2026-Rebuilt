package frc.robot;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;

public class Vision {
    static PhotonCamera LeftCam = new PhotonCamera("LeftCam");
    static PhotonCamera RightCam = new PhotonCamera("RightCam");

    // The last-seen AprilTag on the hub (cached)
    private static PhotonTrackedTarget cachedHubTag;

    // Whether the most recent call to update() found a hub tag we can use. If false,
    // other getters should treat the tag as unavailable.
    public static boolean hubVisible = false;

    // offset from camera to hub: -23.5 inches on the X axis (converted to meters)
    private final static Transform3d offsetToHub = new Transform3d(
            new Translation3d(-23.5 * 0.0254, 0.0, 0.0), // * 0.0254 Convert inches to meters
            new Rotation3d() // We dont need to offset the rotation, just the translation
    );

    // Check a single camera's unread results for a hub tag and return it (or null)
    private static PhotonTrackedTarget findHubTagInCamera(PhotonCamera camera) {
        var results = camera.getAllUnreadResults();
        if (!results.isEmpty()) {
            // Camera processed a new frame since last - use the most recent
            var result = results.get(results.size() - 1);
            if (result.hasTargets()) {
                for (var target : result.getTargets()) {
                    int id = target.getFiducialId();
                    if (id == 10 || id == 26) {
                        return target;
                    }
                }
            }
        }
        return null;
    }

    public static void update() {
        // Only check the left camera per request.
        PhotonTrackedTarget tag = findHubTagInCamera(LeftCam);

        if (tag != null) {
            cachedHubTag = tag;
            hubVisible = true;
        } else {
            // We did not see a hub tag in this update cycle. Mark as not visible so callers
            // won't use stale data. We keep cachedHubTag around for debugging, but it should
            // not be used while hubVisible is false.
            hubVisible = false;
        }
    }

    /** Return the cached hub tag (may be non-null even when {@link #isHubVisible()} is false). */

    public static Transform3d cameraToHub() {
        // Only return a transform when the most recent update() found a hub tag.
        if (!hubVisible) {
            return null;
        }
        PhotonTrackedTarget tag = cachedHubTag;
        if (tag == null) {
            return null;
        }
        return tag.getBestCameraToTarget().plus(offsetToHub);
    }

    public static double getAutoAlignRotation() {
        if (!hubVisible) {
            return Double.NaN; // No valid data to feed callers
        }
        Transform3d camToHub = cameraToHub();
        if (camToHub == null) {
            return Double.NaN;
        }
        double angleToHub = camToHub.getRotation().getZ(); // Get the yaw angle to the hub
        if (Math.abs(angleToHub) < ((Math.PI / 180) * 10)) { // If we're within 10 degrees of the target, we consider it
            angleToHub = 0;
        }
        return angleToHub / 10; // Normalize to [-1, 1] for controller input
    }

    public static double getDistanceToHub() {
        if (!hubVisible) {
            return Double.NaN;
        }
        Transform3d camToHub = cameraToHub();
        if (camToHub == null) {
            return Double.NaN;
        }
        return camToHub.getTranslation().getX(); // Get the forward distance to the hub
    }
}
