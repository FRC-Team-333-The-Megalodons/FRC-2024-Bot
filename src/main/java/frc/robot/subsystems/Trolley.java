// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.SparkPIDController;

import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.TrolleyConstants;

public class Trolley extends SubsystemBase {

  private CANSparkFlex trolleyMotor;
  private DigitalInput maxOutLimitSwitch, minInLimitSwitch;

  private SparkPIDController trolleyController;

  private Pivot pivotRef; // Needed to check limits.
  private Wrist wristRef;
  
  private AnalogInput potInput;
  private AnalogPotentiometer potentiometer;

  public Trolley() {
    trolleyMotor = new CANSparkFlex(TrolleyConstants.TROLLEY_MOTOR_ID, MotorType.kBrushless);
    maxOutLimitSwitch = new DigitalInput(TrolleyConstants.TROLLEY_OUT_LIMIT_SWITCH_ID);
    minInLimitSwitch = new DigitalInput(TrolleyConstants.TROLLEY_IN_LIMIT_SWITCH_ID);


    trolleyMotor.restoreFactoryDefaults();

    trolleyController = trolleyMotor.getPIDController();
    trolleyController.setFeedbackDevice(trolleyMotor.getEncoder());
    trolleyController.setP(TrolleyConstants.kP);
    trolleyController.setI(TrolleyConstants.kI);
    trolleyController.setD(TrolleyConstants.kD);
    trolleyController.setFF(TrolleyConstants.kFF);
    trolleyController.setOutputRange(TrolleyConstants.MIN_INPUT, TrolleyConstants.MAX_INPUT);

    trolleyMotor.setIdleMode(IdleMode.kBrake);

    trolleyMotor.burnFlash();

    
    potInput = new AnalogInput(TrolleyConstants.TROLLEY_POTENTIOMETER_ID);
    potInput.setAverageBits(2); // enable 2-bit averaging to smooth it out
    potentiometer = new AnalogPotentiometer(potInput);

  }

  public void setPivotRef(Pivot _pivotRef) {
    pivotRef = _pivotRef;
  }

  public void setWristRef(Wrist _wristRef) {
    wristRef = _wristRef;
  }

  public void resetEncoder() {
    trolleyMotor.getEncoder().setPosition(0.0);
  }

  public void runTrolley(double speed) {
    // Negative number means moving trolley in; positive number means moving trolley out.
    if (speed > 0) {
      if (isTrolleyAtMaxOutLimitSwitch()) {
        stopTrolley();
        return;
      }
    } else if (speed < 0) {
      if (isTrolleyAtMinInLimitSwitch()) {
        stopTrolley();
        return;
      }
    }
    trolleyMotor.set(speed);
  }

  public void stopTrolley() {
    trolleyMotor.set(0.0);
  }

  public void setPosition(double setpoint) {
    trolleyController.setReference(setpoint, ControlType.kPosition);
  }

  public boolean atSetpoint(double setpoint) {
    // If our encoder is at a premeditated setpoint, return true, otherwise return false
    return (getPotentiometerPosition() == setpoint);
  }

  public boolean fuzzyEquals(double a, double b) {
    final double epsilon = 0.01;
    return Math.abs(a-b) < epsilon;
  }

  public double getPotentiometerPosition() {
      // We flip the sign, add a constant, and multiply by 100 to
      //  make this number more "intuitive" / legible.
      return potentiometer.get() * -100 + 7;
  }

  public boolean isOkToMoveTrolleyOut() {
    if (isTrolleyAtMaxOutLimitSwitch()) {
      return false;
    }
    return true;
  }

  public boolean isOkToMoveTrolleyIn() {
    if (isTrolleyAtMinInLimitSwitch()) {
      return false;
    }
    return true;
  }
  
  public boolean isTrolleyAtMaxOutLimitSwitch() {
    return !maxOutLimitSwitch.get();
  }

  public boolean isTrolleyAtMinInLimitSwitch() {
    if (!minInLimitSwitch.get()) {
      resetEncoder();
    }
    return !minInLimitSwitch.get();
  }

  // This function returns whether the trolley is "past the frame perimeter".
  // This could technically vary based on where the wrist is, but for now we'll just use a single value.
  public boolean isTrolleyOut() {
    // return getPotentiometerPosition() >= TrolleyConstants.TROLLEY_IN_OUT_THRESHOLD;
    // workaround till pot is online
    return !isTrolleyAtMinInLimitSwitch();
  }

  public boolean isTrolleyIn() {
    return !isTrolleyOut();
  }

  public boolean isTrolleyTooFarInToPivotVertical() {
    return getPotentiometerPosition() < TrolleyConstants.TROLLEY_FURTHEST_IN_WHERE_PIVOT_CAN_MOVE_ALL_THE_WAY_UP;
  }

  public boolean isTrolleyTooFarInToPivotUpPastBumper() {
    return getPotentiometerPosition() < TrolleyConstants.TROLLEY_FURTHEST_IN_WHERE_PIVOT_CAN_CLEAR_BACK_BUMPER;
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("TrolleyMaxOutLimit", isTrolleyAtMaxOutLimitSwitch());
    SmartDashboard.putBoolean("TrolleyMinInLimit", isTrolleyAtMinInLimitSwitch());
    SmartDashboard.putNumber("TrolleyEncoder" , trolleyMotor.getEncoder().getPosition());
    SmartDashboard.putBoolean("TrolleyAtSetpoint", atSetpoint(getPotentiometerPosition()));
  }
}
