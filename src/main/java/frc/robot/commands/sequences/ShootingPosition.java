// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Constants.IndexerConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoIndexer;
import frc.robot.commands.advanced.AutoPivot;
import frc.robot.commands.advanced.AutoShooter;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

public class ShootingPosition extends ParallelCommandGroup {

  private Intake intake;
  private Wrist wrist;
  private Trolley trolley;
  private Pivot pivot;
  private Indexer indexer;
  private Shooter shooter;

  /** Creates a new ShootingPosition. */
  public ShootingPosition(double position) {
    addCommands(
      new AutoWrist(wrist, WristConstants.SHOOTING_SETPOINT_POS).withTimeout(0.5),
      new AutoPivot(pivot, position).withTimeout(0.5),
      new AutoShooter(shooter, ShooterConstants.SHOT_RPM).alongWith(new AutoIndexer(indexer, IndexerConstants.SHOT_RPM))
    );
  }
}
