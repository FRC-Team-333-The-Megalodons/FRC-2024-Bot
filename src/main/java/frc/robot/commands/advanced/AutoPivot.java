// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.advanced;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Pivot;

public class AutoPivot extends Command {

  private final Pivot pivot;
  private final double value;

  /** Creates a new AutoPivot. */
  public AutoPivot(Pivot pivot, double value) {
    this.pivot = pivot;
    this.value = value;
    addRequirements(pivot);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    pivot.setPosition(value);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    pivot.stopPivot();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
