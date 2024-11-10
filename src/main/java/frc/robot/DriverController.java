package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;

public class DriverController {

    // We create a unique class for the controller as it allows us to run the swerve
    // code from a different place then robot.java.
    // I do not believe that it is required to do this in its own class.

    // Creating variables
    XboxController controller;
    SwerveDrive drive;

    // Used for "gear" system, allowing the driver to speed up and slow down without
    // needing to hold the joy sticks at a precise angle
    double powerMulti = 1;
    double gearAmount = 0.5;

    // This is the init part, passing the swerve drive in so it can call it later
    DriverController(SwerveDrive drive) {
        controller = new XboxController(0); // Since this is the drive controller, we set it to port Zero (first one plugged in)
        this.drive = drive;
    }

    void run() {
        double transX = -controller.getLeftX(); // negative as left and right were reversed.
        double transY = controller.getLeftY();
        double rotatX = controller.getRightX();

        drive.driveSet(rotatX, transY, -transX, powerMulti); // this is where the swerve drive code is called.

        //Reset the gyro. Check swerve drive code for more details
        if (controller.getYButton()) {
            drive.resetGyro();
        }

        //This is the gear system. Holding the left bumper sets it to half speed, right bumper to full speed.
        //Another way to program this is to have it be a press and not a hold. Ask the driver what way they like it
        if (controller.getLeftBumper()) {
            powerMulti = 0.5;
        } else if (controller.getRightBumper()) {
            powerMulti = 2.3;
        } else {
            powerMulti = 1.0;
        }

    }
}
