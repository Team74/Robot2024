package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.CAN;

public class SwerveModule {
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput;
    private SparkPIDController m_pidController;
    private RelativeEncoder m_encoder;

    CANSparkMax driveMotorCont;
    CANSparkMax turnMotorCont;

    SwerveModule(int driveId, int turnId) {
        turnMotorCont = new CANSparkMax(turnId, MotorType.kBrushless);
        driveMotorCont = new CANSparkMax(driveId, MotorType.kBrushless);
        InitPIDLoop(driveMotorCont);
        InitPIDLoop(turnMotorCont);
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
}
