package frc.robot.Shooter;

import com.ctre.phoenix6.Orchestra;
import com.ctre.phoenix6.configs.AudioConfigs;

import frc.robot.Classes;

public class Music {
    Orchestra orchestra = new Orchestra();

    public Music() {
        orchestra.addInstrument(Classes.shooterClass.shooterMotor);
        orchestra.addInstrument(Classes.shooterClass.angleMotor);
        AudioConfigs config = new AudioConfigs();
        config.AllowMusicDurDisable = true;
        Classes.shooterClass.shooterMotor.getConfigurator().apply(config);
    }

    private void loadMusic(String name) {
        orchestra.loadMusic(name + ".chrp");
    }
    
    public void playStartup() {
        loadMusic("startup");
        orchestra.play();
    }
}
