package frc.robot;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

public class Intake {
       CANSparkMax intakeTop, intakeBottom;

    Intake(int intakeTopID, int intakeBottomID){
    intakeTop = new CANSparkMax(intakeTopID, MotorType.kBrushless);
    intakeBottom = new CANSparkMax(intakeBottomID, MotorType.kBrushless);

    }

    void setPower(double power){
        intakeTop.set(power);
        intakeBottom.set(power);
    }
}
