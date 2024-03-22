// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.BotState;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.TrolleyConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoPivot;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.commands.basic.MarkBotState;
import frc.robot.commands.basic.RunIntake;
import frc.robot.commands.basic.RunTrolley;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

public class SourceIntake extends SequentialCommandGroup {
  /** Creates a new SourceIntake. */
  public SourceIntake(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, LEDStrip leds) {
    addCommands(
      //new MarkBotState(BotState.UNKNOWN_POSITION),
      new RunTrolley(trolley, TrolleyConstants.TROLLEY_FORWARD_SPEED).until(trolley::isTrolleyAtMaxOutLimitSwitch),
      new AutoPivot(pivot, PivotConstants.SOURCE_SETPOINT_POS).withTimeout(1.5),
      new AutoWrist(wrist, WristConstants.SOURCE_SETPOINT_POS).withTimeout(0.5),
      //new MarkBotState(BotState.SOURCE_INTAKE_POSITION),
      new RunIntake(intake, leds, 0.25).until(intake::hasNote),
      new RunCommand(() -> leds.blinkGreen(), leds).repeatedly().withTimeout(3.33)
    );
  }
}
