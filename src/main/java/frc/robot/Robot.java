// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;

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
   CANSparkMax intakeTop, intakeBottom;
   TalonFX falconShooterLeftLeader, falconShooterRightFollower;
   DriverController driverController;
   SwerveModule testSwerveModule; //this is a test module
   Encoder testEncoder = new Encoder(5);

  @Override
  public void robotInit() {
    driveTrain = new SwerveDrive();
    //testSwerveModule = new SwerveModule(5, 14, 0);
    driverController = new DriverController(driveTrain);
    // driveTrain.driveSet(0, 0.5,0.5, 0.5);
    //intakeTop = new CANSparkMax(31, MotorType.kBrushed);
    //intakeBottom = new CANSparkMax(13, MotorType.kBrushed);

    falconShooterLeftLeader = new TalonFX(16);
    falconShooterRightFollower = new TalonFX(17);

    // start with factory-default configs
    var currentConfigs = new MotorOutputConfigs();

    // The left motor is CCW+
    currentConfigs.Inverted = InvertedValue.CounterClockwise_Positive;
    falconShooterLeftLeader.getConfigurator().apply(currentConfigs);

    // The right motor is CW+
    //currentConfigs.Inverted = InvertedValue.Clockwise_Positive;
    //falconShooterRightFollower.getConfigurator().apply(currentConfigs);

    // Ensure our followers are following their respective leader
    falconShooterRightFollower.setControl(new Follower(falconShooterLeftLeader.getDeviceID(), true));
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {
    //frontRight.setDrive(0.25);
    driverController.run();
    //testSwerveModule.setModulePower(1, 0);
    //System.out.println(testEncoder.getDoubleValue());

    if(driverController.controller.getAButton())
    {
      System.out.println("A button pressed");
      falconShooterLeftLeader.set(0.1);

    }else{
      falconShooterLeftLeader.set(0);   

    }
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
