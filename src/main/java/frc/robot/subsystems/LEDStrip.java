// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDStrip extends SubsystemBase {
  private AddressableLED m_led;

  private AddressableLEDBuffer offBuffer, redBuffer, orangeBuffer, yellowBuffer, greenBuffer, blueBuffer, violetBuffer, whiteBuffer;

  private final int LED_COUNT = 33; 
  private LEDColor lastColor;

  public enum LEDColor {
    OFF, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET, WHITE;
  }

  /** Creates a new LEDS. */
  public LEDStrip() {
    initializeBuffers();

    m_led = new AddressableLED(0); // LEDs

    m_led.setLength(LED_COUNT);
    setColor(LEDColor.OFF);
    m_led.start();
  }

  private AddressableLEDBuffer getBufferByColorEnum(LEDColor color) {
    switch (color) {
      case OFF: return offBuffer;
      case RED: return redBuffer;
      case ORANGE: return orangeBuffer;
      case YELLOW: return yellowBuffer;
      case GREEN: return greenBuffer;
      case BLUE: return blueBuffer;
      case VIOLET: return violetBuffer;
      case WHITE: return whiteBuffer;
      default: return offBuffer;
    }
  }

  private AddressableLEDBuffer makeBuffer(int count, int r, int g, int b) {
    AddressableLEDBuffer buffer = new AddressableLEDBuffer(count);
    for (int i = 0; i < buffer.getLength(); ++i) {
      buffer.setRGB(i, r, g, b);
    }
    return buffer;
  }

  private void initializeBuffers() {
    offBuffer = makeBuffer(LED_COUNT, 0, 0, 0);
    redBuffer = makeBuffer(LED_COUNT, 250, 0, 0);
    orangeBuffer = makeBuffer(LED_COUNT, 255, 43, 0);
    yellowBuffer = makeBuffer(LED_COUNT, 230, 223, 0);
    greenBuffer = makeBuffer(LED_COUNT, 0, 250, 0);
    blueBuffer = makeBuffer(LED_COUNT, 0, 59, 174);
    violetBuffer = makeBuffer(LED_COUNT, 255, 0, 255);
    whiteBuffer = makeBuffer(LED_COUNT, 221, 222, 223);
  }

  public void setBlinkColor(LEDColor color) {
    final long interval = 250;
    long millis = System.currentTimeMillis() % 1000;

    // If we're between [250,500) ms, or [750,1000)ms, then make the color "off":
    if ((millis >= 1*interval && millis < 2*interval) || (millis >= 3*interval && millis < 4*interval)) {
      setColor(LEDColor.OFF);
    }
    else {
      setColor(color);
    }
  }

  public String colorToString(LEDColor color)
  {
    if (color == null) {
      return "NULL";
    }
    return color.toString();
  }

  public void setColor(LEDColor color) {
    // We cache the last color we set, so that we don't have
    //  to overwrite it except for when we actually are changing the colors.

    if (color == lastColor) {
      return;
    }

    System.out.println("Changing LED color from "+colorToString(lastColor)+" to "+colorToString(color));

    // If we've made it this far, it means we're actually changing the colors!
    lastColor = color;
    AddressableLEDBuffer buff = getBufferByColorEnum(color);
    m_led.setData(buff);
  }

  public void off() {
    setColor(LEDColor.OFF);
  }

  public void red() {
    setColor(LEDColor.RED);
  }

  public void blinkRed() {
    setBlinkColor(LEDColor.RED);
  }

  public void orange() {
    setColor(LEDColor.ORANGE);
  }

  public void blinkOrange() {
    setBlinkColor(LEDColor.ORANGE);
  }
  
  public void yellow() {
    setColor(LEDColor.YELLOW);
  }

  public void blinkYellow() {
    setBlinkColor(LEDColor.YELLOW);
  }

  public void green() {
    setColor(LEDColor.GREEN);
  }

  public void blinkGreen() {
    setBlinkColor(LEDColor.GREEN);
  }

  public void blue() {
    setColor(LEDColor.BLUE);
  }

  public void blinkBlue() {
    setBlinkColor(LEDColor.BLUE);
  }

  public void violet() {
    setColor(LEDColor.VIOLET);
  }

  public void blinkViolet() {
    setBlinkColor(LEDColor.VIOLET);
  }

  public void white()
  {
    setColor(LEDColor.WHITE);
  }

  public void blinkWhite()
  {
    setBlinkColor(LEDColor.WHITE);
  }
}