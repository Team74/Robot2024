package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveModule {

    //Adding the tabs to the SuffleBoard. Some off these are inputs for the PID Loop

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, tol;
    // AnalogInput encoder;
    Encoder testEncoder;
    PIDController pidController = new PIDController(0, 0, 0);
    double lastTarget, targetAngle, encoderOffset;

    CANSparkMax driveMotorCont;
    CANSparkMax turnMotorCont;

    SwerveModule(int driveId, int turnId, int encoderId) {
        turnMotorCont = new CANSparkMax(turnId, MotorType.kBrushless);
        driveMotorCont = new CANSparkMax(driveId, MotorType.kBrushless);
        testEncoder = new Encoder(encoderId);
        testEncoder.encoderInit();
        PIDInit();
    }

    void setDrive(double driveSpeed, Rotation2d turnAngle) {
        targetAngle = (turnAngle.getRadians())/ (2 * Math.PI) * testEncoder.encoderMax; //convert the radians to encoder ticks
        setModulePower(1, targetAngle);
    }

    int getEncoderAngle() {
        return testEncoder.getAverageValue(); //we get it from this so if the encoder changes it wont effect this class. 
    }
    double getEncoderAngleRadians() //converts the encoder ticks to radians
    {
        return ((getEncoderAngle()/(testEncoder.encoderMax)) * Math.PI * 2);
    }

    void PIDInit() {
        pidController.enableContinuousInput(0, 4096);
        pidController.setTolerance(100);
        pidController.setD(0.05);
    }

    //These are called from the Swerve Drive Class
    void setPIDP(double p){
        pidController.setP(p);
    }
     void setPIDI(double i){
        pidController.setI(i);
    }
     void setPIDD(double d){
        pidController.setD(d);
    }
    void setPIDTol(double tol){
        pidController.setD(tol);
    }

    void setModulePower(double powerMulti, double targetAngle){
        targetAngle = targetAngle - encoderOffset;
        //System.out.println("tar: " + targetAngle + "  enco: " + getEncoderAngle());
        double powerFinal = (pidController.calculate(getEncoderAngle(), targetAngle) * powerMulti);
        if (!pidController.atSetpoint()) {
           turnMotorCont.set(-1 * MathUtil.clamp(powerFinal, -0.2, 0.2));
        } else {
            turnMotorCont.set(0);
        }
    }

}
