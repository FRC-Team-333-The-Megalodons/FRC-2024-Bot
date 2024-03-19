// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoPivot;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.commands.basic.RunIndexer;
import frc.robot.commands.basic.RunIntake;
import frc.robot.commands.basic.RunShooter;
import frc.robot.commands.sequences.ShootingPosition;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class SubWooferShootingPosition extends SequentialCommandGroup {
  /** Creates a new SubWooferShootingPosition. */
  public SubWooferShootingPosition(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, Indexer indexer, Shooter shooter, LEDStrip leds) {
    addCommands(
      new ShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, PivotConstants.AUTO_SUBWOFFER_SETPOINT_POS).withTimeout(3),
      new RunIntake(intake, leds, IntakeConstants.INTAKE_FIRE_SPEED).withTimeout(0.5)
    );
  }
}
