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
     /* Start at velocity 0, enable FOC, no feed forward, use slot 0 */
  private final VelocityVoltage m_voltageVelocity = new VelocityVoltage(0, 0, true, 0, 0, false, false, false);

  @Override
  public void robotInit() {
    driveTrain = new SwerveDrive();
    //testSwerveModule = new SwerveModule(5, 14, 0);
    driverController = new DriverController(driveTrain);
    // driveTrain.driveSet(0, 0.5,0.5, 0.5);
    intakeTop = new CANSparkMax(20, MotorType.kBrushless);
    intakeBottom = new CANSparkMax(32, MotorType.kBrushless);

    falconShooterLeftLeader = new TalonFX(48);
    falconShooterRightFollower = new TalonFX(49);

    // start with factory-default configs
    var currentConfigs = new MotorOutputConfigs();

    // The left motor is CCW+
    currentConfigs.Inverted = InvertedValue.CounterClockwise_Positive;
    falconShooterLeftLeader.getConfigurator().apply(currentConfigs);

    TalonFXConfiguration configs = new TalonFXConfiguration(); 

     /* Voltage-based velocity requires a feed forward to account for the back-emf of the motor */
    configs.Slot0.kP = 0.11; // An error of 1 rotation per second results in 2V output
    configs.Slot0.kI = 0.5; // An error of 1 rotation per second increases output by 0.5V every second
    configs.Slot0.kD = 0.0001; // A change of 1 rotation per second squared results in 0.01 volts output
    configs.Slot0.kV = 0.12; // Falcon 500 is a 500kV motor, 500rpm per V = 8.333 rps per V, 1/8.33 = 0.12 volts / Rotation per second
    // Peak output of 8 volts
    configs.Voltage.PeakForwardVoltage = 8;
    configs.Voltage.PeakReverseVoltage = -8;
    
    /* Torque-based velocity does not require a feed forward, as torque will accelerate the rotor up to the desired velocity by itself */
    //configs.Slot1.kP = 5; // An error of 1 rotation per second results in 5 amps output
    //configs.Slot1.kI = 0.1; // An error of 1 rotation per second increases output by 0.1 amps every second
    //configs.Slot1.kD = 0.001; // A change of 1000 rotation per second squared results in 1 amp output

    // Peak output of 40 amps
    configs.TorqueCurrent.PeakForwardTorqueCurrent = 40;
    configs.TorqueCurrent.PeakReverseTorqueCurrent = -40;

    /* Retry config apply up to 5 times, report if failure */
    StatusCode status = StatusCode.StatusCodeNotInitialized;
    for (int i = 0; i < 5; ++i) {
      status = falconShooterLeftLeader.getConfigurator().apply(configs);
      if (status.isOK()) break;
    }
    if(!status.isOK()) {
      System.out.println("Could not apply configs, error code: " + status.toString());
    }

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
      falconShooterLeftLeader.set(0.12);

    }else if(driverController.controller.getBButton()){
      //falconShooterLeftLeader.set(0.75);   
      //The velocity is in rotations per second
      falconShooterLeftLeader.setControl(m_voltageVelocity.withVelocity(50));

    }else{
      falconShooterLeftLeader.set(0);   
    }

    if(driverController.controller.getRightBumper()){
      intakeBottom.set(0.10);
      intakeTop.set(-0.10);
    }else if(driverController.controller.getLeftBumper()){
        intakeBottom.set(0);
        intakeTop.set(0);
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
