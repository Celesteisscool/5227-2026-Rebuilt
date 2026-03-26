package frc.robot.Controls;

public interface ControlInterface {
    // Drive Controls
    double getDriveX(); 
    double getDriveY(); 
    double getDriveRot(); 
    boolean getSlowMode(); 
    boolean getBreakMode();
    
    boolean resetGyro(); // Should not be pressed in comps! only for testing, will remove.
    
    boolean autoAlignButton(); // for auto-aligning to the target using vision
    
    // Shooter Controls
    boolean getShootButton(); // Spin our shooter up to speed, then feed balls in
    boolean getIntakeButton();  // Intake into hopper
    
    boolean getReverseShootButton(); // Only spins the intake wheels in reverse.
    boolean getOuttakeButton(); // Spits out of hopper, for clearing jams and dumping balls

    double getAngleAdjust(); // for adjusting the angle of the shooter

    boolean debugButton(); // for auto-adjusting the angle of the shooter using vision
    boolean zeroAngleButton(); // move our shooter too zero

    void rumble(double strength, boolean leftRumble); // for providing haptic feedback
    boolean allControlersConnected(); // for checking if all controllers are connected

    boolean speedAdjustUp();
    boolean speedAdjustDown();

}