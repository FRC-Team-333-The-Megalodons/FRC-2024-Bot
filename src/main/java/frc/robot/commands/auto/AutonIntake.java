// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.sequences.AutoIntake;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

public class AutonIntake extends SequentialCommandGroup {
  /** Creates a new AutonIntake. */
  public AutonIntake(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, LEDStrip leds) {
    addCommands(
       new AutoIntake(intake, wrist, trolley, pivot, leds)
      );
  }
}
