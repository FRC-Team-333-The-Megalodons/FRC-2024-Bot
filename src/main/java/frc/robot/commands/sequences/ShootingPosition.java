// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoPivot;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.commands.auto.PrepareToShoot;
import frc.robot.commands.basic.RunLEDs;
import frc.robot.commands.basic.RunShooter;
import frc.robot.commands.basic.RunLEDs.LEDRunMode;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.LEDStrip.LEDColor;

public class ShootingPosition extends ParallelCommandGroup {

  /** Creates a new ShootingPosition. */
  public ShootingPosition(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, Indexer indexer, Shooter shooter, double position, LEDStrip leds) {
    this(intake, wrist, trolley, pivot, indexer, shooter, position, leds, ShooterConstants.SPEED);
  }
  
  public ShootingPosition(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, Indexer indexer, Shooter shooter, double position, LEDStrip leds, double shooterSpeed) {
    addCommands(
      //new MarkBotState(BotState.UNKNOWN_POSITION),
      /*  new AutoWrist(wrist, WristConstants.SHOOTING_SETPOINT_POS),
      new AutoPivot(pivot, position)  .alongWith(
      new RunShooter(shooter, 0.75))
      .andThen(new RunLEDs(leds, LEDColor.GREEN))*/
      new RunLEDs(leds, LEDColor.OFF, LEDRunMode.RUN_ONCE),
      new PrepareToShoot(intake, wrist, trolley, pivot, indexer, shooter, position, leds).andThen(new RunLEDs(leds, LEDColor.GREEN)).alongWith(new RunShooter(shooter, shooterSpeed)) 
    );
  }
}
