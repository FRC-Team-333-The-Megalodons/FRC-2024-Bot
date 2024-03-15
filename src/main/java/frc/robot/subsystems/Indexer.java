// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IndexerConstants;

public class Indexer extends SubsystemBase {

  private CANSparkMax indexMotor;

  private SparkPIDController indexController;

  /** Creates a new Indexer. */
  public Indexer() {
    indexMotor = new CANSparkMax(IndexerConstants.MOTOR_ID, MotorType.kBrushless);

    indexMotor.restoreFactoryDefaults();

    indexController = indexMotor.getPIDController();
    indexController.setFeedbackDevice(indexMotor.getEncoder());
    indexController.setP(IndexerConstants.kP);
    indexController.setI(IndexerConstants.kI);
    indexController.setD(IndexerConstants.kD);
    indexController.setFF(IndexerConstants.kFF);
    indexController.setOutputRange(IndexerConstants.MIN_INPUT, IndexerConstants.MAX_INPUT);

    indexMotor.setIdleMode(IdleMode.kCoast);
    indexMotor.setInverted(true);

    indexMotor.burnFlash();
  }

  public double getVelocity() {
    return indexMotor.getEncoder().getVelocity();
  }

  public void runIndexer(double value) {
    indexMotor.set(value);
  }

  public void stopIndexer() {
    indexMotor.set(0.0);
  }

  public void setSpeed(double speed) {
    indexController.setReference(speed, ControlType.kVelocity);
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Indexer Speed", getVelocity());
  }
}