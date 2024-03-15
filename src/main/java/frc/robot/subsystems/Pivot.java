// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import org.photonvision.PhotonCamera;

import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.TrolleyConstants;

public class Pivot extends SubsystemBase {

  private CANSparkFlex pivotMotorLeader, pivotMotorFollower;
  private DutyCycleEncoder pivotEncoder;
  private PhotonCamera camera;

  private PIDController pivotController;

  private final double cameraHeightMeters = Units.inchesToMeters(0);
  private final double targetHeightMeters = Units.inchesToMeters(0);
  private final double cameraPitchRadians = Units.degreesToRadians(0);

  // How far from the target we want to be
  private final double goalRangeMeters = Units.feetToMeters(0);

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

    pivotController = new PIDController(PivotConstants.kP, PivotConstants.kI, PivotConstants.kD);

    // camera = new PhotonCamera("camera");
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

  
  public boolean fuzzyEquals(double a, double b) {
    final double epsilon = 0.001;
    return Math.abs(a-b) < epsilon;
  }

  public boolean isOkToMovePivotUp() {
    // For now, just return true here because my checks aren't working
    return true;
    /*
    if (m_trolleyRef.isTrolleyTooFarInToPivotUpPastBumper())
    {
        return getPivotPosition() > PivotConstants.PIVOT_UP_FAR_ENOUGH_THAT_TROLLEY_COULD_HIT_BACK_BUMPER;
    }
    if (m_trolleyRef.isTrolleyTooFarInToPivotVertical())
    {
        return getPivotPosition() > PivotConstants.PIVOT_UP_FAR_ENOUGH_THAT_TROLLEY_COULD_HIT_UNDERBELLY;
    }
    return true;
    */
  }

  public boolean isOkToMovePivotDown() {
    if (trolleyRef.isTrolleyOut()) {
      // If the Trolley is out, then we can only move down if we're above the "trolley can move safely" setpoint.
      return getPosition() < PivotConstants.PIVOT_FURTHEST_DOWN_WHERE_TROLLEY_CAN_MOVE;
    }
    return true;
  }

  public boolean isPivotAtMaxUp() {
    // TODO
    return false;
  }

  public boolean isPivotAtMaxDown() {
    // TODO
    return false;
  }

  public double getPosition() {
    // return (pivotEncoder.getAbsolutePosition() * -1) + 1.0;
    return pivotEncoder.getAbsolutePosition();
  }


  public void setPosition(double setpoint) {
    double speed = pivotController.calculate(getPosition(), setpoint);
    runPivot(speed);
  }

  public void trackTarget() {
    var result = camera.getLatestResult();

    if (result.hasTargets()) {
      pivotMotorLeader.set(pivotController.calculate(result.getBestTarget().getYaw(), 0));
    } else {
      stopPivot();
    }
  }

  private boolean mustStopDueToLimit(double speed) {
    return false;
    // // TODO: Is positive Up or Down? This code assumes value > 0 means "go Up", might need to be flipped if not so.
    // return ((speed > 0 && getPosition() >= getUpLimitFromState()) ||
    //         (speed < 0 && getPosition() <= getDownLimitFromState()));
  }

  // // Note: DOWN means the shooter is down, and the Intake is up.
  // private double getDownLimitFromState()
  // {
  //   if (trolleyRef.getPivotPosition() > TrolleyConstants.INTAKE_SETPOINT_POS) {
  //     // This intends to say "if the trolley position is towards the front, don't let us move the Pivot down"
  //     return PivotConstants.HOME_SETPOINT_POS; 
  //   }
  //   return PivotConstants.AMP_SETPOINT_POS;
  // }

  // // Note: UP means the shooter is up, and the Intake is down.
  // private double getUpLimitFromState()
  // {
  //   if (trolleyRef.getPosition() < TrolleyConstants.HOME_SETPOINT_POS) {
  //     // This intends to say "if the trolley position is towards the back, don't let us move the Pivot up"
  //     return PivotConstants.HOME_SETPOINT_POS;
  //   }

  //   return PivotConstants.SHOOTING_SETPOINT_POS;
  // }

  @Override
  public void periodic() {
    final String PREFIX = "Pivot ";
    SmartDashboard.putNumber(PREFIX+"Position", getPosition());
    SmartDashboard.putBoolean(PREFIX+"Setpoint", pivotController.atSetpoint());
  }
}
