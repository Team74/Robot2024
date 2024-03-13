package frc.robot;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.motorcontrol.Talon;

public class Shooter {
    
    TalonFX falconShooterLeftLeader, falconShooterRightFollower;
    private final VelocityVoltage m_voltageVelocity = new VelocityVoltage(0, 0, true, 0, 0, false, false, false);

    Shooter(int leftCANID, int rightCANID){

    falconShooterLeftLeader = new TalonFX(leftCANID);
    falconShooterRightFollower = new TalonFX(rightCANID);

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
    configs.Voltage.PeakForwardVoltage = 16;
    configs.Voltage.PeakReverseVoltage = -16;
    
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

    void setTargetRPS(double targetRPS)
    {
        falconShooterLeftLeader.setControl(m_voltageVelocity.withVelocity(targetRPS));
    }

    void setPower(double power)
    {
      falconShooterLeftLeader.set(power);
    }

    double getRPS(){
      double flyWheelRPS = falconShooterLeftLeader.getVelocity().getValue();
      return flyWheelRPS;
    }

}
