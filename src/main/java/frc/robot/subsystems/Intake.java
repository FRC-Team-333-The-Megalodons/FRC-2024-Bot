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
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {
  
  private CANSparkFlex intakeMotor;
  private DigitalInput leftSensor;
  private DigitalInput rightSensor;

  private SparkPIDController intakeController;
  Long noteSensorTime = null;

  /** Creates a new Intake. */
  public Intake() {
    
    intakeMotor = new CANSparkFlex(IntakeConstants.MOTOR_ID, MotorType.kBrushless);

    intakeMotor.restoreFactoryDefaults();

    intakeMotor.setIdleMode(IdleMode.kCoast);
    
    intakeMotor.burnFlash();

    intakeController = intakeMotor.getPIDController();
    intakeController.setFeedbackDevice(intakeMotor.getEncoder());
    intakeController.setP(IntakeConstants.kP);
    intakeController.setI(IntakeConstants.kI);
    intakeController.setD(IntakeConstants.kD);
    intakeController.setFF(IntakeConstants.kFF);
    intakeController.setOutputRange(IntakeConstants.MIN_INPUT, IntakeConstants.MAX_INPUT);

    leftSensor = new DigitalInput(IntakeConstants.LEFT_SENSOR_ID);
    rightSensor = new DigitalInput(IntakeConstants.RIGHT_SENSOR_ID);
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
    boolean leftTriggered = leftSensor.get(), rightTriggered = rightSensor.get();
    if (leftTriggered || rightTriggered) {
      //System.out.println("HAS NOTE! (left="+leftTriggered+", right="+rightTriggered+")");
      return true;
    } else {
      return false;
    }
  }

  // public boolean hasNoteWithDelay()
  // {
  //   if (!hasNote()) {
  //     noteSensorTime = null;
  //     return false;
  //   }

  //   // We only get this far if hasNote is true!
  //   if (noteSensorTime == null) {
  //     // We just got the note! Write down when we got it.
  //     noteSensorTime = System.currentTimeMillis();
  //   }

  //   long elapsed = System.currentTimeMillis() - noteSensorTime;

  //   return (elapsed >= IntakeConstants.INTAKE_HAS_NOTE_DELAY);
  // }
  
  public boolean shotTheNote(){
    return !hasNote();
    // if(leftSensor.get()|| rightSensor.get()){
    //   return false;
    // }else {
    //   return true;
    // }
  }

  public void resetEncoder() {
    intakeMotor.getEncoder().setPosition(0.0);
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Note?", hasNote());
    SmartDashboard.putBoolean("Left Note Sensor", leftSensor.get());
    SmartDashboard.putBoolean("Right Note Sensor", rightSensor.get());
    SmartDashboard.putNumber("Intake Pos", getPosition());
    SmartDashboard.putBoolean("ShotTheNote?", shotTheNote());
  }
}
