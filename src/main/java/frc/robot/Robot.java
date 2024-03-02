// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.sql.Time;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.VelocityTorqueCurrentFOC;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */

   SwerveDrive driveTrain;
   //Climber climber = new Climber(33, 37);
   Intake intake = new Intake(20,32);
   Shooter shooter = new Shooter(48, 49);
   TalonFX falconShooterLeftLeader, falconShooterRightFollower;
   DriverController driverController;
   SwerveModule testSwerveModule; //this is a test module
   Encoder testEncoder = new Encoder(5);
   XboxController opController = new XboxController(1);

   SendableChooser<String> autonChooser = new SendableChooser<>();
   String autonSelected;
   Timer timer = new Timer();
   Auton auton;
   Double time;

   double autoGyroOffset = 0.0;

    //Autons
    String auDefaultAuton = "Default_Auton";
    String auAmp_2P = "Amp_2_Piece";
    String auAmp_4P = "Amp_4_Piece";
    String auShootMove = "Shoot_Move";

    ShuffleboardTab driveTab = Shuffleboard.getTab("Drive3");
    GenericEntry intakePieceField = driveTab.add("Have Piece", false).getEntry();
    GenericEntry gyroAngleField = driveTab.add("Gyro Angle", false).getEntry();

    /* Start at velocity 0, enable FOC, no feed forward, use slot 0 */

  @Override
  public void robotInit() {
    driveTrain = new SwerveDrive();
    //testSwerveModule = new SwerveModule(5, 14, 0);
    driverController = new DriverController(driveTrain);
    // driveTrain.driveSet(0, 0.5,0.5, 0.5);
    CameraServer.startAutomaticCapture(0);

    autonChooser.setDefaultOption("Default Auto", auDefaultAuton);
    autonChooser.addOption("Amp Side 2 Piece", auAmp_2P);
    autonChooser.addOption("Shoot and Move", auShootMove);

    Shuffleboard.getTab("Drive3").add(autonChooser);
    
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {
    timer.reset();
    timer.start();
    autonSelected = autonChooser.getSelected();

    switch(autonSelected){
      case auAmp_2P:
      auton = new Auton_AmpSide_2P(driveTrain, shooter, intake, false);
      autoGyroOffset = -54.6; 
      break;

      case auAmp_4P:
      auton = new Auton_AmpSide_4P(driveTrain, shooter, intake, false);
      autoGyroOffset = -54.6; 
      break;

      case auShootMove:
      auton = new Auton_Move(driveTrain, shooter, intake, false);
      autoGyroOffset = 0.0;
      break;
    default:
      System.out.println("Auton Failed, Defualt Auto");
auton = new Auton_Move(driveTrain, shooter, intake, false);
    }

    //This is so we can start against the speaker and still have 0 be away from driver station. Don't need a offset for center spot. 
    //TODO check if the gyro is good after auton
    if(autoGyroOffset != 0.0){
      driveTrain.resetGyroWithOffset(autoGyroOffset);
    }else{
      driveTrain.resetGyro();
    }
    
  }

  @Override
  public void autonomousPeriodic() {
    time = timer.get();
    if(auton != null){
      auton.run(time);
    }
  }

  @Override
  public void teleopInit() { }

  @Override
  public void teleopPeriodic() {
    //frontRight.setDrive(0.25);
    driverController.run();
    //testSwerveModule.setModulePower(1, 0);
    //System.out.println(testEncoder.getDoubleValue());

    //Drive Controls
    if(opController.getAButton())
    {
      shooter.setTargetRPS(90);

    }else if(driverController.controller.getBButton()){
      //The velocity is in rotations per second
      shooter.setTargetRPS(3.5);

    }else{
      shooter.setPower(0.0);
    }

    //Intake Controls
    if(opController.getLeftTriggerAxis() > 0.3){ //Intake
      intake.setPower(0.9);
    }else if(opController.getRightTriggerAxis() > 0.3){ //Outtake
      intake.setPower(-0.2);
    }else if(opController.getRightBumper()){ //Intake until piece
      intake.setPowerUntilPiece(0.9);
    }else{ //Stop Motor if no controls
      intake.setPower(0.0);
    }
    
/* 
    //Climber Controls
    if(opController.getPOV() == 180){ //Climb Up
      climber.setPowerTogether(-0.5);
    }else if(opController.getPOV() == 0){ //Climb Down
      climber.setPowerTogether(0.5);
    }else if(opController.getPOV() == 90){ //Climb Down Left
      climber.setLeftPower(-0.5);
    }else if(opController.getPOV()== 270){ //Climb Down Right
      climber.setRightPower(-0.5);
    }else{ //Turn off power
      climber.setPowerTogether(0);
    }
*/

    //Puts a bool to the shuffleboard saying if we have a piece.
    intakePieceField.setBoolean(intake.hasPiece());
    gyroAngleField.setInteger(driveTrain.gyro.getAngle());

  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
