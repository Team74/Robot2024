package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

public class DriverController {
    XboxController controller;
    SwerveDrive drive;
    
    
    DriverController (SwerveDrive drive){
        controller = new XboxController(0);
        this.drive = drive;
    }
        void run (){
        double transX = -controller.getLeftX();
        double transY = controller.getLeftY();
        double rotatX = controller.getRightX();
        drive.driveSet(rotatX,transY,transX);
    }
}
