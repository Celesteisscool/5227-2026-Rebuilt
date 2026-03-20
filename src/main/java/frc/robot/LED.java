package frc.robot;

import static edu.wpi.first.units.Units.InchesPerSecond;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.Percent;

import edu.wpi.first.units.measure.Distance;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.AddressableLEDBufferView;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.LEDPattern.GradientType;
import edu.wpi.first.wpilibj.util.Color;

public class LED {
    public AddressableLED leds;
    public AddressableLEDBuffer data = new AddressableLEDBuffer(60);

    private final Distance LED_SPACING = Meters.of(1.0 / 60);

    // SEGMENTS //
    public final AddressableLEDBufferView leftHopper  = data.createView(0, 29);
    public final AddressableLEDBufferView rightHopper = data.createView(30, 59).reversed();

    // PATTERNS //
    private final LEDPattern redPattern = LEDPattern.solid(new Color(255, 0, 0));
    private final LEDPattern yellowPattern = LEDPattern.solid(new Color(255, 223, 0));
    private final LEDPattern greenPattern = LEDPattern.solid(new Color(0, 255, 0));
    private final LEDPattern offPattern = LEDPattern.solid(new Color(0, 0, 0));


    private final LEDPattern purpleGoldPattern = LEDPattern.gradient(GradientType.kContinuous, new Color[] {
            new Color(186, 85, 211), // Purple
            new Color(255, 215, 0) }); // Gold

    private final LEDPattern purpleGoldScrollingPattern = purpleGoldPattern.scrollAtAbsoluteSpeed(InchesPerSecond.of(12),
            LED_SPACING);


    // FUNCTIONS //
    public LED() {
        leds = new AddressableLED(1);
        leds.setLength(data.getLength());
        leds.start();
    }

    
    public void setLEDDisable() {
        purpleGoldScrollingPattern.atBrightness(Percent.of(10)).applyTo(leftHopper);
        purpleGoldScrollingPattern.atBrightness(Percent.of(10)).applyTo(rightHopper);
    }

    public void setLEDRed() {
        redPattern.atBrightness(Percent.of(10)).applyTo(leftHopper);
        redPattern.atBrightness(Percent.of(10)).applyTo(rightHopper);
    }
    public void setLEDYellow() {
        yellowPattern.atBrightness(Percent.of(10)).applyTo(leftHopper);
        yellowPattern.atBrightness(Percent.of(10)).applyTo(rightHopper);
    }
    public void setLEDGreen() {
        greenPattern.atBrightness(Percent.of(10)).applyTo(leftHopper);
        greenPattern.atBrightness(Percent.of(10)).applyTo(rightHopper);
    }

    public void setLEDOff() {
        offPattern.atBrightness(Percent.of(10)).applyTo(leftHopper);
        offPattern.atBrightness(Percent.of(10)).applyTo(rightHopper);
    }
    public void updateLED() {
        leds.setData(data);
    }
}
