package frc.robot;

import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class Intake {
    
    //This is the class that will handle intake components. This intake is a double bar style, with a upper and lower bar that spin

    //Creating two CAN spark classes
    CANSparkMax intakeTop, intakeBottom;

    //Creating 2 limit Sensors, which in this case are range sensors that give an output at a certain range.
    //One is a close one by the top of the intake, and the other is a far one at the bottom of the intake
    DigitalInput limitSensor = new DigitalInput(8);
    DigitalInput limitSensorFar = new DigitalInput(9);

    //This is the setup function. It takes in the IDs of the 2 CAN spark maxes of the two motors
    //It then initializes the class with these IDs and the type of motor
    Intake(int intakeTopID, int intakeBottomID) {
        intakeTop = new CANSparkMax(intakeTopID, MotorType.kBrushless);
        intakeBottom = new CANSparkMax(intakeBottomID, MotorType.kBrushless);
    }

    //This function sets the power of the two motors at the same time
    void setPower(double power) {
        intakeTop.set(power);
        intakeBottom.set(power);
    }

    //This function sets the power until a piece is detected. Once detected, it will stop to avoid over driving 
    void setPowerUntilPiece(double power) {
        if (limitSensor.get()) {
            intakeTop.set(power);
            intakeBottom.set(power);
        } else {
            intakeTop.set(0);
            intakeBottom.set(0);
        }
    }

    //The sensor we used is naturally true, meaning it returns true when a piece is not in
    //Returns if the piece is at the top. Used to stop over driving
    Boolean hasPiece() {
        return !limitSensor.get();
    }

    //Returns if the piece is at the bottom. Used to know is somewhat secured. 
    Boolean hasPieceFar() {
        return !limitSensorFar.get();
    }
}
