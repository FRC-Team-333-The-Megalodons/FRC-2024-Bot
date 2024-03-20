// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.sequences.GoHome;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

public class ShootAndLeave extends SequentialCommandGroup {
  PathPlannerPath path = PathPlannerPath.fromPathFile("Leave - Center");
  /** Creates a new ShootAndLeave. */
  public ShootAndLeave(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, Indexer indexer, Shooter shooter, LEDStrip ledStrip){

    addCommands(
      new GoHome(pivot, trolley, wrist),
      new SubWooferShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, ledStrip),
      AutoBuilder.followPath(path)
    );
  }
}
