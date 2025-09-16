package frc.robot;

public class Teleop {
	static boolean fieldRelative = true;

	public static double deadzones(double input) {
		if (Math.abs(input) < 0.05) {
			return 0;
		} else {
			return input;
		}
	}

	public static void driveFunction() {
		Drivetrain Drivetrain = Constants.drivetrainClass;
		double translateSpeedLimiter = 0.95;
		double rotateSpeedLimiter = 0.95;

		if (Constants.Controls.getSlowMode()) {
			translateSpeedLimiter *= 0.25;
			rotateSpeedLimiter *= 0.1;
		}

		double xSpeed = (deadzones(Constants.Controls.getDriveY()) * Constants.maxSwerveSpeed * translateSpeedLimiter);
		double ySpeed = (deadzones(Constants.Controls.getDriveX()) * Constants.maxSwerveSpeed * translateSpeedLimiter);
		double rotSpeed = (deadzones(Constants.Controls.getDriveRot()) * Constants.maxSwerveAngularSpeed
				* rotateSpeedLimiter);
		double RobotRelativeAngle = Constants.Controls.getRobotRelativeDegrees();

		if (RobotRelativeAngle != -1) { // Drives with the DPad instead of the joystick for perfect 45° angles
			var POVRadians = Math.toRadians(RobotRelativeAngle);
			xSpeed = Math.cos(POVRadians) * -0.25 * translateSpeedLimiter;
			ySpeed = Math.sin(POVRadians) * 0.25 * translateSpeedLimiter;
			fieldRelative = false; // Forces it to be robot relative
		} else {
			if ((xSpeed > 0) || (ySpeed > 0) || (rotSpeed > 0)) {
				fieldRelative = true;
			}
		}

		double xOutput = Constants.slewRateLimiterX.calculate(xSpeed);
		double yOutput = Constants.slewRateLimiterY.calculate(ySpeed);
		double rotOutput = Constants.slewRateLimiterRot.calculate(rotSpeed);

		Drivetrain.drive(
				xOutput,
				yOutput,
				rotOutput,
				fieldRelative);
	};

	public void teleopPeriodic() {
		driveFunction();
		// Constants.shooterClass.shooterWithHoodServo();
		Constants.ledClass.updateLED();
	}
}
