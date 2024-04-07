// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.sequences;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.auto.PrepareToShoot;
import frc.robot.commands.basic.RunLEDs;
import frc.robot.commands.basic.RunLEDs.LEDRunMode;
import frc.robot.commands.basic.RunShooter;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.LEDStrip.LEDColor;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class TrapShooter extends SequentialCommandGroup {
  /** Creates a new TrapShooter. */
  public TrapShooter(Intake intake, Wrist wrist, Trolley trolley, Pivot pivot, Indexer indexer, Shooter shooter, double position, LEDStrip leds) {
    addCommands(
      //new MarkBotState(BotState.UNKNOWN_POSITION),
      /*  new AutoWrist(wrist, WristConstants.SHOOTING_SETPOINT_POS),
      new AutoPivot(pivot, position)  .alongWith(
      new RunShooter(shooter, 0.75))
      .andThen(new RunLEDs(leds, LEDColor.GREEN))*/
      new RunLEDs(leds, LEDColor.OFF, LEDRunMode.RUN_ONCE),
      new PrepareToShoot(intake, wrist, trolley, pivot, indexer, shooter, position, leds).andThen(new RunLEDs(leds, LEDColor.GREEN)).alongWith(new RunShooter(shooter, ShooterConstants.TRAP_SHOOTER_SPEED)) 
    );
  }
}
