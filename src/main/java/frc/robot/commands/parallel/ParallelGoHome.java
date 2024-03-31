// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.parallel;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.TrolleyConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.commands.auto.AutonPivot;
import frc.robot.commands.basic.RunTrolley;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class ParallelGoHome extends ParallelCommandGroup {
  /** Creates a new ParallelGoHome. */
  public ParallelGoHome(Pivot pivot, Trolley trolley, Wrist wrist, LEDStrip leds) {
    addCommands(
      new AutonPivot(pivot, PivotConstants.HOME_SETPOINT_POS),
      new AutoWrist(wrist, WristConstants.SHOOTING_SETPOINT_POS),
      new RunTrolley(trolley, TrolleyConstants.TROLLEY_REVERSE_SPEED).until(trolley::isTrolleyAtMinInLimitSwitch)
    );
  }
}
