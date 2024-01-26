package frc.robot;

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
        frontRight = new SwerveModule(0, 0, 0);
        frontLeft = new SwerveModule(0, 0, 0);
        backRight = new SwerveModule(0, 0, 0);
        backLeft = new SwerveModule(0, 0, 0);
        Translation2d frontRightLocation = new Translation2d(119,-103);
        Translation2d frontLeftLocation = new Translation2d(119,103);
        Translation2d backRightLocation = new Translation2d(-119,-103);
        Translation2d backLeftLocation = new Translation2d(-119,103);
        driveLocation = new SwerveDriveKinematics(
            frontLeftLocation, frontRightLocation, backLeftLocation, backRightLocation
        );
    }

    void driveSet(double rotatX, double transY, double transX) {
        ChassisSpeeds wheelSpeed = new ChassisSpeeds(transY, transX, rotatX);
        SwerveModuleState[] moduleStates = driveLocation.toSwerveModuleStates(wheelSpeed);
    }
}
