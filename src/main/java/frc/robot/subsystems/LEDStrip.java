// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDStrip extends SubsystemBase {
  private AddressableLED rightLED, leftLED;
  private AddressableLEDBuffer rightLEDBuffer, leftLEDBuffer;

  private AddressableLEDBuffer offBuffer, redBuffer, orangeBuffer, yellowBuffer, greenBuffer, blueBuffer, violetBuffer;

  private LEDColor lastColor;

  public enum LEDColor
  {
    OFF, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET;
  }

  /** Creates a new LEDS. */
  public LEDStrip() {
    initializeBuffers();

    rightLED = new AddressableLED(0); // right LEDs
    leftLEDBuffer = new  AddressableLEDBuffer(100);
    rightLEDBuffer = new  AddressableLEDBuffer(100);

    rightLED.setLength(rightLEDBuffer.getLength());
    rightLED.setData(rightLEDBuffer);
    rightLED.start();

    
    setColor(LEDColor.OFF);
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

  private void initializeBuffers() {
    final int LED_COUNT = 100; 
    offBuffer = new AddressableLEDBuffer(LED_COUNT);
    for (int i=0; i< offBuffer.getLength(); i++) {
      offBuffer.setRGB(i, 0, 0, 0);
    } 

    redBuffer = new AddressableLEDBuffer(LED_COUNT);
    for (int i=0; i< redBuffer.getLength(); i++) {
      redBuffer.setRGB(i, 250, 0, 0);
    }

    orangeBuffer = new AddressableLEDBuffer(LED_COUNT);
    for (int i=0; i< orangeBuffer.getLength(); i++) {
      orangeBuffer.setRGB(i, 255, 43, 0);
    }
      
    yellowBuffer = new AddressableLEDBuffer(LED_COUNT);
    for (int i=0; i< yellowBuffer.getLength(); i++) {
      yellowBuffer.setRGB(i, 230, 223, 0);
    }

    greenBuffer = new AddressableLEDBuffer(LED_COUNT);
    for (int i=0; i< greenBuffer.getLength(); i++) {
      greenBuffer.setRGB(i, 0, 250, 0);
    }

    blueBuffer = new AddressableLEDBuffer(LED_COUNT);
    for (int i=0; i< blueBuffer.getLength(); i++) {
      blueBuffer.setRGB(i, 0, 59, 174);
    }

    violetBuffer = new AddressableLEDBuffer(LED_COUNT);
    for (int i=0; i< violetBuffer.getLength(); i++) {
      violetBuffer.setRGB(i, 255, 0, 255);
    }


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
    rightLED.setData(buff);
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
  
  public void shadesOFBlue() {
    int length = rightLEDBuffer.getLength();
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
            rightLEDBuffer.setRGB(i, red, green, blue); // Измените значения R, G и B, чтобы создать разноцветный эффект
            rightLEDBuffer.setRGB(i, 0, 0, blue); // Измените значения R, G и B, чтобы создать разноцветный эффект
        }
        rightLED.setData(rightLEDBuffer);
        try {
            Thread.sleep(40); // Подождите некоторое время перед обновлением цвета
            Thread.sleep(300); // Подождите некоторое время перед обновлением цвета
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
      }
    }
}