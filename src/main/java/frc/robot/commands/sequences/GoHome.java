// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.TrolleyConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoPivot;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.commands.basic.RunLEDs;
import frc.robot.commands.basic.RunTrolley;
import frc.robot.commands.basic.RunLEDs.LEDRunMode;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.LEDStrip.LEDColor;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

public class GoHome extends SequentialCommandGroup {
  /** Creates a new GoHome. */
  public GoHome(Pivot pivot, Trolley trolley, Wrist wrist, LEDStrip leds) {
    addCommands(
      new RunLEDs(leds, LEDColor.OFF, LEDRunMode.RUN_ONCE),
      new AutoPivot(pivot, PivotConstants.HOME_SETPOINT_POS)
      .alongWith(
      new AutoWrist(wrist, WristConstants.SHOOTING_SETPOINT_POS)),
      new RunTrolley(trolley, TrolleyConstants.TROLLEY_REVERSE_SPEED).until(trolley::isTrolleyAtMinInLimitSwitch)
      .andThen(new RunLEDs(leds, LEDColor.BLUE, LEDRunMode.RUN_PAST_INTERRUPT))
    );
  }
}
