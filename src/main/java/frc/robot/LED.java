package frc.robot;

import static edu.wpi.first.units.Units.InchesPerSecond;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Percent;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.LEDPattern.GradientType;
import edu.wpi.first.wpilibj.util.Color;

public class LED {
    public AddressableLED leds;
    public AddressableLEDBuffer data = new AddressableLEDBuffer(24 * 2);

    private final Distance LED_SPACING = Meters.of(1.0 / 60);

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

    private final LEDPattern patternYellow = LEDPattern.solid(new Color(255, 223, 0) );

    private final LEDPattern patternRed = LEDPattern.solid(new Color(100, 0, 0));
    private final LEDPattern patternGreen = LEDPattern.solid(new Color(0, 100, 0));
    private final LEDPattern patternBlue = LEDPattern.solid(new Color(0, 0, 100));
    private final LEDPattern patternOff = LEDPattern.kOff;

    public double AlignSide = 0; // true is left false is right
    public double leftAlign = -2.25;

    public LED() {
        leds = new AddressableLED(0);
        leds.setLength(data.getLength());
        leds.start();
    }

    public void setLEDOff() {
        patternOff.applyTo(data);
    }


    public void setLEDYELLOW() {
        patternYellow.atBrightness(Percent.of(25)).applyTo(data);
    }

    public void setLEDRED() {
        patternRed.applyTo(data);
    }

    public void setLEDGREEN() {
        patternGreen.applyTo(data);
    }

    public void setLEDBLUE() {
        patternBlue.applyTo(data);
    }

    public void setLEDRainbow() {
        scrollingRainbow.applyTo(data);
    }

    public void setLEDPurpleGold() {
        purpleGoldScrolling.atBrightness(Percent.of(25)).applyTo(data);
    }

    public void setLEDHVAColors() {
        HVAColors.atBrightness(Percent.of(25)).applyTo(data);
    }

    public void updateLED() {
        leds.setData(data);
    }
}
