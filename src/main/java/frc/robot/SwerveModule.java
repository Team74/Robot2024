package frc.robot;

import com.revrobotics.CANSparkMax;                                     
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.CAN;

public class SwerveModule {
    CANSparkMax driveMotorCont;
    CANSparkMax turnMotorCont;
    SwerveModule (int driveId, int turnId) {
        turnMotorCont = new CANSparkMax(turnId, MotorType.kBrushless);
        driveMotorCont = new CANSparkMax(driveId, MotorType.kBrushless);
    }
    void setDrive (double driveSpeed) {
        driveMotorCont.set(driveSpeed);
    }
}
