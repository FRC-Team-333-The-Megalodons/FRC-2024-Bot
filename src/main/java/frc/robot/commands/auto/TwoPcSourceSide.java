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

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TwoPcSourceSide extends SequentialCommandGroup {
  /** Creates a new TwoPcSourceSide. */
  PathPlannerPath path = PathPlannerPath.fromPathFile("Source Side Step 1");
  public TwoPcSourceSide(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, Indexer indexer, Shooter shooter, LEDStrip ledStrip){
    addCommands(
      new GoHome(pivot, trolley, wrist),
      new RunIntakeAlongWithShooter(intake, wrist, trolley, pivot, indexer, shooter, ledStrip),
      AutoBuilder.followPath(path),
      new AutonIntake(intake, wrist, trolley, pivot, ledStrip),
      new GoHome(pivot, trolley, wrist),
      new SpikeMarkShot(intake, wrist, trolley, pivot, indexer, shooter, ledStrip)

    );
  }
}
