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
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.WristConstants;

public class Wrist extends SubsystemBase {

  private CANSparkMax wristMotor;
  private SparkPIDController wristController;
  private AbsoluteEncoder wristEncoder;
  private Pivot pivotRef;
  private Trolley trolleyRef;

  /** Creates a new Wrist. */
  public Wrist() {
    wristMotor = new CANSparkMax(WristConstants.MOTOR_ID, MotorType.kBrushless);

    wristMotor.restoreFactoryDefaults();

    wristEncoder = wristMotor.getAbsoluteEncoder(Type.kDutyCycle);
    wristEncoder.setInverted(true);

    wristController = wristMotor.getPIDController();
    wristController.setFeedbackDevice(wristEncoder);
    wristController.setP(WristConstants.kP);
    wristController.setI(WristConstants.kI);
    wristController.setD(WristConstants.kD);
    wristController.setFF(WristConstants.kFF);
    wristController.setOutputRange(WristConstants.MIN_INPUT, WristConstants.MAX_INPUT);

    wristMotor.setIdleMode(IdleMode.kBrake);

    wristMotor.burnFlash();
  }

  public void setPivotRef(Pivot _pivotRef)
  {
    pivotRef = _pivotRef;
  }

  public void setTrolleyRef(Trolley _trolleyRef)
  {
    trolleyRef = _trolleyRef;
  }

  public double getPosition() {
    return wristEncoder.getPosition();
  }

  public void runWrist(double speed) {
    // if (mustStopDueToLimit(speed)) {
    //   stopWrist();
    //   return;
    // }

    wristMotor.set(speed);
  }

  public void stopWrist() {
    wristMotor.set(0.0);
  }

  public void setPosition(double setpoint) {
    // TODO: How can we apply the limits here and cancel the controller?
    // We can "guess" at what direction it'll set:
    // double direction = (setpoint > getPosition() ? 1.0 : -1.0);
    // if (mustStopDueToLimit(direction)) {
    //   stopWrist(); // TODO: Verify that doing `set` on the motor cancels the closed-loop mode by setReference (I hope so, nothing in the documentation explains how to do it if not)
    //   return;
    // }

    wristController.setReference(setpoint, ControlType.kPosition);
  }

  // private double getDownLimitFromState()
  // {
  //   if (pivotRef.getPosition() > PivotConstants.HOME_SETPOINT_POS) {
  //     // This intends to say, "If the Pivot is up (i.e. the wrist is down), then don't rotate the wrist down past the point of intake, else it'll smack the bumper/interior."
  //     return WristConstants.INTAKE_SETPOINT_POS;
  //   }
  //   return WristConstants.DOWN_LIMIT_POS;
  // }

  // private double getUpLimitFromState()
  // {
  //   //  This is always the hard-stop (i.e. flush) limit
  //   return WristConstants.UP_LIMIT_POS;
  // }

  private boolean isWristAtMaxDown()
  {
    // TODO
    return false;
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Wrist Pos", getPosition());
  }
}
