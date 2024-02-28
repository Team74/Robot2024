package frc.robot;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DigitalInput;

public class Intake {
       CANSparkMax intakeTop, intakeBottom;
       DigitalInput limitSensor = new DigitalInput(4);

    Intake(int intakeTopID, int intakeBottomID){
    intakeTop = new CANSparkMax(intakeTopID, MotorType.kBrushless);
    intakeBottom = new CANSparkMax(intakeBottomID, MotorType.kBrushless);

    }

    void setPower(double power){
        intakeTop.set(power);
        intakeBottom.set(power);
    }

    void setPowerUntilPiece(double power){
        if(limitSensor.get())
        {
            intakeTop.set(power);
            intakeBottom.set(power);
        }
    }

    Boolean hasPiece()
    {
        return limitSensor.get();
    }
}
