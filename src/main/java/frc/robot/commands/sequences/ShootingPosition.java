// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoPivot;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.commands.basic.RunShooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

public class ShootingPosition extends ParallelCommandGroup {

  /** Creates a new ShootingPosition. */
  public ShootingPosition(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, Indexer indexer, Shooter shooter, double position) {
    addCommands(
      //new MarkBotState(BotState.UNKNOWN_POSITION),
      new AutoWrist(wrist, WristConstants.SHOOTING_SETPOINT_POS),
      new AutoPivot(pivot, position).alongWith(
      new RunShooter(shooter, 0.75))
    );
  }
}
