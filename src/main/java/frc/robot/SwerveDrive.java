package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

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
    SwerveModule testMod;
    SwerveDriveKinematics driveLocation;

    AHRS gyro = new AHRS(SPI.Port.kMXP);

    double powerMulti = 0.6;

    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, tol; //the PID loop doubles 
    ShuffleboardTab driveTab = Shuffleboard.getTab("Drive3");

    //adding the current encoder values to shuffle board. TODO add a way to change PID loop from shuffle board
    GenericEntry fl_currentAngleField = driveTab.add("Front Left Current", 0).getEntry();
    GenericEntry fr_currentAngleField = driveTab.add("Front Right Current", 0).getEntry();
    GenericEntry bl_currentAngleField = driveTab.add("Back Left Current", 0).getEntry();
    GenericEntry br_currentAngleField = driveTab.add("Back Right Current", 0).getEntry();

    GenericEntry fl_currentAngleField_offset = driveTab.add("Front Left Current Offset", 0).getEntry();
    GenericEntry fr_currentAngleField_offset = driveTab.add("Front Right Current Offset", 0).getEntry();
    GenericEntry bl_currentAngleField_offset = driveTab.add("Back Left Current Offset", 0).getEntry();
    GenericEntry br_currentAngleField_offset = driveTab.add("Back Right Current Offset", 0).getEntry();

    GenericEntry fl_currentAngleField_target = driveTab.add("Front Left Current Target", 0).getEntry();
    GenericEntry fr_currentAngleField_target = driveTab.add("Front Right Current Target", 0).getEntry();
    GenericEntry bl_currentAngleField_target = driveTab.add("Back Left Current Target", 0).getEntry();
    GenericEntry br_currentAngleField_target = driveTab.add("Back Right Current Target", 0).getEntry();


    SwerveDrive() {
        gyro.reset();
        frontRight = new SwerveModule(11, 10, 3, 1421, fr_currentAngleField);
        frontLeft = new SwerveModule(16, 19, 0, 1846, fl_currentAngleField);
        backRight = new SwerveModule(17, 12, 2, 0, br_currentAngleField);
        backLeft = new SwerveModule(18, 14, 1, 1839, bl_currentAngleField);
        //testMod = new SwerveModule(5,14,0,0, bl_currentAngleField);
        //positive x means moving forward aka towoard the front of the robot, positive y means moving to the left
        Translation2d frontRightLocation = new Translation2d(-119, -119); //119, 103
        Translation2d frontLeftLocation = new Translation2d(-119, 119);
        Translation2d backRightLocation = new Translation2d(119, -119);
        Translation2d backLeftLocation = new Translation2d(119, 119);
        driveLocation = new SwerveDriveKinematics(
                frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);
    }

    void driveSet(double rotatX, double transY, double transX, double powerMulti) {

       ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
            transX, transY, rotatX/275, Rotation2d.fromDegrees(gyro.getAngle()+90));

            //ChassisSpeeds speeds = new ChassisSpeeds(
            //transX, transY, rotatX/275);

            //System.out.println("X: " + speeds.vxMetersPerSecond + "Y: " + speeds.vyMetersPerSecond);

        // Now use this in our kinematics
        SwerveModuleState[] moduleStates = driveLocation.toSwerveModuleStates(speeds);

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

        //var backRightOptimized = SwerveModuleState.optimize(backRightState, new Rotation2d(testMod.getEncoderAngleRadians()));

        Rotation2d testAngle = Rotation2d.fromDegrees(0);

        frontLeft.setDrive(frontLeftOptimized.speedMetersPerSecond,frontLeftOptimized.angle, powerMulti, false);
        frontRight.setDrive(-frontRightOptimized.speedMetersPerSecond, frontRightOptimized.angle, powerMulti,false);
        backLeft.setDrive(backLeftOptimized.speedMetersPerSecond, backLeftOptimized.angle, powerMulti,false); //negitive due to issue. TODO Fix it
        backRight.setDrive(-backRightOptimized.speedMetersPerSecond,backRightOptimized.angle , powerMulti,true); //negitive due to issue. TODO Fix it

        //testMod.setDrive(backRightOptimized.speedMetersPerSecond, backRightOptimized.angle, powerMulti, false);

        //frontLeft.setDrive(frontLeftOptimized.speedMetersPerSecond,testAngle, powerMulti, false);
        //frontRight.setDrive(frontRightOptimized.speedMetersPerSecond, testAngle, powerMulti,false);
        //backLeft.setDrive(backLeftOptimized.speedMetersPerSecond, testAngle, powerMulti,false); //negitive due to issue. TODO Fix it
        //backRight.setDrive(backRightOptimized.speedMetersPerSecond,testAngle , powerMulti,true); //negitive due to issue. TODO Fix it

        addDataToShuffle();
        // fl_currentAngleField_offset.setDouble(frontLeftOptimized.angle.getRadians());
        //fr_currentAngleField_offset.setDouble(frontRightOptimized.angle.getRadians());
        //bl_currentAngleField_offset.setDouble(backLeftOptimized.angle.getRadians());
        br_currentAngleField_offset.setDouble(backRightOptimized.angle.getRadians());
    }

    //Adding the data from the swerve modules to the shuffle board
    void addDataToShuffle()
    {
      //System.out.println(frontRight.getEncoderAngleRadians());
      //fl_currentAngleField.setDouble(testMod.getEncoderAngleRadians());
      //fr_currentAngleField.setDouble(frontRight.getEncoderAngleRadians());
      //bl_currentAngleField.setDouble(backLeft.getEncoderAngleRadians());
      //br_currentAngleField.setDouble(backRight.getEncoderAngleRadians());


      //fl_currentAngleField_target.setDouble(frontLeft.targetAngle);
      //fr_currentAngleField_target.setInteger(frontRight.targetAngle);
      //bl_currentAngleField_target.setInteger(backLeft.targetAngle);
      //br_currentAngleField_target.setInteger(testMod.getEncoderAngle());
      
    }

    void resetGyro()
    {
        gyro.reset();
    }
}
