// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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

   SwerveModule frontRight;
   CANSparkMax intakeTop;
   CANSparkMax intakeBottom; 
   XboxController driverController = new XboxController(0);

  @Override
  public void robotInit() {
    frontRight = new SwerveModule(5,14,0);
    intakeTop = new CANSparkMax(31, MotorType.kBrushed);
    intakeBottom = new CANSparkMax(13, MotorType.kBrushed);
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

    if(driverController.getRightTriggerAxis() > 0.1)
    {
      intakeTop.set(driverController.getRightTriggerAxis() * 0.4);
      intakeBottom.set(driverController.getRightTriggerAxis() * -1);
    }else if(driverController.getBButton()){
      intakeTop.set(-0.5);
      intakeBottom.set(0.5);
    }else{
      intakeTop.set(0);
      intakeBottom.set(0);
    }
    frontRight.setPIDLoop();
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