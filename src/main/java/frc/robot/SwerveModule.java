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
    Encoder testEncoder;
    PIDController anglePIDController = new PIDController(0, 0, 0.05);
    PIDController drivePIDController = new PIDController(0, 0, 0.05);
    double lastTarget;
    int targetAngle, encoderOffset;

    CANSparkMax driveMotorCont;
    CANSparkMax turnMotorCont;
    Double targetSpeed = 0.0;

    SparkPIDController drivePID;

    GenericEntry currentDisplayField;

    SwerveModule(int driveId, int turnId, int encoderId, int offset, GenericEntry currentDisplay) {
        turnMotorCont = new CANSparkMax(turnId, MotorType.kBrushless);
        driveMotorCont = new CANSparkMax(driveId, MotorType.kBrushless);
        testEncoder = new Encoder(encoderId);
        testEncoder.encoderInit();
        PIDInit();
        encoderOffset = offset;
    }

    void setDrive(double driveSpeed, Rotation2d turnAngle, double powerMulti, Boolean print) {
        targetAngle = (int) ((turnAngle.getRadians() + Math.PI)/ (2 * Math.PI) * testEncoder.encoderMax); //convert the radians to encoder tick
        setAngleMotorPower(targetAngle, print); //set the motor power 

        targetSpeed = MathUtil.clamp(driveSpeed,-0.95,0.95) * 5600 * 0.1; //driver speed gives -1 to 1, multiply by encoder ticks, then speed multiplier
        drivePID.setReference(targetSpeed * powerMulti, CANSparkMax.ControlType.kVelocity); //set reference as it is velocity not position 

        //if you want to print something to console, do it here.
        if(print){
        
        }
    }

    int getEncoderAngle() {
        //System.out.println("Test 1: " + testEncoder.getAverageValue());
        System.out.println("offset" + (encoderOffset));
        System.out.println("Final" + (testEncoder.getAverageValue() - encoderOffset));
        return testEncoder.getAverageValue() - encoderOffset; //we get it from this so if the encoder changes it wont effect this class. 
    }
    double getEncoderAngleRadians() //converts the encoder ticks to radians
    { 
        return ((( (double) getEncoderAngle()/(testEncoder.encoderMax)) * Math.PI * 2)) -Math.PI;
    }

    void PIDInit() {

        //the angle PID is on the rio, so we have to set everything manually
        anglePIDController.reset();
        anglePIDController.enableContinuousInput(0, 4096); //this means that when it hits 0, it will go to 4096. It acts like a circle
        anglePIDController.setTolerance(100);
        anglePIDController.setD(0.000005);
        anglePIDController.setI(0.0);
        anglePIDController.setP(0.0005);

        driveMotorCont.restoreFactoryDefaults(); //clear the previous PID Loop. This is bc the drive motor PID is on the controller
        drivePID = driveMotorCont.getPIDController(); 

        //PID for the drive controller
        kP = 0.000000; 
        kI = 0;
        kD = 0.000000; 
        kIz = 0; 
        kFF = 0.001050; 
        kMaxOutput = 0.95; 
        kMinOutput = -0.95;
 
        drivePID.setP(kP);
        drivePID.setI(kI);
        drivePID.setD(kD);
        drivePID.setIZone(kIz);
        drivePID.setFF(kFF);
        drivePID.setOutputRange(kMinOutput, kMaxOutput);
    }

    //These are called from the Swerve Drive Class
    void setPIDP(double p, double driveP){
        anglePIDController.setP(p);
        drivePIDController.setP(driveP);
    }
     void setPIDI(double i, double driveI){
        anglePIDController.setI(i);
        drivePIDController.setI(driveI);
    }
     void setPIDD(double d, double driveD){
        anglePIDController.setD(d);
        drivePIDController.setD(driveD);
    }
    void setPIDTol(double tol, double driveTol){ //tolerance
        anglePIDController.setTolerance(tol);
        drivePIDController.setTolerance(driveTol);
    }

    void setAngleMotorPower(int targetAngle, Boolean print){

        //dont call calc 2 times in one cycle, it does not like it 
        double calc = anglePIDController.calculate(getEncoderAngle(), targetAngle); //get the pid power
        double powerFinal = (calc); //power multi is the "gear" system, speed up / slow down
        //System.out.println("Test 2: " + powerFinal);
        //we do this as we dont want to strain motors or batteries when we are at the setpoint. If we don't do this it will set the power to a very small number
        if (!anglePIDController.atSetpoint()) {
           turnMotorCont.set(1 * MathUtil.clamp(powerFinal, -0.2, 0.2));
        } else {
           turnMotorCont.set(0);
        }

        //if you want to print anything to console, put it here
        if(print){
            
        }
    }

}
