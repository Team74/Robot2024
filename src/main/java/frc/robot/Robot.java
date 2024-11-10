// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.sql.Time;

import javax.swing.text.html.HTMLDocument.BlockElement;

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
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the
 * name of this class or
 * the package after creating this project, you must also update the
 * build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any
   * initialization code.
   */

   //initializing a bunch of classes
  SwerveDrive driveTrain;
  Dumper dumper = new Dumper(29, 6);
  Climber climber = new Climber(4, 33);
  Intake intake = new Intake(20, 32);
  Shooter shooter = new Shooter(48, 49);
  TalonFX falconShooterLeftLeader, falconShooterRightFollower;
  DriverController driverController;
  SwerveModule testSwerveModule; // this is a test module
  Encoder testEncoder = new Encoder(5);
  XboxController opController = new XboxController(1);

  //auton stuff 
  SendableChooser<String> autonChooser = new SendableChooser<>(); //for shuffleboard
  SendableChooser<String> colorChooser = new SendableChooser<>();
  String autonSelected;
  Timer timer = new Timer();
  Auton auton;
  Double time;

  //used for leds and auton
  String color = "Red";
  Boolean isBlue = false;

  //I believe this goes unused 
  double autoGyroOffset = 0.0;

  //led stuff
  RGBLED lights = new RGBLED(3);
  boolean isRainbow = false;

  // Autons
  private static final String auDefaultAuton = "Default_Auton";
  private static final String auAmp_2P = "Amp_2_Piece";
  private static final String auSource_2P = "Source_2_Piece";
  private static final String auShootMove = "Shoot_Move";
  private static final String auCenter_2P = "Center_2_Piece";
  private static final String auCenter_3P = "Center_3_Piece";

  //add to the shuffleboard 
  ShuffleboardTab driveTab = Shuffleboard.getTab("Drive3");
  GenericEntry intakePieceField = driveTab.add("Have Piece", false).getEntry();
  GenericEntry gyroAngleField = driveTab.add("Gyro Angle", 0.0).getEntry();

  private static final String colorRed = "Red";
  private static final String colorBlue = "Blue";


  /* Start at velocity 0, enable FOC, no feed forward, use slot 0 */

  @Override
  public void robotInit() {
    //create the drive train
    driveTrain = new SwerveDrive();
    // testSwerveModule = new SwerveModule(5, 14, 0);
    driverController = new DriverController(driveTrain);
    // driveTrain.driveSet(0, 0.5,0.5, 0.5);

    //Init Camera
    CameraServer.startAutomaticCapture(0);

    //Add everything to the shuffleboard, including this "spinner" (dropdown menu)
    autonChooser.setDefaultOption("Default Auto", auDefaultAuton);
    autonChooser.addOption("Amp 2 Piece", auAmp_2P);
    autonChooser.addOption("Shoot and Move", auShootMove);
    autonChooser.addOption("Center 4 Piece", auCenter_2P);
    autonChooser.addOption("Source 2 Piece", auSource_2P);
    autonChooser.addOption("Center 3 Piece (Source)", auSource_2P);

    Shuffleboard.getTab("Drive3").add(autonChooser);

    colorChooser.setDefaultOption("Red", colorRed);
    colorChooser.addOption("Blue", colorBlue);
    
    Shuffleboard.getTab("Drive3").add(colorChooser);

  }

  @Override
  public void robotPeriodic() {
  
    //this was to fix a bug where the spinner would not show up. If this happens then def look into it.
    //This ended up going unused 

    //if (autonChooser.getSelected() == null || autonChooser.getSelected() == ""
      //  || Shuffleboard.getTab("Drive3") == null) {
      //System.out.println("Auton Selector Null");
    //}
  }

  @Override
  public void autonomousInit() {
    //reset the gyro so forward is forward.
    //This is where the unused code for the offset was going to be used
    //The idea of the offset is to make it so if the robot starts not facing forward, it still knows where forward is.
    //I would look into the offset if you have time and it is needed
    driveTrain.resetGyro();
    //Timer init
    timer.reset();
    timer.start();
    autonSelected = autonChooser.getSelected();
    //It was a flipped field, so you have to flip left/right 
    color = colorChooser.getSelected();
    if(color == "Blue"){
      isBlue = true;
    }else{
      isBlue = false; 
    }
    System.out.println(autonSelected);

    //Auton class init. Each sub class of auton extends the main class so you can do this
    //System.out is your friend!
    switch (autonSelected) {
      case auAmp_2P:
        auton = new Auton_AmpSide_4P(driveTrain, shooter, intake, isBlue);
        System.out.println("Running Amp Auto");
        autoGyroOffset = -54.6; //again, unused 
        break;

      case auSource_2P:
        auton = new Auton_SourceSide_2P(driveTrain, shooter, intake, isBlue);
        autoGyroOffset = -54.6;
        System.out.println("Running Source Auto");
        break;

      case auCenter_2P:
        auton = new Auton_Center_2P(driveTrain, shooter, intake, isBlue);
        System.out.println("Running Center 2 Auto");
        autoGyroOffset = 0.0;
        break;

      case auCenter_3P:
         auton = new Auton_Center_4P(driveTrain, shooter, intake, false);
        System.out.println("Running Center 3 Auto");
        autoGyroOffset = 0.0;
        break;

      case auShootMove:
        auton = new Auton_Move(driveTrain, shooter, intake, isBlue);
        autoGyroOffset = 0.0;
        break;
      default:
        System.out.println("Auton Failed, Default Auto");
        auton = new Auton_Center_4P(driveTrain, shooter, intake, isBlue);
        System.out.println("Running No Auto");
        break;
    }

    // This is so we can start against the speaker and still have 0 be away from
    // driver station. Don't need a offset for center spot.
    // TODO check if the gyro is good after auton
    if (autoGyroOffset != 0.0) {
      //Again, unused. This code is the same as reset gyro without offset
      driveTrain.resetGyroWithOffset(autoGyroOffset);
    } else {
      driveTrain.resetGyro();
    }

  }

  @Override
  public void autonomousPeriodic() {
    //Update timer
    time = timer.get();
    if (auton != null) {
      auton.run(time);
    }
  }

  @Override
  public void teleopInit() {
    //This was for the cool leds 
    color = colorChooser.getSelected();
    if(color == "Blue"){
      isBlue = true;
    }else{
      isBlue = false; 
    }
  }

  @Override
  public void teleopPeriodic() {
    // driveTrain.driveSet(0, -1, 0, 0.3);
    // frontRight.setDrive(0.25);

    //Run the driver controller. I dont think it needs to be in its own class but it does not really matter
    driverController.run();

    //This was for debug
    // Stuff to test if swerve is going max speed
    //RelativeEncoder relativeEncoder = driveTrain.backLeft.driveMotorCont.getEncoder();
    //System.out.println(relativeEncoder.getVelocity());

    //again, debug
    //System.out.println(intake.hasPieceFar());

    // testSwerveModule.setModulePower(1, 0);
    // System.out.println(testEncoder.getDoubleValue());

    /*
     * Operator Controls
     * A = Shoot, B = Bloop onto Amp
     * LT = Intake, RT = Outtake, RB = Intake until Piece
     * X and Y are Unused, going to be for Amp
     * D Pad for Climber, LB for modified climber
     * Sticks are Unused
     */

    // Shooter Controls
    if (opController.getAButton()) {
      shooter.setTargetRPS(90); //rev up

    }else if(opController.getPOV() == 90){ //outtake
      shooter.setTargetRPS(-5);

    }else if (opController.getBButton()) { //spit out
      // The velocity is in rotations per second
      shooter.setTargetRPS(3.5); // 3.5
      intake.setPower(0.8);
    } else if (opController.getRightTriggerAxis() > 0.3) { //auto shoot
      shooter.setTargetRPS(90);
      if (shooter.getRPS() > 75) {
        intake.setPower(0.9);
      }
    } else {
      //if nothing, turn off. very important. 
      //Note that it is power to 0, not rps to zero. If it was rps to zero, it will break HARD (learned the hard way)
      shooter.setPower(0.0); 
    }

    // Intake Controls
    if(opController.getLeftTriggerAxis() > 0.3) { // Intake
      intake.setPower(0.9);
    } else if (opController.getRightBumper()) { // Outtake
      intake.setPower(-0.5);
    } else if (opController.getLeftBumper()) { // Intake until piece
      intake.setPowerUntilPiece(0.9);
    } else if (!opController.getBButton() && !(opController.getRightTriggerAxis() > 0.3)) { // Stop Motor if no controls and if the shooter is not asking for intake controls
      intake.setPower(0.0);
    }

    // Climber Controls
    if (opController.getPOV() == 180) { // Climb Up
      climber.setPowerTogether(-0.6);
    } else if (opController.getPOV() == 0) { // Climb Down
      climber.setPowerTogether(0.6);
    } else if (opController.getLeftY() < -0.3) { // Climb Down Left
      climber.setLeftPower(0.3);
    } else if (opController.getRightY() < -0.3) { // Climb Down Right
      climber.setRightPower(0.3);
    } else if (opController.getLeftY() > 0.3) { // Climb Up Left
      climber.setLeftPower(-0.3);
    } else if (opController.getRightY() > 0.3) { // Climb Up Right
      climber.setRightPower(-0.3);
    } else { // Turn off power
      climber.setPowerTogether(0);
    }

    // Puts a bool to the shuffleboard saying if we have a piece.
    intakePieceField.setBoolean(!intake.hasPiece());
    gyroAngleField.setDouble(driveTrain.gyro.getAngle());

    // 50 ticks per sec
    // one button flip
    if (opController.getYButtonPressed()) {
      //Unused, this would be for auto dumper
    }
    /*
     * for (int i = 0; i < 152; i++) {
     * if(i < 70){//bloop
     * shooter.setTargetRPS(3.5);
     * intake.setPower(0.9);
     * }else if(i < 100){//flip out
     * shooter.setPower(0);
     * intake.setPower(0);
     * dumper.setPower(0.15);
     * }else if(i < 120){//wait
     * dumper.setPower(0);
     * }else if(i < 150){//flip back
     * dumper.setPower(-0.50);
     * }else{ //reset
     * shooter.setPower(0);
     * intake.setPower(0);
     * dumper.setPower(0);
     * }
     * }
     */

    //Dumper open and close
    if (opController.getYButton()) {
      // dumper.close();
      dumper.setPower(-0.17);
    } else if (opController.getXButton()) {
      // dumper.open();
      dumper.setPower(0.35);
    } else {
      //Again, very important
      dumper.setPower(0);
    }

    // System.out.println("Left: " + dumper.leftServo.getAngle() + " Right: " +
    // dumper.rightServo.getAngle());
    //System.out.println("angle " + driveTrain.gyro.getAngle());


    //Rainbow LEDs, very important
    if(driverController.controller.getBButtonPressed()){
      isRainbow = !isRainbow;
    }

    //Led control. 
    //If not rainbow, then set the color based on if we have the piece or team color
    if (!isRainbow) {
      if (intake.hasPiece()) {// green
        lights.setColor(0, 255, 0);
        System.out.println("piece in");
      } else if (intake.hasPieceFar()) {// yellow
        System.out.println("piece close");
        lights.setColor(255, 150, 0);
      } else {// team color
        System.out.println("piece no");
        if (isBlue) {
          lights.setColor(0, 0, 255);
        } else {
          lights.setColor(255, 0, 0);
        }
      }
    } else {
      lights.rainbow();
    }
  }

  //Unused, but you have to have this
  @Override
  public void disabledInit() {  }

  @Override
  public void disabledPeriodic() {  }

  @Override
  public void testInit() {  }

  @Override
  public void testPeriodic() {  }

  @Override
  public void simulationInit() {  }

  @Override
  public void simulationPeriodic() {  }

  

}
