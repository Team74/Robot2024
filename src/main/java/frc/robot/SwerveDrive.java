package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class SwerveDrive {
    SwerveModule frontRight;
    SwerveModule frontLeft;
    SwerveModule backRight;
    SwerveModule backLeft;
    SwerveDriveKinematics driveLocation;

    double powerMulti = 1;

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, tol; //the PID loop doubles 
    ShuffleboardTab driveTab = Shuffleboard.getTab("Drive2");

    //adding the current encoder values to shuffle board. TODO add a way to change PID loop from shuffle board
    GenericEntry fl_currentAngleField = driveTab.add("Front Left Current", 0).getEntry();
    GenericEntry fr_currentAngleField = driveTab.add("Front Right Current", 0).getEntry();
    GenericEntry bl_currentAngleField = driveTab.add("Back Left Current", 0).getEntry();
    GenericEntry br_currentAngleField = driveTab.add("Back Right Current", 0).getEntry();

    SwerveDrive() {
        frontRight = new SwerveModule(5, 14, 0, 2925, fr_currentAngleField);
        frontLeft = new SwerveModule(44, 4, 3, 3713, fl_currentAngleField);
        backRight = new SwerveModule(12, 15, 1, 2939-2048, br_currentAngleField);
        backLeft = new SwerveModule(11, 3, 2, 1008, bl_currentAngleField);
        Translation2d frontRightLocation = new Translation2d(119, 119); //119, 103
        Translation2d frontLeftLocation = new Translation2d(119, -119);
        Translation2d backRightLocation = new Translation2d(-119, 119);
        Translation2d backLeftLocation = new Translation2d(-119, -119);
        driveLocation = new SwerveDriveKinematics(
                frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);
    }

    void driveSet(double rotatX, double transY, double transX) {
        ChassisSpeeds wheelSpeed = new ChassisSpeeds(transY, transX, rotatX/150); //we are dividing the value as it is way to fast otherwise
        SwerveModuleState[] moduleStates = driveLocation.toSwerveModuleStates(wheelSpeed);
        // Front left module state
        SwerveModuleState frontLeftState = moduleStates[0];

        // Front right module state
        SwerveModuleState frontRightState = moduleStates[1];

        // Back left module state
        SwerveModuleState backLeftState = moduleStates[2];

        // Back right module state
        SwerveModuleState backRightState = moduleStates[3];

        var frontLeftOptimized = SwerveModuleState.optimize(frontLeftState, new Rotation2d(frontLeft.getEncoderAngleRadians()));
        var frontRightOptimized = SwerveModuleState.optimize(frontRightState, new Rotation2d(frontRight.getEncoderAngleRadians()));
        var backLeftOptimized = SwerveModuleState.optimize(backLeftState, new Rotation2d(backLeft.getEncoderAngleRadians()));
        var backRightOptimized = SwerveModuleState.optimize(backRightState, new Rotation2d(backRight.getEncoderAngleRadians()));

        frontLeft.setDrive(frontLeftOptimized.speedMetersPerSecond, frontLeftOptimized.angle, powerMulti, false);
        frontRight.setDrive(frontRightOptimized.speedMetersPerSecond, frontRightOptimized.angle, powerMulti,false);
        backLeft.setDrive(-backLeftOptimized.speedMetersPerSecond, backLeftOptimized.angle, powerMulti,false); //negitive due to issue. TODO Fix it
        backRight.setDrive(-backRightOptimized.speedMetersPerSecond, backRightOptimized.angle, powerMulti,true); //negitive due to issue. TODO Fix it
        
        addDataToShuffle();
    }

    //Adding the data from the swerve modules to the shuffle board
    void addDataToShuffle()
    {
        fl_currentAngleField.setInteger(frontLeft.getEncoderAngle());
        fr_currentAngleField.setInteger(frontRight.getEncoderAngle());
        bl_currentAngleField.setInteger(backLeft.getEncoderAngle());
        br_currentAngleField.setInteger(backRight.getEncoderAngle());
    }
}
