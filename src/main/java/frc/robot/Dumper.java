package frc.robot;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Servo;

import com.revrobotics.CANSparkMax;

public class Dumper {
    
   CANSparkMax dumperMotor;
   Encoder encoder;
   PIDController anglePIDController = new PIDController(0, 0, 0.05);
   int closeEnc, openEnc;

     Dumper(int motorID, int encoderID){
        dumperMotor = new CANSparkMax(motorID, MotorType.kBrushless);
        encoder = new Encoder(encoderID);
        dumperMotor.setIdleMode(IdleMode.kBrake);
        pidInit();
        closeEnc = getEncoderAngle();
        
    }

    void pidInit(){
        anglePIDController.reset();
        anglePIDController.enableContinuousInput(0, 4096); //this means that when it hits 0, it will go to 4096. It acts like a circle
        anglePIDController.setTolerance(150);
        anglePIDController.setD(0.00001);
        anglePIDController.setI(0.000);
        anglePIDController.setP(0.05);
    }

    double powerFinal;
    //Moves the climber together, they should be in sync
    void setEncoderValue(double targetAngle){
       //dont call calc 2 times in one cycle, it does not like it 
        double calc = anglePIDController.calculate(getEncoderAngle(), targetAngle); //get the pid power
        powerFinal = (calc); //power multi is the "gear" system, speed up / slow down
       // System.out.println(powerFinal);
        //we do this as we dont want to strain motors or batteries when we are at the setpoint. If we don't do this it will set the power to a very small number
        if (!anglePIDController.atSetpoint()) {
           dumperMotor.set(1 * MathUtil.clamp(powerFinal, -0.35, 0.15));
        } else {
           dumperMotor.set(0);
        }
    }

    int encoderOffset = 0;
    int getEncoderAngle() {
        //System.out.println("Test 1: " + testEncoder.getAverageValue());
        return ((encoder.getAverageValue() - encoderOffset) + encoder.encoderMax)% 4096; //we get it from this so if the encoder changes it wont effect this class. Mod so it rolls over
    }

    void close(){
        setEncoderValue(closeEnc);
    }
    void open(){
        int angle;
        int num = closeEnc - 400;
        if (num < 0){
            angle = encoder.encoderMax + num;
        }else{
            angle = num;
        }
        setEncoderValue(angle);
    }
    void setSpeed(){
       
    }
    void setPower(double power){
        dumperMotor.set(power);
    }

}
