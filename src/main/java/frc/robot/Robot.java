// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;

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
   TalonFX falconTest;
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
    //falconTest = new TalonFX(16);
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
      //falconTest.set(0.1);
    }else{
      //falconTest.set(0);
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
