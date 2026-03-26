package frc.robot.Shooter;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.AudioConfigs;
import frc.robot.Classes;
public class ShooterMusic {
    Orchestra orchestra = new Orchestra();

    public ShooterMusic() {
        orchestra.addInstrument(Classes.shooterClass.flywheel.shooterMotor);
        // orchestra.addInstrument(Classes.shooterClass.angleMotor);
        
        AudioConfigs config = new AudioConfigs();
        config.AllowMusicDurDisable = true;
        Classes.shooterClass.flywheel.shooterMotor.getConfigurator().apply(config);
        // Classes.shooterClass.angleMotor.getConfigurator().apply(config);
    }
    private void loadMusic(String name) {
        orchestra.loadMusic(name + ".chrp");
    }

    /** Music list:
     * badApple
     * megalovania
     */
    public void playMusic(String name) {
        loadMusic(name);
        orchestra.play();
    }
}