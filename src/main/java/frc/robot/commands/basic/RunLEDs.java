// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.basic;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.LEDStrip.LEDColor;

public class RunLEDs extends Command {

  public enum LEDRunMode {
    RUN_ONCE,
    RUN_UNTIL_INTERRUPT,
    RUN_PAST_INTERRUPT
  }

  private final LEDStrip leds;
  private final LEDColor color;
  private final LEDRunMode mode;

  /** Creates a new RunShooter. */
  public RunLEDs(LEDStrip leds, LEDColor color) {
    this.leds = leds;
    this.color = color;
    this.mode = LEDRunMode.RUN_UNTIL_INTERRUPT;
  }

  public RunLEDs(LEDStrip leds, LEDColor color, LEDRunMode mode) {
    this.leds = leds;
    this.color = color;
    this.mode = mode;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    leds.setColor(color);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    if (mode != LEDRunMode.RUN_PAST_INTERRUPT) {
      leds.off();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (mode == LEDRunMode.RUN_ONCE);
  }
}