package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;
import edu.wpi.first.wpilibj.CAN;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SwerveModule {

    ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");
    GenericEntry kpField = driveTab.add("Kp Field", 0).getEntry();
    GenericEntry kiField = driveTab.add("Ki Field", 0).getEntry();
    GenericEntry kdField = driveTab.add("Kd Field", 0).getEntry();
    GenericEntry targetAngleField = driveTab.add("Target", 0).getEntry();
    GenericEntry currentAngle = driveTab.add("Current", 0).getEntry();
    GenericEntry tolerance = driveTab.add("Tolerance", 100).getEntry();
    GenericEntry posError = driveTab.add("Position Error", 0).getEntry();
    GenericEntry powerField = driveTab.add("Power", 0).getEntry();

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, tol;
    AnalogInput encoder;
    PIDController pidController = new PIDController(0, 0, 0);
    double lastTarget, targetAngle;

    CANSparkMax driveMotorCont;
    CANSparkMax turnMotorCont;

    SwerveModule(int driveId, int turnId, int encoderId) {
        turnMotorCont = new CANSparkMax(turnId, MotorType.kBrushless);
        driveMotorCont = new CANSparkMax(driveId, MotorType.kBrushless);
        encoder = new AnalogInput(encoderId);
        encoder.setAverageBits(4);
        PIDInit();
    }

    void setDrive(double driveSpeed) {
        driveMotorCont.set(driveSpeed);
    }

    int getEncoderAngle(){
        return encoder.getAverageValue();
    }

    void PIDInit()
    {
        pidController.enableContinuousInput(0,4096);
        pidController.setTolerance(100);
    }

    void setPIDLoop(){
        // read PID coefficients from SmartDashboard
    double p = kpField.getDouble(0);
    double i = kiField.getDouble(0);
    double d = kdField.getDouble(0);
    double t = tolerance.getDouble(0);
    double tar = targetAngleField.getDouble(0);

    currentAngle.setDouble(getEncoderAngle());
    posError.setDouble(pidController.getPositionError());

    // if PID coefficients on SmartDashboard have changed, write new values to controller
    if((p != kP)) { pidController.setP(p); kP = p; }
    if((i != kI)) { pidController.setI(i); kI = i; }
    if((d != kD)) { pidController.setD(d); kD = d; }
    if((t != tol)) { pidController.setTolerance(t); tol = t; }
    if((tar != lastTarget)) {
        pidController.calculate(getEncoderAngle(), tar);
        double posErrorAbs = Math.abs(pidController.getPositionError());
         if(posErrorAbs > 1024)
    {
        targetAngle = tar + 2048;
        targetAngle = targetAngle % 4096;
    }else{
        targetAngle = tar;
    }
     }
    
<<<<<<< HEAD
    
=======
    //double targetAngle = targetAngleField.getDouble(0);
    
    double power = pidController.calculate(getEncoderAngle(), targetAngle);
    powerField.setDouble(power);
>>>>>>> 6bc250405bbf7486e8099b18330997cc323e813f
        if (!pidController.atSetpoint()) {
           turnMotorCont.set(-1*MathUtil.clamp(power, -0.2, 0.2));
        } else {
            turnMotorCont.set(0);
        }
    }
}
