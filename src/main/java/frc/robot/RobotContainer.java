
// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import org.photonvision.PhotonCamera;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest;
import com.ctre.phoenix6.mechanisms.swerve.SwerveRequest.FieldCentricFacingAngle;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.ctre.phoenix6.mechanisms.swerve.SwerveModule.DriveRequestType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS5Controller;
import frc.robot.Constants.IndexerConstants;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.GlobalState;
import frc.robot.Constants.PivotConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.TrolleyConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.commands.advanced.AutoIndexer;
import frc.robot.commands.advanced.AutoPivot;
import frc.robot.commands.advanced.AutoShooter;
import frc.robot.commands.advanced.AutoWrist;
import frc.robot.commands.auto.AutoGoHome;
import frc.robot.commands.auto.AutonIntake;
import frc.robot.commands.auto.EventMarkIntake;
import frc.robot.commands.auto.Shoot;
import frc.robot.commands.auto.SpikeMarkShot;
import frc.robot.commands.auto.SubWooferShootingPosition;
import frc.robot.commands.basic.RunIndexer;
import frc.robot.commands.basic.RunIntake;
import frc.robot.commands.basic.RunLEDs;
import frc.robot.commands.basic.RunPivot;
import frc.robot.commands.basic.RunShooter;
import frc.robot.commands.basic.RunTrolley;
import frc.robot.commands.basic.RunWrist;
import frc.robot.commands.sequences.AutoAmp;
import frc.robot.commands.sequences.AutoIntake;
import frc.robot.commands.sequences.AutoShootingPoseToFloorIntake;
import frc.robot.commands.sequences.GoHome;
import frc.robot.commands.sequences.ShootingPosition;
import frc.robot.commands.sequences.SourceIntake;
import frc.robot.commands.sequences.AutoClimb;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.LEDStrip;
import frc.robot.subsystems.Pivot;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Trolley;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.LEDStrip.LEDColor;

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

  PhotonCamera camera = new PhotonCamera("shooterCam");

  private final boolean startInManualMode = false;

  private void configureInitialControllerBindings() {
    /* Drivetrain */ 
    drivetrain.setDefaultCommand( // Drivetrain will execute this command periodically
    drivetrain.applyRequest(() -> drive.withVelocityX(-driverController.getLeftY() * MaxSpeed) // Drive forward with
                                                                                        // negative Y (forward)
        .withVelocityY(-driverController.getLeftX() * MaxSpeed) // Drive left with negative X (left)
        .withRotationalRate(-driverController.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
    ));

    configureDriverControllerBindings();

    if (Utils.isSimulation()) {
      drivetrain.seedFieldRelative(new Pose2d(new Translation2d(), Rotation2d.fromDegrees(0)));
    }
    drivetrain.registerTelemetry(logger::telemeterize);

    if (startInManualMode) {
      configureOperatorControllerManualModeBindings();
    } else {
      configureOperatorControllerSmartModeBindings();
    }
  }

  private void configureDriverControllerBindings() {

    driverController.cross().whileTrue(drivetrain.applyRequest(() -> brake));
    driverController.circle().whileTrue(drivetrain
        .applyRequest(() -> point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))));

    // reset the field-centric heading on left bumper press
    driverController.L1().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldRelative()));
    driverController.povUp().whileTrue(drivetrain.applyRequest(() -> forwardStraight.withVelocityX(2).withVelocityY(0)));
    driverController.povDown().whileTrue(drivetrain.applyRequest(() -> forwardStraight.withVelocityX(-2).withVelocityY(0)));
    driverController.povLeft().whileTrue(drivetrain.applyRequest(() -> forwardStraight.withVelocityX(0).withVelocityY(2)));
    driverController.povRight().whileTrue(drivetrain.applyRequest(() -> forwardStraight.withVelocityX(0).withVelocityY(-2)));
    
    driverController.R1().whileTrue(new RunIntake(intake, leds, IntakeConstants.INTAKE_FIRE_SPEED).alongWith(new RunIndexer(indexer, 0.75)));

    driverController.square().whileTrue(drivetrain.aimAtTarget(camera));
  }

  public void removeOperatorControllerBindings() {
    // This should (hopefully) clear all buttons. 
    CommandScheduler.getInstance().getActiveButtonLoop().clear(); 
    // If all the button bindings have been cleared, that means the driver controller too.
    // Since our intention is to only clear the Operator controller bindings, it means we'll
    //  need to immediately re-instate the Driver Controller bindings.
    configureDriverControllerBindings();
  }

  public void configureOperatorControllerManualModeBindings() {
    leds.violet();
    operatorController.circle().whileTrue((new RunIntake(intake, leds, IntakeConstants.INTAKE_SPEED).until(intake::hasNote)).andThen(new RunLEDs(leds, LEDColor.GREEN)));
    operatorController.cross().whileTrue(new RunIntake(intake, leds, IntakeConstants.INTAKE_FIRE_SPEED));
    operatorController.triangle().whileTrue(new RunIntake(intake, leds, IntakeConstants.INTAKE_EJECT_SPEED));

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

    operatorController.square().whileTrue(new RunShooter(shooter, ShooterConstants.SPEED).alongWith(new AutoIndexer(indexer, IndexerConstants.SPEED)));

    operatorController.povLeft().whileTrue(new GoHome(pivot, trolley, wrist, leds));
    operatorController.povRight().whileTrue(new AutoIntake(intake, wrist, trolley, pivot, leds));
    operatorController.PS().whileTrue(new AutoAmp(intake, wrist, trolley, pivot, leds));
  }

  public void configureOperatorControllerSmartModeBindings() {
    leds.yellow();

    operatorController.L2().whileTrue(new GoHome(pivot, trolley, wrist, leds));

    operatorController.R1().whileTrue(new AutoIntake(intake, wrist, trolley, pivot, leds));

    operatorController.L1().whileTrue(new RunIntake(intake, leds, -IntakeConstants.INTAKE_SPEED)); // eject
    operatorController.R2().whileTrue(new RunIntake(intake, leds, 0.25)); // intake

    
    operatorController.povUp().whileTrue(new RunTrolley(trolley, TrolleyConstants.TROLLEY_FORWARD_SPEED).until(trolley::isTrolleyAtMaxOutLimitSwitch)); // trolley out
    operatorController.povDown().whileTrue(new RunTrolley(trolley, TrolleyConstants.TROLLEY_REVERSE_SPEED).until(trolley::isTrolleyAtMinInLimitSwitch)); // trolley in

    operatorController.touchpad().whileTrue(new ShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, PivotConstants.HOME_SETPOINT_POS, leds));
    operatorController.triangle().whileTrue(new ShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, PivotConstants.SUBWOFFER_SETPOINT_POS, leds));
    operatorController.square().whileTrue(new ShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, PivotConstants.PODIUM_SETPOINT_POS, leds));
    operatorController.cross().whileTrue(new AutoAmp(intake, wrist, trolley, pivot, leds));
    operatorController.circle().whileTrue(new SourceIntake(intake, wrist, trolley, pivot, leds));
    operatorController.options().whileTrue(new ShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, PivotConstants.TRUSS_SETPOINT_POS, leds));

    operatorController.PS().whileTrue(new AutoShootingPoseToFloorIntake(intake, wrist, trolley, pivot, leds));
    operatorController.create().whileTrue(new AutoClimb(intake, wrist, trolley, pivot, leds));
  }

  public void toggleManualModeWhenButtonPressed() {
    if (operatorController.getHID().getRawButtonPressed(15)) {
      boolean before = GlobalState.isManualMode();
      boolean after = !before;
      System.out.println("TOGGLE MANUAL MODE from "+before+" to "+after+".");
      removeOperatorControllerBindings();
      SmartDashboard.putBoolean(GlobalState.MANUAL_MODE_KEY, after);
      if (after) {
        configureOperatorControllerManualModeBindings();
      } else {
        configureOperatorControllerSmartModeBindings();
      }
    }
  }

  public RobotContainer() {

    trolley.setPivotRef(pivot);
    trolley.setWristRef(wrist);
    pivot.setTrolleyRef(trolley);
    pivot.setWristRef(wrist);

    NamedCommands.registerCommand("SubWooferShot",new SubWooferShootingPosition(intake, wrist, trolley, pivot, indexer, shooter, leds));
    NamedCommands.registerCommand("GoHome", new AutoGoHome(pivot, trolley, wrist, leds));
    NamedCommands.registerCommand("AutonIntake", new AutonIntake(intake, wrist, trolley, pivot, leds));
    NamedCommands.registerCommand("SpikeMarkShot", new SpikeMarkShot(intake, wrist, trolley, pivot, indexer, shooter, leds));
    NamedCommands.registerCommand("EventMarkIntake", new EventMarkIntake( wrist, trolley, pivot));
    NamedCommands.registerCommand("Shoot", new Shoot(shooter, indexer));

    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
    configureInitialControllerBindings();

    // Default to non-manual mode (i.e. false)
    SmartDashboard.putBoolean(GlobalState.MANUAL_MODE_KEY, false);
  }

  private int dashboardErrorCounter = 0;

  public void updateDashboard() {
    try {
      SmartDashboard.putBoolean("CAMERA_HAS_TARGET",camera.getLatestResult().hasTargets());
    } catch (Exception e) {
      // Avoid printing this constantly. Maybe just every 50 iterations or so.
      if (dashboardErrorCounter >= 50) {
        System.out.println("UpdateDashboard Exception:"+e.getMessage());
        dashboardErrorCounter = 0;
      } else {
        ++dashboardErrorCounter;
      }
    }
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
