// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.SparkAbsoluteEncoder.Type;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.WristConstants;

public class Wrist extends SubsystemBase {

  private CANSparkMax wristMotor;
  private SparkPIDController wristController;
  private AbsoluteEncoder wristEncoder;
  
  private double mostRecentWristControllerSetpoint;

  /** Creates a new Wrist. */
  public Wrist() {
    wristMotor = new CANSparkMax(WristConstants.MOTOR_ID, MotorType.kBrushless);
    wristMotor.restoreFactoryDefaults();

    wristEncoder = wristMotor.getAbsoluteEncoder(Type.kDutyCycle);
    wristEncoder.setInverted(false);
    wristEncoder.setZeroOffset(WristConstants.ZERO_OFFSET);

    wristController = wristMotor.getPIDController();
    wristController.setFeedbackDevice(wristEncoder);
    wristController.setP(WristConstants.kP);
    wristController.setI(WristConstants.kI);
    wristController.setD(WristConstants.kD);
    wristController.setFF(WristConstants.kFF);
    
    wristController.setOutputRange(WristConstants.MIN_INPUT, WristConstants.MAX_INPUT);

    wristMotor.setInverted(true);
    wristMotor.setIdleMode(IdleMode.kBrake);

    wristMotor.burnFlash();
  }

  public double getPosition() {
    return wristEncoder.getPosition();
  }

  public void runWrist(double speed) {
    wristMotor.set(speed);
  }

  public void stopWrist() {
    wristMotor.set(0.0);
  }

  public boolean isWristDown() {
    return (getPosition() >= WristConstants.WRIST_DOWN_THRESHOLD);
  }

  public void setSetpoint(double setpoint) {
    mostRecentWristControllerSetpoint = setpoint;
    wristController.setReference(setpoint, ControlType.kPosition);
  }

  public boolean atSetpoint() {
    return Math.abs(wristEncoder.getPosition() - mostRecentWristControllerSetpoint) <= WristConstants.kTolerance;
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Wrist Pos", getPosition());
  }
}
