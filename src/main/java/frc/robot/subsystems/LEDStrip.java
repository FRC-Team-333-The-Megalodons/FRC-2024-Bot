// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import java.util.HashMap;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.LED_MODE_OPTIONS;

public class LEDStrip extends SubsystemBase {
  private AddressableLED m_led;

  // private AddressableLEDBuffer offBuffer, redBuffer, orangeBuffer, yellowBuffer, greenBuffer, blueBuffer, violetBuffer, whiteBuffer;

  private final int LED_COUNT = 33;
  private final int LED_COUNT_BOTTOM = 21, LED_COUNT_TOP = 12;
  private LEDColor lastColor;
  private LEDColor lastBottomColor, lastTopColor;

  public enum LEDColor {
    OFF, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET, WHITE;
  }
  class RGB {
    int r, g, b;
    public RGB(int _r, int _g, int _b) {
      r = _r;
      g = _g;
      b = _b;
    }
  }

  RGB OFF_RGB = new RGB(0, 0, 0);
  RGB RED_RGB = new RGB(250, 0, 0);
  RGB ORANGE_RGB = new RGB(255, 43, 0);
  RGB YELLOW_RGB = new RGB(230, 223, 0);
  RGB GREEN_RGB =  new RGB(0, 250, 0);
  RGB BLUE_RGB = new RGB(0, 59, 174);
  RGB VIOLET_RGB = new RGB(255, 0, 255);
  RGB WHITE_RGB = new RGB(221, 222, 223);

  HashMap<String, AddressableLEDBuffer> bufferCache = new HashMap<>();

  /** Creates a new LEDS. */
  public LEDStrip() {
    //initializeBuffers();

    m_led = new AddressableLED(6); // LEDs

    m_led.setLength(LED_COUNT);
    setColor(LEDColor.OFF);
    m_led.start();
  }

  // private AddressableLEDBuffer getBufferByColorEnum(LEDColor color) {
  //   switch (color) {
  //     case OFF: return offBuffer;
  //     case RED: return redBuffer;
  //     case ORANGE: return orangeBuffer;
  //     case YELLOW: return yellowBuffer;
  //     case GREEN: return greenBuffer;
  //     case BLUE: return blueBuffer;
  //     case VIOLET: return violetBuffer;
  //     case WHITE: return whiteBuffer;
  //     default: return offBuffer;
  //   }
  // }

  private RGB colorToRGB(LEDColor color)
  {
    if (color == null) {
      return OFF_RGB;
    }
    switch (color) {
      case RED: return RED_RGB;
      case ORANGE: return ORANGE_RGB;
      case YELLOW: return YELLOW_RGB;
      case GREEN: return GREEN_RGB;
      case BLUE: return BLUE_RGB;
      case VIOLET: return VIOLET_RGB;
      case WHITE: return WHITE_RGB;
      default: return OFF_RGB;
    }
  }

  private AddressableLEDBuffer getLEDBuffer(LEDColor color)
  {
    // Make sure we dont have nulls here. Treat null as off.
    if (color == null) {
      color = LEDColor.OFF;
    }
    String key = colorToString(color);
    if (!bufferCache.containsKey(key)) {
      System.out.println("Cache miss for single color: "+key);
      RGB rgb = colorToRGB(color);
      bufferCache.put(key,  makeBuffer(LED_COUNT, rgb));
    }

    return bufferCache.get(key);
  }

  private AddressableLEDBuffer getLEDBuffer(LEDColor color1, LEDColor color2)
  {
    // Make sure we dont have nulls here. Treat null as off.
    if (color1 == null) {
      color1 = LEDColor.OFF;
    }
    if (color2 == null) {
      color2 = LEDColor.OFF;
    }
    String key = colorToString(color1) + "_" + colorToString(color2);
    if (!bufferCache.containsKey(key)) {
      System.out.println("Cache miss for two color: "+key);
      RGB rgb1 = colorToRGB(color1);
      RGB rgb2 = colorToRGB(color2);
      bufferCache.put(key,  makeBuffer(LED_COUNT_BOTTOM, rgb1, LED_COUNT_TOP, rgb2));
    }

    return bufferCache.get(key);
  }

  private AddressableLEDBuffer makeBuffer(int count, RGB rgb) {
    AddressableLEDBuffer buffer = new AddressableLEDBuffer(count);
    for (int i = 0; i < buffer.getLength(); ++i) {
      buffer.setRGB(i, rgb.r, rgb.g, rgb.b);
    }
    return buffer;
  }

  private AddressableLEDBuffer makeBuffer(int count1, RGB rgb1, int count2, RGB rgb2) {
    AddressableLEDBuffer buffer = new AddressableLEDBuffer(count1+count2);
    for (int i = 0; i < count1; ++i) {
      buffer.setRGB(i, rgb1.r, rgb1.g, rgb1.b);
    }
    for (int i = 0; i < count2; ++i) {
      buffer.setRGB(i, rgb2.r, rgb2.g, rgb2.b);
    }
    return buffer;
  }

  // private void initializeBuffers() {
  //   offBuffer = makeBuffer(LED_COUNT, OFF_RGB);
  //   redBuffer = makeBuffer(LED_COUNT, RED_RGB);
  //   orangeBuffer = makeBuffer(LED_COUNT, ORANGE_RGB);
  //   yellowBuffer = makeBuffer(LED_COUNT, YELLOW_RGB);
  //   greenBuffer = makeBuffer(LED_COUNT, GREEN_RGB);
  //   blueBuffer = makeBuffer(LED_COUNT, BLUE_RGB);
  //   violetBuffer = makeBuffer(LED_COUNT, VIOLET_RGB);
  //   whiteBuffer = makeBuffer(LED_COUNT, WHITE_RGB);
  // }

  // public void setBlinkColor(LEDColor color) {
  //   final long interval = 250;
  //   long millis = System.currentTimeMillis() % 1000;

  //   // If we're between [250,500) ms, or [750,1000)ms, then make the color "off":
  //   if ((millis >= 1*interval && millis < 2*interval) || (millis >= 3*interval && millis < 4*interval)) {
  //     setColor(LEDColor.OFF);
  //   }
  //   else {
  //     setColor(color);
  //   }
  // }

  public String colorToString(LEDColor color)
  {
    if (color == null) {
      return "NULL";
    }
    return color.toString();
  }

  public void setColor(LEDColor color) {
    if (Constants.LED_MODE == LED_MODE_OPTIONS.TWO_COLOR) {
      setBottomColor(color);
      return;
    }

    // We cache the last color we set, so that we don't have
    //  to overwrite it except for when we actually are changing the colors.

    if (color == lastColor) {
      return;
    }

    System.out.println("Changing LED color from "+colorToString(lastColor)+" to "+colorToString(color));

    // If we've made it this far, it means we're actually changing the colors!
    lastColor = color;
    // AddressableLEDBuffer buff = getBufferByColorEnum(color);
    AddressableLEDBuffer buff = getLEDBuffer(color);
    m_led.setData(buff);
  }

  public void setBottomColor(LEDColor bottomColor) {
    if (bottomColor == lastBottomColor) {
      return;
    }

    System.out.println("Changing Bottom LED color from "+colorToString(lastBottomColor)+" to "+colorToString(bottomColor));

    lastBottomColor = bottomColor;

    AddressableLEDBuffer buff = getLEDBuffer(bottomColor, lastTopColor);
    m_led.setData(buff);
  }

  public void setTopColor(LEDColor topColor) {
    if (topColor == lastTopColor) {
      return;
    }

    System.out.println("Changing Top LED color from "+colorToString(lastTopColor)+" to "+colorToString(topColor));

    lastTopColor = topColor;

    AddressableLEDBuffer buff = getLEDBuffer(lastBottomColor, topColor);
    m_led.setData(buff);
  }

  public void off() {
    setColor(LEDColor.OFF);
  }

  public void red() {
    setColor(LEDColor.RED);
  }

  // public void blinkRed() {
  //   setBlinkColor(LEDColor.RED);
  // }

  public void orange() {
    setColor(LEDColor.ORANGE);
  }

  // public void blinkOrange() {
  //   setBlinkColor(LEDColor.ORANGE);
  // }
  
  public void yellow() {
    setColor(LEDColor.YELLOW);
  }

  // public void blinkYellow() {
  //   setBlinkColor(LEDColor.YELLOW);
  // }

  public void green() {
    setColor(LEDColor.GREEN);
  }

  // public void blinkGreen() {
  //   setBlinkColor(LEDColor.GREEN);
  // }

  public void blue() {
    setColor(LEDColor.BLUE);
  }

  // public void blinkBlue() {
  //   setBlinkColor(LEDColor.BLUE);
  // }

  public void violet() {
    setColor(LEDColor.VIOLET);
  }

  // public void blinkViolet() {
  //   setBlinkColor(LEDColor.VIOLET);
  // }

  public void white()
  {
    setColor(LEDColor.WHITE);
  }

  // public void blinkWhite()
  // {
  //   setBlinkColor(LEDColor.WHITE);
  // }
}