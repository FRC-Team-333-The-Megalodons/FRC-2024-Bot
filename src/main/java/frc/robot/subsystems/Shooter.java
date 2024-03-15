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

  private CANSparkFlex topMotor, bottomMotor;

  private SparkPIDController shooterController;

  /** Creates a new Shooter. */
  public Shooter() {
    topMotor = new CANSparkFlex(ShooterConstants.TOP_MOTOR_ID, MotorType.kBrushless);
    bottomMotor = new CANSparkFlex(ShooterConstants.BOTTOM_MOTOR_ID, MotorType.kBrushless);

    topMotor.restoreFactoryDefaults();
    bottomMotor.restoreFactoryDefaults();

    shooterController = topMotor.getPIDController();
    shooterController.setFeedbackDevice(topMotor.getEncoder());
    shooterController.setP(ShooterConstants.kP);
    shooterController.setI(ShooterConstants.kI);
    shooterController.setD(ShooterConstants.kD);
    shooterController.setFF(ShooterConstants.kFF);
    shooterController.setOutputRange(ShooterConstants.MIN_INPUT, ShooterConstants.MAX_INPUT);

    topMotor.setInverted(true);

    topMotor.setIdleMode(IdleMode.kCoast);
    bottomMotor.setIdleMode(IdleMode.kCoast);

    topMotor.burnFlash();
    bottomMotor.burnFlash();
  }

  public double getVelocity() {
    return topMotor.getEncoder().getVelocity();
  }

  public void runShooter(double value) {
    topMotor.set(value);
    bottomMotor.follow(topMotor);
  }

  public void stopShooter() {
    topMotor.set(0.0);
    bottomMotor.set(0.0);
  }

  public void setSpeed(double speed) {
    shooterController.setReference(speed, ControlType.kVelocity);
    bottomMotor.follow(topMotor);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Shooter Speed", getVelocity());
  }
}