package frc.robot;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.Servo;

import com.revrobotics.CANSparkMax;

public class Dumper {
    
    Servo leftServo, rightServo;

     Dumper(int leftServoID, int rightServoID){
        leftServo = new Servo(leftServoID);
        rightServo = new Servo(rightServoID);
        
    }

    //Moves the climber together, they should be in sync
    void setAngle(double angle){
       rightServo.setAngle(angle);
       leftServo.setAngle(180 - angle);
    }

    void setSpeed(){
        rightServo.setDisabled();
        leftServo.setDisabled();
    }
}
