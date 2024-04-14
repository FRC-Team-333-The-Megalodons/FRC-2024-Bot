// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import java.util.HashMap;

import edu.wpi.first.math.Pair;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class LEDStrip extends SubsystemBase {
  private AddressableLED m_led;

  private AddressableLEDBuffer offBuffer, redBuffer, orangeBuffer, yellowBuffer, greenBuffer, blueBuffer, violetBuffer, whiteBuffer;

  private final int LED_COUNT_BOTTOM = 23; 
  private final int LED_COUNT_TOP = 10;
  private LEDColor lastColorBottom, lastColorTop;

  public enum LEDColor {
    OFF, RED, ORANGE, YELLOW, GREEN, BLUE, VIOLET, WHITE;
  }

  public class RGB {
    public int r, g, b;
    public RGB(int _r, int _g, int _b) { 
      r = _r;
      g = _g;
      b = _b;
    }
  }

  /** Creates a new LEDS. */
  public LEDStrip() {
    initializeBuffers();

    m_led = new AddressableLED(6); // LEDs

    m_led.setLength(LED_COUNT_BOTTOM+LED_COUNT_TOP);
    setBottomColor(LEDColor.OFF);
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

  private AddressableLEDBuffer makeBuffer(int count, LEDColor color)
  {
    RGB rgb = colorToRGB(color);
    return makeBuffer(count, rgb.r, rgb.g, rgb.b);
  }


  private RGB colorToRGB(LEDColor color)
  {
    switch(color) {
      case RED: return new RGB(250, 0, 0);
      case ORANGE: return new RGB(255, 43, 0);
      case YELLOW: return new RGB(230, 223, 0);
      case GREEN: return new RGB(0, 250, 0);
      case BLUE: return new RGB(0, 59, 174);
      case VIOLET: return new RGB(255, 0, 255);
      case WHITE: return new RGB(221, 222, 223);
      case OFF: 
      default: return new RGB(0, 0, 0);
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
    offBuffer = makeBuffer(LED_COUNT_BOTTOM, LEDColor.OFF);
    redBuffer = makeBuffer(LED_COUNT_BOTTOM, LEDColor.RED);
    orangeBuffer = makeBuffer(LED_COUNT_BOTTOM, LEDColor.ORANGE);
    yellowBuffer = makeBuffer(LED_COUNT_BOTTOM, LEDColor.YELLOW);
    greenBuffer = makeBuffer(LED_COUNT_BOTTOM, LEDColor.GREEN);
    blueBuffer = makeBuffer(LED_COUNT_BOTTOM, LEDColor.BLUE);
    violetBuffer = makeBuffer(LED_COUNT_BOTTOM, LEDColor.VIOLET);
    whiteBuffer = makeBuffer(LED_COUNT_BOTTOM, LEDColor.WHITE);
  }

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

  public void setBottomColor(LEDColor color) {
    // We cache the last color we set, so that we don't have
    //  to overwrite it except for when we actually are changing the colors.

    if (color == lastColorBottom) {
      return;
    }

    System.out.println("Changing LED bottom color from "+colorToString(lastColorBottom)+" to "+colorToString(color));

    // If we've made it this far, it means we're actually changing the colors!
    lastColorBottom = color;
    //AddressableLEDBuffer buff = getBufferByColorEnum(color);
    AddressableLEDBuffer buff = getTwoColorBuffer(lastColorBottom, lastColorTop);
    m_led.setData(buff);
  }

    public void setTopColor(LEDColor color) {
    // We cache the last color we set, so that we don't have
    //  to overwrite it except for when we actually are changing the colors.

    if (color == lastColorTop) {
      return;
    }

    System.out.println("Changing LED top color from "+colorToString(lastColorTop)+" to "+colorToString(color));

    // If we've made it this far, it means we're actually changing the colors!
    lastColorTop = color;
    //AddressableLEDBuffer buff = getBufferByColorEnum(color);
    AddressableLEDBuffer buff = getTwoColorBuffer(lastColorBottom, lastColorTop);
    m_led.setData(buff);
  }
  
  public Pair<LEDColor, LEDColor> makeKey(LEDColor first, LEDColor second)
  {
    return new Pair<LEDColor, LEDColor>(first, second);
  }

  public HashMap<Pair<LEDColor, LEDColor>, AddressableLEDBuffer> bufferCache = new HashMap<>();

  public AddressableLEDBuffer getTwoColorBuffer(LEDColor bottomColor, LEDColor topColor)
  {
    Pair<LEDColor, LEDColor> key = makeKey(bottomColor, topColor);
    if (bufferCache.containsKey(key)) {
      System.out.println("Cache hit for Buffer Factory for "+colorToString(bottomColor)+":"+colorToString(topColor));

      return bufferCache.get(key);
    }

    
    System.out.println("Cache MISS, making Buffer for "+colorToString(bottomColor)+":"+colorToString(topColor));

    // If we didn't have it cached, we have to create it and cache it.
    AddressableLEDBuffer buff = new AddressableLEDBuffer(LED_COUNT_BOTTOM+LED_COUNT_TOP);
    RGB bottomRGB = colorToRGB(bottomColor);
    RGB topRGB = colorToRGB(topColor);
    for (int i = 0; i < buff.getLength(); ++i) {
      if (i < LED_COUNT_BOTTOM) {
        buff.setRGB(i, bottomRGB.r, bottomRGB.g, bottomRGB.b);
      } else {
        buff.setRGB(i, topRGB.r, topRGB.g, topRGB.b);
      }
    }

    bufferCache.put(key, buff);
    return buff;
  }

  public void off() {
    setBottomColor(LEDColor.OFF);
  }

  public void red() {
    setBottomColor(LEDColor.RED);
  }

  // public void blinkRed() {
  //   setBlinkColor(LEDColor.RED);
  // }

  public void orange() {
    setBottomColor(LEDColor.ORANGE);
  }

  // public void blinkOrange() {
  //   setBlinkColor(LEDColor.ORANGE);
  // }
  
  public void yellow() {
    setBottomColor(LEDColor.YELLOW);
  }

  // public void blinkYellow() {
  //   setBlinkColor(LEDColor.YELLOW);
  // }

  public void green() {
    setBottomColor(LEDColor.GREEN);
  }

  // public void blinkGreen() {
  //   setBlinkColor(LEDColor.GREEN);
  // }

  public void blue() {
    setBottomColor(LEDColor.BLUE);
  }

  // public void blinkBlue() {
  //   setBlinkColor(LEDColor.BLUE);
  // }

  public void violet() {
    setBottomColor(LEDColor.VIOLET);
  }

  // public void blinkViolet() {
  //   setBlinkColor(LEDColor.VIOLET);
  // }

  public void white()
  {
    setBottomColor(LEDColor.WHITE);
  }

  // public void blinkWhite()
  // {
  //   setBlinkColor(LEDColor.WHITE);
  // }
}