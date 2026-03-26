package frc.robot.Shooter;

public class ShooterStatus {
        public boolean shooting = false; // whether we are currently trying to shoot
        public boolean intaking = false; // whether we are currently trying to intake
        public boolean outtaking = false; // whether we are currently trying to outtake
        public boolean reverseShoot = false; // whether we are currently trying to reverse shoot
        public boolean atAngle = false;
        public boolean atSpeed = false; // whether we are at speed or not
        public double shooterSpeed = 0; // current shooter speed as percentage of max RPM
        public double desiredShooterSpeed = 0; // the speed we want to be at
        public double desiredShooterAngle = 0; // the angle we want to be at
        public double speedAdjust = 0.0; // start at +0 RPM
        public double globalSpindexSpeed = 0.2; // what speed to run our spindexer at
        public double distanceToHub = Double.NaN;
}
