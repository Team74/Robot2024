package frc.robot;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Servo;

import com.revrobotics.CANSparkMax;

public class Dumper {

    //This is the class for the "Dumper", a plate that flips over to move the note. 
    //A lot of this code is unused do to not making it to states this year. Thus, the PID loop was not tuned and was unused.
    //I will leave this code intact and note when it is unfinished. 

    CANSparkMax dumperMotor;


    Encoder encoder;
    PIDController anglePIDController = new PIDController(0, 0, 0.05);
    int closeEnc, openEnc;

    //Setting the type of motor and setting encoder ports. Setting brake mode as well
    Dumper(int motorID, int encoderID) {
        dumperMotor = new CANSparkMax(motorID, MotorType.kBrushless);
        dumperMotor.setIdleMode(IdleMode.kBrake);
        
        //These go unused
        encoder = new Encoder(encoderID);
        pidInit();
        closeEnc = getEncoderAngle();

    }

    //This function went unused. It sets the setting of the PID loop
    void pidInit() {
        anglePIDController.reset();
        // this means that when it hits 0, it will go to 4096. It acts like a circle
        anglePIDController.enableContinuousInput(0, 4096); 
        anglePIDController.setTolerance(150);
        anglePIDController.setD(0.00001);
        anglePIDController.setI(0.000);
        anglePIDController.setP(0.05);
    }

    double powerFinal;

    // This code went unused. It was copied from swerve drive class
    void setEncoderValue(double targetAngle) {
        // dont call calc 2 times in one cycle, it does not like it
        double calc = anglePIDController.calculate(getEncoderAngle(), targetAngle); // get the pid power
        powerFinal = (calc); // power multi is the "gear" system, speed up / slow down
    
        // we do this as we dont want to strain motors or batteries when we are at the
        // setpoint. If we don't do this it will set the power to a very small number
        if (!anglePIDController.atSetpoint()) {
            dumperMotor.set(1 * MathUtil.clamp(powerFinal, -0.35, 0.15));
        } else {
            dumperMotor.set(0);
        }
    }

    int encoderOffset = 0;

    //Again, unused
    int getEncoderAngle() {
        // we get it from the encoder class so if the encoder changes it wont effect this class. Mod so it rolls over
        return ((encoder.getAverageValue() - encoderOffset) + encoder.encoderMax) % 4096; 
    }

    //It would set it to the value at starting config. Again, unused
    void close() {
        setEncoderValue(closeEnc);
    }

    //Again, unused
    void open() {
        int angle;
        int num = closeEnc - 400;
        if (num < 0) {
            angle = encoder.encoderMax + num;
        } else {
            angle = num;
        }
        setEncoderValue(angle);
    }

    //This is the only functioned that ended up being used. Very simple set power function with a clamp to prevent breaking
    void setPower(double power) {
        dumperMotor.set(MathUtil.clamp(power, -0.4, 0.4));
    }

}
