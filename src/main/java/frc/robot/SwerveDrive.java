package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class SwerveDrive {

    //this is the class for the swerve drive, meaning all 4 modules
    //Some code I am scared to delete right now. If you are in the future and tested everything, then you can prob delete the commented out code
    //For whoever is reading this, I am sorry this is not very readable. My first time writing swerve code under time pressure lol

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
    //TBH this was not helpful after we where done testing
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


    //Init
    SwerveDrive() {
        gyro.reset();
        //If the CAN IDs changed, then you would have to change them here.
        frontRight = new SwerveModule(11, 10, 3, 1351, fr_currentAngleField);//-
        frontLeft = new SwerveModule(16, 19, 0, 1956, fl_currentAngleField);//+
        backRight = new SwerveModule(17, 12, 2, 0, br_currentAngleField);
        backLeft = new SwerveModule(18, 14, 1, 1839, bl_currentAngleField);

        //positive x means moving forward aka toward the front of the robot, positive y means moving to the left
        //Even tho this robot is 119 by 103, making it square does not affect anything
        Translation2d frontRightLocation = new Translation2d(-119, -119); //119, 103
        Translation2d frontLeftLocation = new Translation2d(-119, 119);
        Translation2d backRightLocation = new Translation2d(119, -119);
        Translation2d backLeftLocation = new Translation2d(119, 119);
        //This does magic
        driveLocation = new SwerveDriveKinematics(
                frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);
    }

    double lastGyroAngle = 0.0;
    void driveSet(double rotatX, double transY, double transX, double powerMulti) {

       ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
            transX, transY, rotatX/275, Rotation2d.fromDegrees(gyro.getAngle()+90.0)); //idk why we divide by 275, prob was too fast or something

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

        //This makes it never move more then 90 degrees, as if it needs to move more, it can reverse the power and move shorter distance
        var frontLeftOptimized = SwerveModuleState.optimize(frontLeftState, new Rotation2d(frontLeft.getEncoderAngleRadians()));
        var frontRightOptimized = SwerveModuleState.optimize(frontRightState, new Rotation2d(frontRight.getEncoderAngleRadians()));
        var backLeftOptimized = SwerveModuleState.optimize(backLeftState, new Rotation2d(backLeft.getEncoderAngleRadians()));
        var backRightOptimized = SwerveModuleState.optimize(backRightState, new Rotation2d(backRight.getEncoderAngleRadians()));

        //var backRightOptimized = SwerveModuleState.optimize(backRightState, new Rotation2d(testMod.getEncoderAngleRadians()));

        Rotation2d testAngle = Rotation2d.fromDegrees(0);

        //Make them move
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
        //System.out.println("FR: " + frontRight.powerFinal + " FL: " + frontLeft.powerFinal 
          //  + " BR: " + backRight.powerFinal + " BL: " + backLeft.powerFinal);
        
        
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
        lastGyroAngle = 0.0;
    }

    //This was going to make it so you could reset the gyro while not facing the driver station. Again, ran out of time to test it 
    void resetGyroWithOffset(double offSet) //TODO make this do something
    {
        gyro.reset();
    }

    //This is for Auton. It will try to correct for the natural drift of the robot
    void driveSetWithGyro(double rotatX, double transY, double transX, double powerMulti) {

        //If the last gyro angle set is less or greater then the deadzone, rotate slowly to the fix it
        if((0.85 - lastGyroAngle > gyro.getAngle() || gyro.getAngle() > 0.85 + lastGyroAngle) && rotatX == 0)
        {
            rotatX = (gyro.getAngle() - lastGyroAngle) * -0.065;
        
        //reset the last gyro angle
        }else if(Math.abs(rotatX) > 0.05){
            lastGyroAngle = gyro.getAngle();
        }

        //After this, it is the same as Drive Set
        
        //System.out.println("Rotate x " + rotatX + "Last Gyro Angle " + lastGyroAngle + " Current Gyro " + gyro.getAngle());

       ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
            transX, transY, rotatX/275, Rotation2d.fromDegrees(gyro.getAngle()+90.0));

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
        //System.out.println("FR: " + frontRight.powerFinal + " FL: " + frontLeft.powerFinal 
          //  + " BR: " + backRight.powerFinal + " BL: " + backLeft.powerFinal);

        
    }

}
