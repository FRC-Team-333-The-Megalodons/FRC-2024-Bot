// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.advanced;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Trolley;

public class AutoTrolley extends Command {

  private final Trolley trolley;
  private final double value;

  /** Creates a new AutoTrolley. */
  public AutoTrolley(Trolley trolley, double value) {
    this.trolley = trolley;
    this.value = value;
    addRequirements(trolley);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    trolley.setPosition(value);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    trolley.stopTrolley();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}