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
  /** Creates a new LEDS. */
  public LEDStrip() {
    // leftLED = new AddressableLED(1); // left LEDs
    rightLED = new AddressableLED(0); // right LEDs
    leftLEDBuffer = new  AddressableLEDBuffer(100);
    rightLEDBuffer = new  AddressableLEDBuffer(100);
    // leftLED.setLength(leftLEDBuffer.getLength());
    // leftLED.setData(leftLEDBuffer);
    // leftLED.start();
    rightLED.setLength(rightLEDBuffer.getLength());
    rightLED.setData(rightLEDBuffer);
    rightLED.start();
  }

  public void noLED() {
    for (var i=0; i< rightLEDBuffer.getLength(); i++) {
      // leftLEDBuffer.setRGB(i, 0, 0, 0);
      rightLEDBuffer.setRGB(i, 0, 0, 0);
    } 
    // leftLED.setData(leftLEDBuffer);
    rightLED.setData(rightLEDBuffer);
  }

  public void setBlinkLED(int r, int g, int b) {
    int length = rightLEDBuffer.getLength();

    for (int i = 0; i<length; i++) {
      // leftLEDBuffer.setRGB(i, r, g, b);
      rightLEDBuffer.setRGB(i, r, g, b);
    }
    // leftLED.setData(leftLEDBuffer);
    rightLED.setData(rightLEDBuffer);

    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    for (int i = 0; i<length; i++) {
      // leftLEDBuffer.setRGB(i , 0, 0, 0);
      rightLEDBuffer.setRGB(i , 0, 0, 0);
    }
    // leftLED.setData(leftLEDBuffer);
    rightLED.setData(rightLEDBuffer);

    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void royalBlueLED() {
    for (var i = 0; i< rightLEDBuffer.getLength(); i++) {
      // leftLEDBuffer.setRGB(i, 0, 59, 174);
      rightLEDBuffer.setRGB(i, 0, 59, 174);
    } 
    // leftLED.setData(leftLEDBuffer);
    rightLED.setData(rightLEDBuffer);
  }

  public void orangeLED() {
    for (var i = 0; i< rightLEDBuffer.getLength(); i++) {
      // leftLEDBuffer.setRGB(i, 255, 43, 0);
      rightLEDBuffer.setRGB(i, 255, 43, 0);
    }  
    // leftLED.setData(leftLEDBuffer);
    rightLED.setData(rightLEDBuffer);
  }

  public void yellowLED() {
    for (var i = 0; i< rightLEDBuffer.getLength(); i++) {
      // leftLEDBuffer.setRGB(i, 230, 223, 0);
      rightLEDBuffer.setRGB(i, 230, 223, 0);
    }  
    // leftLED.setData(leftLEDBuffer);
    rightLED.setData(rightLEDBuffer);
  }

  public void greenLED() {
    for (var i = 0; i< rightLEDBuffer.getLength(); i++) {
      // leftLEDBuffer.setRGB(i, 0, 250, 0);
      rightLEDBuffer.setRGB(i, 0, 250, 0);
    } 
    // leftLED.setData(leftLEDBuffer);
    rightLED.setData(rightLEDBuffer);
  }

  public void redLED() {
    for (var i = 0; i< rightLEDBuffer.getLength(); i++) {
      // leftLEDBuffer.setRGB(i, 250, 0, 0);
      rightLEDBuffer.setRGB(i, 250, 0, 0);
    } 
    // leftLED.setData(leftLEDBuffer);
    rightLED.setData(rightLEDBuffer);
  }
}