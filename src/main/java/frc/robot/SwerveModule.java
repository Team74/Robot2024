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
    PIDController pidController = new PIDController(0, 0, 0.05);
    double lastTarget;
    int targetAngle, encoderOffset;

    CANSparkMax driveMotorCont;
    CANSparkMax turnMotorCont;

    GenericEntry currentDisplayField;

    SwerveModule(int driveId, int turnId, int encoderId, int offset, GenericEntry currentDisplay) {
        turnMotorCont = new CANSparkMax(turnId, MotorType.kBrushless);
        driveMotorCont = new CANSparkMax(driveId, MotorType.kBrushless);
        testEncoder = new Encoder(encoderId);
        testEncoder.encoderInit();
        PIDInit();
        encoderOffset = offset;

        //currentDisplayField = currentDisplay;
    }

    void setDrive(double driveSpeed, Rotation2d turnAngle, Boolean print) {
       // System.out.println("turn angle rad " + turnAngle.getRadians());
        targetAngle = (int) ((turnAngle.getRadians() + Math.PI)/ (2 * Math.PI) * testEncoder.encoderMax); //convert the radians to encoder tick
        setModulePower(1, targetAngle, print);
        //currentDisplayField.setInteger(getEncoderAngle());
    }

    int getEncoderAngle() {
        return testEncoder.getAverageValue() - encoderOffset; //we get it from this so if the encoder changes it wont effect this class. 
    }
    double getEncoderAngleRadians() //converts the encoder ticks to radians
    {
       // System.out.println("encoder rad " + (((( (double) getEncoderAngle()/(testEncoder.encoderMax)) * Math.PI * 2)) - Math.PI));    
        return ((( (double) getEncoderAngle()/(testEncoder.encoderMax)) * Math.PI * 2)) -Math.PI;
    }

    void PIDInit() {
        pidController.reset();
        pidController.enableContinuousInput(0, 4096);
        pidController.setTolerance(60);
        pidController.setD(0);
        pidController.setI(0);
        pidController.setP(0.0005);
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
        pidController.setTolerance(tol);
    }

    void setModulePower(double powerMulti, int targetAngle, Boolean print){
        //targetAngle = targetAngle + encoderOffset;
        //System.out.println("tar: " + targetAngle + "  enco: " + getEncoderAngle() + " PID d: " + pidController.getD() + "PID Tol: " + pidController.getPositionTolerance());
        //System.out.println("Power Final" + pidController.calculate(getEncoderAngle(), targetAngle) + " PID Error: " + pidController.getPositionError());
        double calc = pidController.calculate(getEncoderAngle(), targetAngle);
        if(print){
        //System.out.println("PID Error " + pidController.getPositionError() + " PID Current " + getEncoderAngle() + " PID Target " + targetAngle);
        //System.out.println("Power Final: " + calc);
        }
        double powerFinal = (calc * powerMulti);
        if (!pidController.atSetpoint()) {
           turnMotorCont.set(-1 * MathUtil.clamp(powerFinal, -0.2, 0.2));
        } else {
           turnMotorCont.set(0);
        }
    }

}
