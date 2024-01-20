package frc.robot;

import com.revrobotics.CANSparkMax;                                     
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CAN;

public class SwerveModule {
    AnalogInput encoder;
    CANSparkMax driveMotorCont;
    CANSparkMax turnMotorCont;
    SwerveModule (int driveId, int turnId, int encoderId) {
        turnMotorCont = new CANSparkMax(turnId, MotorType.kBrushless);
        driveMotorCont = new CANSparkMax(driveId, MotorType.kBrushless);
        encoder = new AnalogInput(encoderId);
        encoder.setAverageBits(4);
    }
    void setDrive (double driveSpeed) {
        driveMotorCont.set(driveSpeed);
    }
    int getEncoderAngle(){
        return encoder.getAverageValue();
    }

}
