package frc.robot.Controls;

public interface ControlInterface {
    // Drive Controls
    double getDriveX(); 
    double getDriveY(); 
    double getDriveRot(); 
    boolean getSlowMode(); 

    boolean resetGyro(); // Should not be pressed in comps! only for testing, will remove.

    // Shooter Controls
    boolean getShootButton(); // Spin our shooter up to speed, then feed balls in
    boolean getReverseShootButton(); // Only spins the intake wheels in reverse.

    boolean getIntakeButton();  // Intake into hopper
    boolean getOuttakeButton(); // Spits out of hopper, for clearing jams and dumping balls

    double getAngleAdjust(); // for adjusting the angle of the shooter

    boolean autoAlignButton(); // for auto-aligning to the target using vision
    boolean autoAngleButton(); // for auto-adjusting the angle of the shooter using vision
    boolean zeroAngleButton(); // move our shooter too zero

    void rumble(double strength, boolean leftRumble); // for providing haptic feedback
    boolean allControlersConnected(); // for checking if all controllers are connected
}