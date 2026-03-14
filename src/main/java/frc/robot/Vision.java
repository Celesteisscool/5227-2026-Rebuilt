package frc.robot;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonTrackedTarget;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
public class Vision {
    PhotonCamera LeftCam = new PhotonCamera("LeftCam");
    PhotonCamera RightCam = new PhotonCamera("RightCam");

    public Vision() {
        // Constructor code, if needed
    }


    // offset from camera to hub: -23.5 inches on the X axis (converted to meters)
    private final Transform3d offsetToHub = new Transform3d(
        new Translation3d(-23.5 * 0.0254, 0.0, 0.0), // * 0.0254 Convert inches to meters
        new Rotation3d() // We dont need to offset the rotation, just the translation
    );

    
    public PhotonTrackedTarget getHubTag(PhotonCamera camera) {
        var results = camera.getAllUnreadResults();
        if (!results.isEmpty()) {
            // Camera processed a new frame since last
            // Get the last one in the list.
            var result = results.get(results.size() - 1);
            if (result.hasTargets()) {
                // At least one AprilTag was seen by the camera
                for (var target : result.getTargets()) {
                    if ((target.getFiducialId() == 10) | (target.getFiducialId() == 26)) { // The two front center tags on each hub
                        return target;                        
                    }
                }
            }
        }
        return null;
    }

    public Transform3d cameraToHub() {
        return getHubTag(LeftCam).getBestCameraToTarget().plus(offsetToHub);
    }

}
