// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDStrip extends SubsystemBase {
  private AddressableLED m_led;

  private AddressableLEDBuffer offBuffer, redBuffer, orangeBuffer, yellowBuffer, greenBuffer, blueBuffer, violetBuffer;

  private final int LED_COUNT = 100; 
  private LEDColor lastColor;

  public enum LEDColor
  {
    OFF, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET;
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
      default: return offBuffer;
    }
  }

  private AddressableLEDBuffer makeBuffer(int count, int r, int g, int b)
  {
    AddressableLEDBuffer buffer = new AddressableLEDBuffer(count);
    for (int i = 0; i < buffer.getLength(); ++i) {
      buffer.setRGB(i, r, g, b);
    }
    return buffer;
  }

  private void initializeBuffers() {
    offBuffer = makeBuffer(LED_COUNT, 0, 0, 0);
    //
    redBuffer = makeBuffer(LED_COUNT, 250, 0, 0);
    orangeBuffer = makeBuffer(LED_COUNT, 255, 43, 0);
    yellowBuffer = makeBuffer(LED_COUNT, 230, 223, 0);
    greenBuffer = makeBuffer(LED_COUNT, 0, 250, 0);
    blueBuffer = makeBuffer(LED_COUNT, 0, 59, 174);
    violetBuffer = makeBuffer(LED_COUNT, 255, 0, 255);
  }

  public void setBlinkColor(LEDColor color) {
    final long interval = 250;
    long millis = System.currentTimeMillis() % 1000;

    // If we're between [250,500) ms, or [750,1000)ms, then make the color "off":
    if ((millis >= 1*interval && millis < 2*interval) || (millis >= 3*interval && millis < 4*interval)) 
    {
      setColor(LEDColor.OFF);
    }
    else
    {
      setColor(color);
    }
  }

  public void setColor(LEDColor color) {
    // We cache the last color we set, so that we don't have
    //  to overwrite it except for when we actually are changing the colors.
    if (color == lastColor) {
      return;
    }

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
  
  // WARNING: This function never exits, it loops forever!!!
  public void shadesOFBlue() {
    
    AddressableLEDBuffer m_ledBuffer = new  AddressableLEDBuffer(100);
    int length = m_ledBuffer.getLength();
    int blue = 80;
    int red = 20;
    int green = 20;
    while (true) {
        blue = (blue + 1) % 210;
        red = (red + 1) % 120;
        green = (green + 1) % 100;
        blue = (blue + 50) % 200;
        red = (red - 20) % 20;
        green = (green - 20) % 20;
        for (var i = 0; i < length; i++) {
            m_ledBuffer.setRGB(i, red, green, blue); // Измените значения R, G и B, чтобы создать разноцветный эффект
            m_ledBuffer.setRGB(i, 0, 0, blue); // Измените значения R, G и B, чтобы создать разноцветный эффект
        }
        m_led.setData(m_ledBuffer);
        try {
            Thread.sleep(40); // Подождите некоторое время перед обновлением цвета
            Thread.sleep(300); // Подождите некоторое время перед обновлением цвета
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
      }
    }
}