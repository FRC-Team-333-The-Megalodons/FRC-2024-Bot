// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkBase.ControlType;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstatnts;

public class Intake extends SubsystemBase {
  
  private CANSparkFlex intakeMotor;
  private DigitalInput leftSensor;
  private DigitalInput rightSensor;

  private SparkPIDController intakeController;

  /** Creates a new Intake. */
  public Intake() {
    intakeMotor = new CANSparkFlex(IntakeConstatnts.MOTOR_ID, MotorType.kBrushless);

    intakeMotor.restoreFactoryDefaults();

    intakeMotor.setIdleMode(IdleMode.kCoast);
    
    intakeMotor.burnFlash();

    intakeController = intakeMotor.getPIDController();
    intakeController.setFeedbackDevice(intakeMotor.getEncoder());
    intakeController.setP(IntakeConstatnts.kP);
    intakeController.setI(IntakeConstatnts.kI);
    intakeController.setD(IntakeConstatnts.kD);
    intakeController.setFF(IntakeConstatnts.kFF);
    intakeController.setOutputRange(IntakeConstatnts.MIN_INPUT, IntakeConstatnts.MAX_INPUT);

    leftSensor = new DigitalInput(IntakeConstatnts.LEFT_SENSOR_ID);
    rightSensor = new DigitalInput(IntakeConstatnts.RIGHT_SENSOR_ID);
  }

  public void runIntake(double value) {
    intakeMotor.set(value);
  }

  public void stopIntake() {
    intakeMotor.set(0.0);
  }

  public double getPosition() {
    return intakeMotor.getEncoder().getPosition();
  }

  public void setPosition(double setpoint) {
    intakeController.setReference(setpoint, ControlType.kPosition);
  }

  public boolean hasNote() {
    if (leftSensor.get() || rightSensor.get()) {
      return true;
    } else {
      return false;
    }
  }

  public void resetEncoder() {
    intakeMotor.getEncoder().setPosition(0.0);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Note?", hasNote());
    SmartDashboard.putNumber("Intake Pos", getPosition());
  }
}
