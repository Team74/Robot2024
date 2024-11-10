package frc.robot;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;

public class Climber {

    //This is the class for the climber. The design functioned very similarly to a "climber in a box" system 
    //That meaning 2 winches to lift and lower a hook. 

    //Creating Classes
    CANSparkMax leftClimber, rightClimber;

    //Setting the motor type and idle mode in it the class Init
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
        rightClimber.set(power);
    }

    //Set the power of the right one only, used to make them in sync if they unsync
    void setRightPower(double power){
        rightClimber.set(power);
    }

    //Set the power of the left one only, used to make them in sync if they unsync
    void setLeftPower(double power){
        leftClimber.set(power);
    }
}
