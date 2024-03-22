// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.basic;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.BotState;
import frc.robot.Constants.GlobalState;

public class MarkBotState extends Command {

  private BotState state;
  /** Creates a new RunShooter. */
  public MarkBotState(BotState _state) {
    state = _state;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {    
    GlobalState.setRobotState(state);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    GlobalState.setRobotState(state);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    GlobalState.setRobotState(state);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true; // GlobalState.getRobotState() == state;
  }
}