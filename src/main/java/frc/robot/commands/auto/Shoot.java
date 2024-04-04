// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.basic.RunAutoShooter;
import frc.robot.commands.basic.RunIndexer;
import frc.robot.commands.basic.RunShooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Shooter;

public class Shoot extends SequentialCommandGroup {
  /** Creates a new Shoot. */
  public Shoot(Shooter shooter, Indexer indexer) {
    addCommands(
      new RunAutoShooter(shooter, 0.75).alongWith(new RunIndexer(indexer, 0.75))
    );
  }
}
