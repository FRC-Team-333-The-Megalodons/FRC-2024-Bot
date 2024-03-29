// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.PivotConstants;

public class Pivot extends SubsystemBase {

  private CANSparkFlex pivotMotorLeader, pivotMotorFollower;
  private DutyCycleEncoder pivotEncoder;

  private PIDController pivotController;
  private PIDController autoPivotController;

  private Trolley trolleyRef; 
  private Wrist wristRef;
  /** Creates a new Pivot. */
  public Pivot() {
    pivotMotorLeader = new CANSparkFlex(PivotConstants.MOTOR1_ID, MotorType.kBrushless);
    pivotMotorFollower = new CANSparkFlex(PivotConstants.MOTOR2_ID, MotorType.kBrushless);

    pivotMotorLeader.restoreFactoryDefaults();
    pivotMotorFollower.restoreFactoryDefaults();

    pivotMotorLeader.setIdleMode(IdleMode.kBrake);
    pivotMotorFollower.setIdleMode(IdleMode.kBrake);

    
    pivotMotorFollower.follow(pivotMotorLeader);

    pivotMotorLeader.burnFlash();
    pivotMotorFollower.burnFlash();


    pivotEncoder = new DutyCycleEncoder(PivotConstants.PIVOT_ENCODER_ID);
    autoPivotController = new PIDController(PivotConstants.kAutoP,PivotConstants.kAutoI,PivotConstants.kAutoD);

    pivotController = new PIDController(PivotConstants.kP, PivotConstants.kI, PivotConstants.kD);
    pivotController.setTolerance(PivotConstants.kTolerance);
    autoPivotController.setTolerance(PivotConstants.kAutoTolerance);
  }

  public void setTrolleyRef(Trolley _trolleyRef) {
    trolleyRef = _trolleyRef;
  }

  public void setWristRef(Wrist _wristRef){
    wristRef = _wristRef;
  }

  public void runPivot(double speed) {
    // Negative value means "up". Positive value means "down".
    if (speed < 0) {
      if (!isOkToMovePivotUp()) {
        stopPivot();
        return;
      }
    } else if (speed > 0) {
      if (!isOkToMovePivotDown()) {
        stopPivot();
        return;
      }
    }
    pivotMotorLeader.set(speed);
  }

  public void stopPivot() {
    pivotMotorLeader.set(0.0);
    pivotMotorFollower.set(0.0);
  }

  public boolean isOkToMovePivotUp() {
    // Furthest possible limit. Nothing can invalidate this constraint.
    if (getPosition() >= PivotConstants.PIVOT_MAX_UP) {
      return false;
    }

    // For now, just return true here because my checks aren't working
    if (trolleyRef.isTrolleyTooFarInToPivotUpPastBumper()) {
      return !isPivotUpFarEnoughThatTrolleyCantMoveIn();
    }
    return true;
  }

  public boolean isOkToMovePivotDown() {
    // Furthest possible limit. Nothing can invalidate this constraint.
    if (getPosition() <= PivotConstants.PIVOT_MIN_DOWN) {
      return false;
    }

    if (trolleyRef.isTrolleyOut()) {
      // In the special case that the wrist is in the intake position, and that the trolley is all the way out, we can move down a bit further!
      if (trolleyRef.isTrolleyAtMaxOutLimitSwitch() && wristRef.isWristDown())
      {
        // IF we're in Intake Position, we can only go as far down as intake height.
        return getPosition() >= PivotConstants.INTAKE_SETPOINT_POS;
      }
      // Otherwise, if the Trolley is out, then we can only move down if we're above the "trolley can move safely" setpoint.
      return !isPivotDownFarEnoughThatTrolleyCantMoveOut();
    }

    return true;
  }

  public boolean isPivotDownFarEnoughThatTrolleyCantMoveOut() {
    return getPosition() <= PivotConstants.PIVOT_FURTHEST_DOWN_WHERE_TROLLEY_CAN_MOVE; 
  }

  public boolean isPivotUpFarEnoughThatTrolleyCantMoveIn() {
    return getPosition() >= PivotConstants.PIVOT_UP_FAR_ENOUGH_THAT_TROLLEY_COULD_HIT_BACK_BUMPER;
  }

  public boolean isPivotAtMaxDown() {
    return getPosition() <= PivotConstants.SUBWOFFER_SETPOINT_POS;
  }

  public double getPosition() {
    return pivotEncoder.getAbsolutePosition()*Constants.PivotConstants.PIVOT_ENCODER_MULTIPLIER;
  }

  public void setPosition(double setpoint) {
    double speed = pivotController.calculate(getPosition(), setpoint);
    runPivot(-speed);
  }
  public void setAutoPosition(double setpoint) {
    double speed = autoPivotController.calculate(getPosition(), setpoint);
    runPivot(-speed);
  }

  public boolean atSetpoint() {
    return pivotController.atSetpoint();
  }
  public boolean atAutoSetpoint(){
    return autoPivotController.atSetpoint();
  }

  // pivot shoot to look at april tag
  public Command aimAtTarget(PhotonCamera camera) {
    return run(() -> {
      PhotonPipelineResult result = camera.getLatestResult();
      if (result.hasTargets()) {
        pivotMotorLeader.set(pivotController.calculate(getPosition(), result.getBestTarget().getPitch()));
      }
    });
  }

  @Override
  public void periodic() {
    final String PREFIX = "Pivot ";
    SmartDashboard.putNumber(PREFIX+"Position", getPosition());
    SmartDashboard.putBoolean(PREFIX+"Setpoint", atSetpoint());
  }
}
