// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

public final class Constants {

  public static final class IntakeConstants
  {
    public static final int MOTOR_ID = 3;
    public static final int LEFT_SENSOR_ID = 0;
    public static final int RIGHT_SENSOR_ID = 1;
    public static final double INTAKE_SPEED = 0.5;
    public static final double INTAKE_EJECT_SPEED = -0.5;
    public static final double INTAKE_FIRE_SPEED = 0.75;
    public static final double NOTE_SETPOINT = 0.0;
    public static final double kP = 5.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    public static final double MIN_INPUT = -1.0;
    public static final double MAX_INPUT = 1.0;
  }

  public static final class WristConstants {
    public static final int MOTOR_ID = 4;
    public static final double ZERO_OFFSET = 0.55;
    public static final double INTAKE_SETPOINT_POS = 0.68;
    public static final double SHOOTING_SETPOINT_POS = 0.441275894641876;
    public static final double AMP_SETPOINT_POS = 0.803184986114502;
    public static final double kP = 6.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    public static final double MIN_INPUT = -1.0;
    public static final double MAX_INPUT = 1.0;
    public static final double WRIST_DOWN_THRESHOLD = 0.62;
    // TODO: Measure on Real Robot for below 3 values!
    public static final double WRIST_MIN_DOWN = 0.14;
    public static final double WRIST_MAX_UP = 0.82;
    public static final double WRIST_FURTHEST_DOWN_WHERE_TROLLEY_CAN_MOVE_FREELY = 0.46;
  }

  public static final class TrolleyConstants {
    public static final int TROLLEY_MOTOR_ID = 5;
    public static final int TROLLEY_OUT_LIMIT_SWITCH_ID = 7; // digital sensor port
    public static final int TROLLEY_IN_LIMIT_SWITCH_ID = 2; // digital sensor port
    public static final int TROLLEY_POTENTIOMETER_ID = 0; // analog sensor port
    public static final double TROLLEY_FORWARD_SPEED = -1.0;
    public static final double TROLLEY_REVERSE_SPEED = 1.0;
    public static final double HOME_SETPOINT_POS = 0.0;
    
    public static final double kP = 0.5;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    public static final double MIN_INPUT = -0.3;
    public static final double MAX_INPUT = 0.3;

    // TODO: Redo numbers for Trolley based on Real Robot
    public static final double INTAKE_SETPOINT_POS = 107.47097778320312;
    public static final double AMP_SETPOINT_POS = 69;
    public static final double WRIST_POS_LOWER_LIMIT_WHILE_TROLLEY_DOWN = 0.7024;
    public static final double TROLLEY_POS_LOWEST_POINT_WRIST_CAN_MOVE = 4.185;                                               
    public static final double TROLLEY_IN_OUT_THRESHOLD = 1.16; // Can use this to decide if it's in or out
    public static final double TROLLEY_FURTHEST_IN_WHERE_PIVOT_CAN_MOVE_ALL_THE_WAY_UP = 1.33;
    // have updated as of 3/16/14
    public static final double TROLLEY_MIN_IN = 0.325;
    public static final double TROLLEY_MAX_OUT = 0.761;
    
    public static final double TROLLEY_FURTHEST_IN_WHERE_PIVOT_CAN_CLEAR_BACK_BUMPER_AND_MOVE_ALL_THE_WAY_UP = 0.45;
    
  }

  public static final class PivotConstants { 
    public static final int MOTOR1_ID = 6; 
    public static final int MOTOR2_ID = 7; 
    public static final int PIVOT_ENCODER_ID = 9; 
    public static final double PIVOT_SPEED = 0.2;
    public static final double ZERO_OFFSET = 0.0;
    public static final double HOME_SETPOINT_POS = 0.214587305364683;
    public static final double INTAKE_SETPOINT_POS = 0.15122307873;
    public static final double SUBWOFFER_SETPOINT_POS = 0.058;
    public static final double AUTO_SUBWOFFER_SETPOINT_POS = 0.052;
    public static final double PODIUM_SETPOINT_POS = 0.120947403023685;
    public static final double WING_SETPOINT_POS = 0.0;
    public static final double AMP_SETPOINT_POS = 0.44;
    public static final double kP = 4.1;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    public static final double MIN_INPUT = -0.3;
    public static final double MAX_INPUT = 0.3;
    public static final double PIVOT_MIN_DOWN = 0.05; // This is the lowest point the intake can be down. Just past the subwoofer.
    public static final double PIVOT_MAX_UP = 0.46; // This is also the Amp scoring position.
    
    public static final double PIVOT_UP_FAR_ENOUGH_THAT_TROLLEY_COULD_HIT_BACK_BUMPER = 0.235;
    // TODO: Redo these numbers with real robot
    public static final double PIVOT_FURTHEST_DOWN_WHERE_TROLLEY_CAN_MOVE = 0.149223078730577;
    //public static final double PIVOT_UP_FAR_ENOUGH_THAT_TROLLEY_COULD_HIT_UNDERBELLY = 0.2;
  }

  public static final class IndexerConstants {
    public static final int MOTOR_ID = 15;
    public static final double SPEED = 0.9;
    public static final double SHOT_RPM = 6000;
    public static final double IDLE_RPM = 1000;
    public static final double kP = 0.0004;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    public static final double MIN_INPUT = -1;
    public static final double MAX_INPUT = 1;
  }

  public static final class ShooterConstants {
    public static final int TOP_MOTOR_ID = 8;
    public static final double SPEED = 0.9;
    public static final int BOTTOM_MOTOR_ID = 9;
    public static final int INDEX_MOTOR_ID = 15;
    public static final double SHOT_RPM = 6000;
    public static final double IDLE_RPM = 1000;
    public static final double kP = 0.0004;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    public static final double MIN_INPUT = -1;
    public static final double MAX_INPUT = 1;
  }
}
