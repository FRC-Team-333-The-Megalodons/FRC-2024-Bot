// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ShooterConstants;

public class Shooter extends SubsystemBase {

  private CANSparkFlex rightMotor, leftMotor;

  private SparkPIDController shooterController;

  /** Creates a new Shooter. */
  public Shooter() {
    rightMotor = new CANSparkFlex(ShooterConstants.RIGHT_MOTOR_ID, MotorType.kBrushless);
    leftMotor = new CANSparkFlex(ShooterConstants.LEFT_MOTOR_ID, MotorType.kBrushless);

    rightMotor.restoreFactoryDefaults();
    leftMotor.restoreFactoryDefaults();

    shooterController = rightMotor.getPIDController();
    shooterController.setFeedbackDevice(rightMotor.getEncoder());
    shooterController.setP(ShooterConstants.kP);
    shooterController.setI(ShooterConstants.kI);
    shooterController.setD(ShooterConstants.kD);
    shooterController.setFF(ShooterConstants.kFF);
    shooterController.setOutputRange(ShooterConstants.MIN_INPUT, ShooterConstants.MAX_INPUT);

    rightMotor.setInverted(false);
    rightMotor.setIdleMode(IdleMode.kCoast);
    
    leftMotor.setIdleMode(IdleMode.kCoast);

    rightMotor.burnFlash();
    leftMotor.burnFlash();
  }

  public double getVelocity() {
    return rightMotor.getEncoder().getVelocity();
  }

  public void runShooter(double value) {
    rightMotor.set(value);
    // leftMotor.follow(rightMotor, true);
    leftMotor.set(-(value - 0.1));
  }

  public void stopShooter() {
    rightMotor.set(0.0);
    leftMotor.set(0.0);
  }

  public void setSpeed(double speed) {
    shooterController.setReference(speed, ControlType.kVelocity);
    leftMotor.follow(rightMotor);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter Speed", getVelocity());
  }
}