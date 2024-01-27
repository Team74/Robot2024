package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;

public class SwerveDrive {
    SwerveModule frontRight;
    SwerveModule frontLeft;
    SwerveModule backRight;
    SwerveModule backLeft;
    SwerveDriveKinematics driveLocation;

    SwerveDrive() {
        frontRight = new SwerveModule(5, 14, 0);
        frontLeft = new SwerveModule(44, 4, 3);
        backRight = new SwerveModule(12, 15, 1);
        backLeft = new SwerveModule(11, 3, 2);
        Translation2d frontRightLocation = new Translation2d(119, -103);
        Translation2d frontLeftLocation = new Translation2d(119, 103);
        Translation2d backRightLocation = new Translation2d(-119, -103);
        Translation2d backLeftLocation = new Translation2d(-119, 103);
        driveLocation = new SwerveDriveKinematics(
                frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation);
    }

    void driveSet(double rotatX, double transY, double transX) {
        ChassisSpeeds wheelSpeed = new ChassisSpeeds(transY, transX, rotatX);
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

        frontLeft.setDrive(frontLeftOptimized.speedMetersPerSecond, frontLeftOptimized.angle);
    }
}
