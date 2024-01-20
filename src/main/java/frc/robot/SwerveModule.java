package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.CAN;

public class SwerveModule {

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
    private SparkPIDController m_pidController;
    private RelativeEncoder m_encoder;
    AnalogInput encoder;

    CANSparkMax driveMotorCont;
    CANSparkMax turnMotorCont;

    SwerveModule(int driveId, int turnId, int encoderId) {
        turnMotorCont = new CANSparkMax(turnId, MotorType.kBrushless);
        driveMotorCont = new CANSparkMax(driveId, MotorType.kBrushless);
        InitPIDLoop(driveMotorCont);
        InitPIDLoop(turnMotorCont);
        encoder = new AnalogInput(encoderId);
        encoder.setAverageBits(4);
    }

    void setDrive(double driveSpeed) {
        driveMotorCont.set(driveSpeed);
    }


    void InitPIDLoop(CANSparkMax motor) {
        m_pidController = motor.getPIDController();
        m_encoder = motor.getEncoder();

        // PID coefficients
        kP = 0.1;
        kI = 1e-4;
        kD = 1;
        kIz = 0;
        kFF = 0;
        kMaxOutput = 1;
        kMinOutput = -1;

        // set PID coefficients
        m_pidController.setP(kP);
        m_pidController.setI(kI);
        m_pidController.setD(kD);
        m_pidController.setIZone(kIz);
        m_pidController.setFF(kFF);
        m_pidController.setOutputRange(kMinOutput, kMaxOutput);
    }

    int getEncoderAngle(){
        return encoder.getAverageValue();
    }
}
