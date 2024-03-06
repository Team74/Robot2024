package frc.robot;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;

public class Climber {
    CANSparkMax leftClimber, rightClimber;

    //Climber Init
    Climber(int leftCLimberID, int rightClimberID){
        leftClimber = new CANSparkMax(leftCLimberID, MotorType.kBrushless);
        rightClimber = new CANSparkMax(rightClimberID, MotorType.kBrushless);

        //Set to brake so when we lose control we don't fall
        leftClimber.setIdleMode(IdleMode.kBrake);
        rightClimber.setIdleMode(IdleMode.kBrake);
    }

    //Moves the climber together, they should be in sync
    void setPowerTogether(double power){
        leftClimber.set(power);
        rightClimber.set(-power);
    }

    //Set the power of the right one only, used to make them in sync 
    void setRightPower(double power){
        rightClimber.set(power);
    }

    //Set the power of the left one only, used to make them in sync 
    void setLeftPower(double power){
        leftClimber.set(power);
    }
}
