// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public final class Constants {

  public static final class IntakeConstants {
    public static final int MOTOR_ID = 3;
    public static final int LEFT_SENSOR_ID = 0;
    public static final int RIGHT_SENSOR_ID = 1;
    public static final double INTAKE_SPEED = 0.4;
    public static final double INTAKE_FLOOR_SPEED_EXTRA = 0.05;
    public static final double INTAKE_SOURCE_SPEED = 0.25;
    public static final double INTAKE_SOURCE_SPEED_EXTRA = 0.1;
    public static final double INTAKE_EJECT_SPEED = -0.4;
    public static final double INTAKE_FIRE_SPEED =  0.375;
    public static final double NOTE_SETPOINT = 0.0;
    public static final double kP = 5.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    public static final double MIN_INPUT = -1.0;
    public static final double MAX_INPUT = 1.0;
    public static final double INTAKE_HAS_NOTE_EXTRA_S = 0.06;
    public static final double INTAKE_SOURCE_HAS_NOTE_EXTRA_S = 0.12;
  }

  public static final class WristConstants {
    public static final int MOTOR_ID = 4;
    public static final double ZERO_OFFSET = 0.55;
    public static final double INTAKE_SETPOINT_POS = 0.639;
    public static final double SHOOTING_SETPOINT_POS = 0.4762;
    public static final double AMP_SETPOINT_POS = 0.803184986114502;
    public static final double SOURCE_SETPOINT_POS = 0.637242329597473;
    public static final double kP = 6.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    public static final double kTolerance = 0.02;
    public static final double MIN_INPUT = -1.0;
    public static final double MAX_INPUT = 1.0;
    public static final double WRIST_DOWN_THRESHOLD = 0.62;
    public static final double WRIST_MIN_DOWN = 0.14;
    public static final double WRIST_MAX_UP = 0.82;
    public static final double WRIST_FURTHEST_DOWN_WHERE_TROLLEY_CAN_MOVE_FREELY = 0.46;
    public static final double WRIST_CLIMBING_POS = 0.433;
    
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
    public static final double TROLLEY_MIN_IN = 0.546;
    public static final double TROLLEY_MAX_OUT = 1.001;
    
    public static final double TROLLEY_FURTHEST_IN_WHERE_PIVOT_CAN_CLEAR_BACK_BUMPER_AND_MOVE_ALL_THE_WAY_UP = 0.925; // Delta to min is (0.585-0.325 = 0.26)
    public static final double TROLLEY_FURTHEST_IN_FOR_CLIMBING = 0.795;  // Delta to min is (0.45-0.325 = 0.125)
    public static final double TROLLEY_NEO_DISTANCE_FROM_MAX_TO_CLIMB = 50.5;
    public static final double TROLLEY_NEO_DISTANCE_FROM_MAX_TO_BUMPERCLEAR = 28.25;
    public static final double TROLLEY_NEO_MIN_POSITION_RESET_VALUE = 110;
  }

  public static final class PivotConstants { 
    //0.03685825
    public static final int MOTOR1_ID = 6; 
    public static final int MOTOR2_ID = 7; 
    public static final int PIVOT_ENCODER_ID = 6; 
    public static final double PIVOT_SPEED = 0.2;

    public static final double PIVOT_ENCODER_ROLLOVER = 0.15;

    public static final double ZERO_OFFSET = 0.053656; // 0.097498702437468
    public static final double HOME_SETPOINT_POS = 0.192314154807854;
    public static final double INTAKE_SETPOINT_POS = 0.143458553586464;
    public static final double INTAKE_WITH_BUFFER_SETPOINT_POS = 0.16;
    public static final double SUBWOFFER_SETPOINT_POS = 0.050 ;
    public static final double AUTO_SUBWOFFER_SETPOINT_POS = 0.056;
    public static final double PODIUM_SETPOINT_POS = 0.103;
    public static final double AUTO_PODIUM_SETPOINT_POSE = 0.09273627;
    public static final double TRUSS_SETPOINT_POS =0.1247;
    public static final double AMP_SETPOINT_POS = 0.44;
    public static final double SOURCE_SETPOINT_POS = 0.396183559904589;
    public static final double PIVOT_MIN_DOWN = 0.052; // This is the lowest point the intake can be down. Just past the subwoofer.
    public static final double PIVOT_MAX_UP = 0.465; // This is also the Amp scoring position.
    public static final double PIVOT_CLIMB_POS = 0.465;
    public static final double PIVOT_UP_FAR_ENOUGH_THAT_TROLLEY_COULD_HIT_BACK_BUMPER = 0.235;
    public static final double PIVOT_FURTHEST_DOWN_WHERE_TROLLEY_CAN_MOVE = 0.143458553586464;

    public static final String PODIUM_SETPOINT_DASHBOARD_KEY = "PODIUM_SETPOINT";
    public static final String SUBWOFFER_SETPOINT_DASHBOARD_KEY = "SUBWOFFER_SETPOINT";

    public static final double kP = 4.2;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
    public static final double kFF = 0.0;
    public static final double kAutoP = 3.9;//4.2
    public static final double kAutoI = 0.0;
    public static final double kAutoD = 0.0;
    public static final double kAutoFF = 0.0;
    public static final double kTolerance = 0.004;//0.004
    public static final double kAutoTolerance = 0.0059;

    public static final double MIN_INPUT = -0.3;
    public static final double MAX_INPUT = 0.3;
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
    public static final int RIGHT_MOTOR_ID = 8;
    public static final double SPEED = 1.0;
     public static final double TRAP_SHOOTER_SPEED = 0.5;
    public static final int LEFT_MOTOR_ID = 9;
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

  public static final class GlobalState {
    public static  final String MANUAL_MODE_KEY = "MANUAL_MODE";

    private static BotState ROBOT_STATE = BotState.UNKNOWN_POSITION;
    
    public static boolean isManualMode() {
      return SmartDashboard.getBoolean(MANUAL_MODE_KEY, true);
    }

    public static void setRobotState(BotState state) {
      if (ROBOT_STATE == state) { return; }

      // If we're entering the Shooter Position, we need to switch out our Intake Button Mapping
      //  to do the new intake sequence instead. 
      ROBOT_STATE = state;
    }

    public static BotState getRobotState()
    {
      return ROBOT_STATE;
    }
  }

  public enum BotState {
    UNKNOWN_POSITION,
    HOME_POSITION,
    SHOOTER_POSITION,
    FLOOR_INTAKE_POSITION, 
    SOURCE_INTAKE_POSITION,
    AMP_POSITION
  }

}