package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class DriverController {

    //We create a unique class for the controller as it allows us to run the swerve code from a differnt place then robot.java.
    //I do not belive that it is required to do this in its own class. 

    XboxController controller;
    SwerveDrive drive;
    
    //This is the init part, passing the swerve drive in so it can call it later
    DriverController (SwerveDrive drive){
        controller = new XboxController(0); //Since this is the drive controller, we set it to port one
        this.drive = drive;
    }
        void run (){
        double transX = -controller.getLeftX(); //negitive as left and right were reversed. 
        double transY = controller.getLeftY();
        double rotatX = -controller.getRightX(); //Same as above commet
        drive.driveSet(rotatX,transY,transX); //this is where the swerve drive code is called. 
    }
}
