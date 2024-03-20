// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest.FieldCentricFacingAngle;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.Constants.IndexerConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.TrolleyConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoIndexer;
import frc.robot.commands.advanced.AutoPivot;
import frc.robot.commands.advanced.AutoShooter;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.commands.auto.AutonIntake;
import frc.robot.commands.auto.EventMarkIntake;
import frc.robot.commands.auto.SpikeMarkShot;
import frc.robot.commands.auto.SubWooferShootingPosition;
import frc.robot.commands.basic.RunIntake;
import frc.robot.commands.basic.RunPivot;
import frc.robot.commands.basic.RunTrolley;
import frc.robot.commands.basic.RunWrist;
import frc.robot.commands.sequences.AutoAmp;
import frc.robot.commands.sequences.AutoIntake;
import frc.robot.commands.sequences.GoHome;
import frc.robot.commands.sequences.ShootingPosition;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;

public class RobotContainer {
  private double MaxSpeed = TunerConstants.kSpeedAt12VoltsMps; // kSpeedAt12VoltsMps desired top speed
  private double MaxAngularRate = 1.5 * Math.PI; // 3/4 of a rotation per second max angular velocity

  /* Setting up bindings for necessary control of the swerve drive platform */
  private final CommandPS5Controller driverController = new CommandPS5Controller(0);
  private final CommandPS5Controller operatorController = new CommandPS5Controller(1);
  private final CommandSwerveDrivetrain drivetrain = TunerConstants.DriveTrain;
  private final SendableChooser<Command> autoChooser;

  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // I want field-centric
                                                               // driving in open loop
  private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
  private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();
  private final SwerveRequest.FieldCentricFacingAngle angle = new FieldCentricFacingAngle();
  private final SwerveRequest.RobotCentric forwardStraight = new SwerveRequest.RobotCentric().withDriveRequestType(DriveRequestType.OpenLoopVoltage);
  private final Telemetry logger = new Telemetry(MaxSpeed);

  private final Intake intake = new Intake();
  private final Trolley trolley = new Trolley();
  private final Pivot pivot = new Pivot();
  private final Wrist wrist = new Wrist();
  private final Indexer indexer = new Indexer();
  private final Shooter shooter = new Shooter();
  private final LEDStrip leds = new LEDStrip();

  private final boolean manualMode = false;

  private void configureBindings() {

    /* Drivetrain */ 
    drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
        drivetrain.applyRequest(() -> drive.withVelocityX(-driverController.getLeftY() * MaxSpeed) // Drive forward with
                                                                                           // negative Y (forward)
            .withVelocityY(-driverController.getLeftX() * MaxSpeed) // Drive left with negative X (left)
            .withRotationalRate(-driverController.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
        ));

    driverController.cross().whileTrue(drivetrain.applyRequest(() -> brake));
    driverController.circle().whileTrue(drivetrain
        .applyRequest(() -> point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))));

    // reset the field-centric heading on left bumper press
    driverController.L1().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldRelative()));

    // TODO: test snap-to functionality
    driverController.povUp().whileTrue(drivetrain.applyRequest(() -> forwardStraight.withVelocityX(0.5).withVelocityY(0)));
    driverController.povDown().whileTrue(drivetrain.applyRequest(() -> forwardStraight.withVelocityX(-0.5).withVelocityY(0)));
    driverController.povDownLeft().whileTrue(drivetrain.applyRequest(() -> forwardStraight.withVelocityX(0).withVelocityY(0.5)));
    driverController.povRight().whileTrue(drivetrain.applyRequest(() -> forwardStraight.withVelocityX(0).withVelocityY(-0.5)));

    if (Utils.isSimulation()) {
      drivetrain.seedFieldRelative(new Pose2d(new Translation2d(), Rotation2d.fromDegrees(90)));
    }
    drivetrain.registerTelemetry(logger::telemeterize);

    /* Robot Mech */ 
    if (manualMode) {

      operatorController.circle().whileTrue((new RunIntake(intake, leds, IntakeConstants.INTAKE_SPEED).until(intake::hasNote)).andThen(new RunCommand(() -> leds.blinkGreen(), leds)));
      operatorController.cross().whileTrue(new RunIntake(intake, leds, IntakeConstants.INTAKE_FIRE_SPEED));
      operatorController.triangle().whileTrue(new RunIntake(intake, leds, IntakeConstants.INTAKE_EJECT_SPEED));

      operatorController.touchpad().whileTrue(new RunCommand(() -> leds.blinkBlue(), leds));

      operatorController.L1().whileTrue(new RunWrist(wrist, 0.5));
      operatorController.R1().whileTrue(new RunWrist(wrist, -0.5));
      operatorController.L3().whileTrue(new AutoWrist(wrist, WristConstants.INTAKE_SETPOINT_POS));
      operatorController.R3().whileTrue(new AutoWrist(wrist, WristConstants.SHOOTING_SETPOINT_POS));

      operatorController.povUp().whileTrue(new RunTrolley(trolley, TrolleyConstants.TROLLEY_FORWARD_SPEED).until(trolley::isTrolleyAtMaxOutLimitSwitch));
      operatorController.povDown().whileTrue(new RunTrolley(trolley, TrolleyConstants.TROLLEY_REVERSE_SPEED).until(trolley::isTrolleyAtMinInLimitSwitch));

      operatorController.R2().whileTrue(new RunPivot(pivot, PivotConstants.PIVOT_SPEED));
      operatorController.L2().whileTrue(new RunPivot(pivot, -PivotConstants.PIVOT_SPEED));
      operatorController.create().whileTrue(new AutoPivot(pivot, PivotConstants.HOME_SETPOINT_POS));
      operatorController.options().whileTrue(new AutoPivot(pivot, PivotConstants.SUBWOFFER_SETPOINT_POS));

      // operatorController.square().whileTrue(new RunShooter(shooter, 0.9).alongWith(new RunIndexer(indexer, 1.0)));
      operatorController.square().whileTrue(new AutoShooter(shooter, ShooterConstants.SHOT_RPM).alongWith(new AutoIndexer(indexer, IndexerConstants.SHOT_RPM)));

      operatorController.povLeft().whileTrue(new GoHome(pivot, trolley, wrist));
      operatorController.povRight().whileTrue(new AutoIntake(intake, wrist, trolley, pivot, leds));
      operatorController.PS().whileTrue(new AutoAmp(intake, wrist, trolley, pivot));

    } else {

      operatorController.L2().whileTrue(new GoHome(pivot, trolley, wrist));

      operatorController.R1().whileTrue(new AutoIntake(intake, wrist, trolley, pivot, leds));
      
      operatorController.L1().whileTrue(new RunIntake(intake, leds, -IntakeConstants.INTAKE_SPEED).alongWith(new RunCommand(() -> leds.blinkGreen(), leds))); // eject
      operatorController.R2().whileTrue(new RunIntake(intake, leds, IntakeConstants.INTAKE_SPEED).alongWith(new RunCommand(() -> leds.blinkRed(), leds))); // intake

      operatorController.povUp().whileTrue(new RunTrolley(trolley, TrolleyConstants.TROLLEY_FORWARD_SPEED).until(trolley::isTrolleyAtMaxOutLimitSwitch)); // trolley out
      operatorController.povDown().whileTrue(new RunTrolley(trolley, TrolleyConstants.TROLLEY_REVERSE_SPEED).until(trolley::isTrolleyAtMinInLimitSwitch)); // trolley in

      operatorController.triangle().whileTrue(new ShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, PivotConstants.SUBWOFFER_SETPOINT_POS));
      operatorController.square().whileTrue(new ShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, PivotConstants.PODIUM_SETPOINT_POS));
      operatorController.circle().whileTrue(new ShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, PivotConstants.HOME_SETPOINT_POS));
      
      operatorController.cross().whileTrue(new AutoAmp(intake, wrist, trolley, pivot));
    }
  }







  public class ManualModeEvaluator implements BooleanSupplier {
    public ManualModeEvaluator() {}
    public boolean getAsBoolean() {
        return SmartDashboard.getBoolean(MANUAL_MODE_KEY, true);
    }
  }
  
  final String MANUAL_MODE_KEY = "MANUAL_MODE";

  boolean lastMicButtonState = false;

  public void toggleManualModeWhenButtonPressed() {
    if (operatorController.getHID().getRawButtonPressed(15)) {
      boolean before = SmartDashboard.getBoolean(MANUAL_MODE_KEY, true);
      boolean after = !before;
      System.out.println("TOGGLE MANUAL MODE from "+before+" to "+after+".");
      SmartDashboard.putBoolean(MANUAL_MODE_KEY, after);
      if (after) {
        leds.violet();
      } else {
        leds.yellow();
      }
    }
  }

  ManualModeEvaluator manualModeEvaluator = new ManualModeEvaluator(); // We only need one instance of this class.

  public Command smartOrManual(Command smart, Command manual) {
    return new ConditionalCommand(manual, smart, manualModeEvaluator);
  }

  public Command NullCommand() {
    return new WaitCommand(0);
  }

  public void registerButtons() {
    // operatorController.circle().whileTrue(new RunIntake(intake, 0.5).until(intake::hasNote));
    operatorController.circle().whileTrue(smartOrManual(
      /* SMART */ NullCommand(), /*TODO - Implement Circle for Smart Mode */
      /* MANUAL */  ((new RunIntake(intake, leds, IntakeConstants.INTAKE_SPEED).alongWith(new RunCommand(() -> leds.red(), leds))).
                            until(intake::hasNote)).andThen(new RunCommand(() -> leds.blinkGreen(), leds).repeatedly().withTimeout(1))
    ));

    operatorController.cross().whileTrue(smartOrManual(
      /* SMART */  new AutoAmp(intake, wrist, trolley, pivot),
      /* MANUAL */  new RunIntake(intake, leds, 0.75)
    ));

    operatorController.triangle().whileTrue(smartOrManual(
        /* SMART */ new ShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, PivotConstants.SUBWOFFER_SETPOINT_POS),
      /* MANUAL */ new RunIntake(intake, leds, -IntakeConstants.INTAKE_SPEED)
    ));

    // operatorController.square().whileTrue(new RunShooter(shooter, 0.9).alongWith(new RunIndexer(indexer, 1.0)));
    operatorController.square().whileTrue(smartOrManual(
        /* SMART */ new ShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, PivotConstants.PODIUM_SETPOINT_POS),
      /* MANUAL */ new AutoShooter(shooter, ShooterConstants.SHOT_RPM).alongWith(new AutoIndexer(indexer, IndexerConstants.SHOT_RPM))
    ));
    // operatorController.square().whileTrue(new AutoShooter(shooter, ShooterConstants.SHOT_RPM));
    // operatorController.square().whileTrue(new AutoIndexer(indexer, IndexerConstants.SHOT_RPM));)

    operatorController.touchpad().onTrue(smartOrManual(
        /* SMART */ new RunCommand(() -> leds.blinkOrange(), leds).repeatedly().withTimeout(2.0), // TODO: Do we need repeatedly here?
      /* MANUAL */ new RunCommand(() -> leds.blinkViolet(), leds).repeatedly().withTimeout(2.0)
    ));

    operatorController.L1().whileTrue(smartOrManual(
        /* SMART */ new RunIntake(intake, leds, -IntakeConstants.INTAKE_SPEED), // Eject
      /* MANUAL */ new RunWrist(wrist, -0.2)
    ));

    operatorController.R1().whileTrue(smartOrManual(
        /* SMART */ new AutoIntake(intake, wrist, trolley, pivot, leds),
      /* MANUAL */ new RunWrist(wrist, 0.2)
    ));

    operatorController.L3().whileTrue(smartOrManual(
        /* SMART */ NullCommand(),
      /* MANUAL */ new AutoWrist(wrist, WristConstants.INTAKE_SETPOINT_POS)
    ));

    operatorController.R3().whileTrue(smartOrManual(
        /* SMART */ NullCommand(),
      /* MANUAL */ new AutoWrist(wrist, WristConstants.SHOOTING_SETPOINT_POS)
    ));

    operatorController.povUp().whileTrue(smartOrManual(
        /* SMART */ new RunTrolley(trolley, TrolleyConstants.TROLLEY_FORWARD_SPEED).until(trolley::isTrolleyAtMaxOutLimitSwitch), // trolley out,
      /* MANUAL */ new RunTrolley(trolley, TrolleyConstants.TROLLEY_FORWARD_SPEED).until(trolley::isTrolleyAtMaxOutLimitSwitch)
    ));

    operatorController.povDown().whileTrue(smartOrManual(
        /* SMART */ new RunTrolley(trolley, TrolleyConstants.TROLLEY_REVERSE_SPEED).until(trolley::isTrolleyAtMinInLimitSwitch), // trolley in,
      /* MANUAL */ new RunTrolley(trolley, TrolleyConstants.TROLLEY_REVERSE_SPEED).until(trolley::isTrolleyAtMinInLimitSwitch)
    ));

    operatorController.R2().whileTrue(smartOrManual(
        /* SMART */ new RunIntake(intake, leds, IntakeConstants.INTAKE_SPEED), // Intake
      /* MANUAL */ new RunPivot(pivot, PivotConstants.PIVOT_SPEED)
    ));

    operatorController.L2().whileTrue(smartOrManual(
        /* SMART */ new GoHome(pivot, trolley, wrist),
      /* MANUAL */ new RunPivot(pivot, -PivotConstants.PIVOT_SPEED)
    ));

    operatorController.create().whileTrue(smartOrManual(
        /* SMART */ NullCommand(),
      /* MANUAL */ new AutoPivot(pivot, PivotConstants.HOME_SETPOINT_POS)
    ));

    operatorController.options().whileTrue(smartOrManual(
        /* SMART */ NullCommand(),
      /* MANUAL */ new AutoPivot(pivot, PivotConstants.SUBWOFFER_SETPOINT_POS)
    ));

    operatorController.povLeft().whileTrue(smartOrManual(
        /* SMART */ NullCommand(),
      /* MANUAL */ new GoHome(pivot, trolley, wrist)
    ));

    operatorController.povRight().whileTrue(smartOrManual(
        /* SMART */ NullCommand(),
      /* MANUAL */ new AutoIntake(intake, wrist, trolley, pivot, leds)
    ));

    operatorController.PS().whileTrue(smartOrManual(
        /* SMART */ NullCommand(),
      /* MANUAL */ new AutoAmp(intake, wrist, trolley, pivot)
    ));
  }

  public RobotContainer() {
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);

    trolley.setPivotRef(pivot);
    trolley.setWristRef(wrist);
    wrist.setPivotRef(pivot);
    wrist.setTrolleyRef(trolley);
    pivot.setTrolleyRef(trolley);
    pivot.setWristRef(wrist);

    NamedCommands.registerCommand("SubWooferShot",new SubWooferShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, leds) );
    NamedCommands.registerCommand("GoHome", new GoHome(pivot, trolley, wrist));
    NamedCommands.registerCommand("AutonIntake", new AutonIntake(intake, wrist, trolley, pivot, leds));
    NamedCommands.registerCommand("SpikeMarkShot", new SpikeMarkShot(intake, wrist, trolley, pivot, indexer, shooter, leds));
    NamedCommands.registerCommand("EventMarkIntake", new EventMarkIntake( wrist, trolley, pivot));
    configureBindings();

    // Default to non-manual mode (i.e. false)
    SmartDashboard.putBoolean(MANUAL_MODE_KEY, false);
  }

  SendableChooser<Boolean> manualModeToggle = new SendableChooser<>();

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
