// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.TrolleyConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoPivot;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.commands.basic.RunTrolley;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

public class EventMarkIntake extends SequentialCommandGroup {
  /** Creates a new EventMarkIntake. */
  public EventMarkIntake(Wrist wrist, Trolley trolley, Pivot pivot) {
    addCommands(
      new RunTrolley(trolley, TrolleyConstants.TROLLEY_FORWARD_SPEED).until(trolley::isTrolleyAtMaxOutLimitSwitch),
      new AutoWrist(wrist, WristConstants.INTAKE_SETPOINT_POS),
      new AutonPivot(pivot, PivotConstants.INTAKE_SETPOINT_POS)
    );
  }
}
