// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.PivotConstants;
import frc.robot.commands.basic.RunIntake;
import frc.robot.commands.sequences.AutoShootingPose;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

public class SpikeMarkShot extends SequentialCommandGroup {
  /** Creates a new SpikeMarkShot. */
  public SpikeMarkShot(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, Indexer indexer, Shooter shooter, LEDStrip leds) {
    addCommands(
      new AutoShootingPose(intake, wrist, trolley, pivot, indexer,PivotConstants.AUTO_PODIUM_SETPOINT_POSE).withTimeout(2),
      new RunIntake(intake, leds, IntakeConstants.INTAKE_FIRE_SPEED).until(intake::shotTheNote)
    );
  }
}
