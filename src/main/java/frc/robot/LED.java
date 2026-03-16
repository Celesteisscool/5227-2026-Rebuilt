package frc.robot;

import static edu.wpi.first.units.Units.InchesPerSecond;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Percent;

import java.util.Optional;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AddressableLEDBufferView;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.LEDPattern.GradientType;
import edu.wpi.first.wpilibj.util.Color;

public class LED {
    public AddressableLED leds;
    public AddressableLEDBuffer data = new AddressableLEDBuffer(100);

    private final Distance LED_SPACING = Meters.of(1.0 / 60);

    // SEGMENTS //
    public final AddressableLEDBufferView underBumpers = data.createView(0, 59);
    public final AddressableLEDBufferView hopper = data.createView(60, data.getLength() - 1);


    // PATTERNS //
    private final LEDPattern rainbow = LEDPattern.rainbow(255, 128);
    private final LEDPattern scrollingRainbow = rainbow.scrollAtAbsoluteSpeed(InchesPerSecond.of(12), LED_SPACING);
    private final LEDPattern purpleGold = LEDPattern.gradient(GradientType.kContinuous, new Color[] {
            new Color(75, 0, 130), // Purple
            new Color(255, 223, 0) }); // Gold
    private final LEDPattern purpleGoldScrolling = purpleGold.scrollAtAbsoluteSpeed(InchesPerSecond.of(12),
            LED_SPACING);
    private final LEDPattern HVAColors = LEDPattern.gradient(GradientType.kContinuous, new Color[] {
            new Color(0, 0, 128), // Navy Blue
            new Color(185, 217, 235) }); // Columbia Blue
    private final LEDPattern yellow = LEDPattern.solid(new Color(255, 223, 0));
    private final LEDPattern red = LEDPattern.solid(new Color(100, 0, 0));
    private final LEDPattern green = LEDPattern.solid(new Color(0, 100, 0));
    private final LEDPattern blue = LEDPattern.solid(new Color(0, 0, 100));
    private final LEDPattern off = LEDPattern.kOff;
    // PATTERNS //

    public LED() {
        leds = new AddressableLED(0);
        leds.setLength(data.getLength());
        leds.start();
    }

    public void setUnderBumperLED() {
        Optional<Alliance> alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
            if (alliance.isPresent() && alliance.get() == Alliance.Red) {
                red.applyTo(underBumpers);
            } else if (alliance.isPresent() && alliance.get() == Alliance.Blue) {
                blue.applyTo(underBumpers);
            } else {
                purpleGold.applyTo(underBumpers);
            }
        }
    }

    public void setLEDOff(AddressableLEDBufferView segment) {
        off.applyTo(segment);
    }

    public void setLEDYELLOW(AddressableLEDBufferView segment) {
        yellow.atBrightness(Percent.of(25)).applyTo(segment);
    }

    public void setLEDRED(AddressableLEDBufferView segment) {
        red.applyTo(segment);
    }

    public void setLEDGREEN(AddressableLEDBufferView segment) {
        green.applyTo(segment);
    }

    public void setLEDBLUE(AddressableLEDBufferView segment) {
        blue.applyTo(segment);
    }

    public void setLEDRainbow(AddressableLEDBufferView segment) {
        scrollingRainbow.applyTo(segment);
    }

    public void setLEDPurpleGold(AddressableLEDBufferView segment) {
        purpleGoldScrolling.atBrightness(Percent.of(50)).applyTo(segment);
    }

    public void setLEDHVAColors(AddressableLEDBufferView segment) {
        HVAColors.atBrightness(Percent.of(50)).applyTo(segment);
    }

    public void updateLED() {
        leds.setData(data);
    }
}
