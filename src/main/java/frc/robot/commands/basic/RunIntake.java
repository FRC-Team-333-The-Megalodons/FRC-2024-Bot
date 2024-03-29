// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.basic;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;

public class RunIntake extends Command {
  private final Intake intake;
  private final LEDStrip leds;
  
  private final double value;

  /** Creates a new IntakeNote. */
  public RunIntake(Intake intake, LEDStrip leds, double value) {
    this.intake = intake;
    this.value = value;
    this.leds = leds;
    addRequirements(intake);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    intake.resetEncoder();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    intake.runIntake(value);
    if (value < 0) {
      leds.blue();
    } else {
      leds.red();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    intake.stopIntake();
    leds.off();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}

