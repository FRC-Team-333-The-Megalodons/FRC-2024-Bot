// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.TrolleyConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoPivot;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.commands.basic.RunLEDs;
import frc.robot.commands.basic.RunLEDs.LEDRunMode;
import frc.robot.commands.basic.RunTrolley;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.LEDStrip.LEDColor;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

public class AutoAmp extends SequentialCommandGroup {
  /** Creates a new AutoAmp. */
  public AutoAmp(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, LEDStrip leds) {
    addCommands(
      //new MarkBotState(BotState.UNKNOWN_POSITION),
      new RunLEDs(leds, LEDColor.OFF, LEDRunMode.RUN_ONCE),
      new RunTrolley(trolley, TrolleyConstants.TROLLEY_FORWARD_SPEED).until(trolley::isTrolleyAtMaxOutLimitSwitch),
      new AutoPivot(pivot, PivotConstants.AMP_SETPOINT_POS),
      new AutoWrist(wrist, WristConstants.AMP_SETPOINT_POS)
      //,new MarkBotState(BotState.AMP_POSITION)
    );
  }
}
