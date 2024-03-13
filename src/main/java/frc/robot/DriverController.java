package frc.robot;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.XboxController;

public class DriverController {

    //We create a unique class for the controller as it allows us to run the swerve code from a different place then robot.java.
    //I do not belive that it is required to do this in its own class. 

    XboxController controller;
    SwerveDrive drive;
    double powerMulti = 1;
    double gearAmount = 0.5;
    
    //This is the init part, passing the swerve drive in so it can call it later
    DriverController (SwerveDrive drive){
        controller = new XboxController(0); //Since this is the drive controller, we set it to port one
        this.drive = drive;
    }
        void run (){
        double transX = -controller.getLeftX(); //negative as left and right were reversed. 
        double transY = controller.getLeftY();
        double rotatX = controller.getRightX(); 

        //TODO make values close to 1 be 1, thus holding forward on the joystick will go perfectly strait. 
        if(transX > 0.95){

        }else if(transX < -0.95){

        }else if(transY > 0.95){

        }else if(transY < -0.95){

        }

        drive.driveSet(rotatX,transY,-transX, powerMulti); //this is where the swerve drive code is called. 

        if(controller.getYButton()){
            drive.resetGyro();
        }

        if(controller.getLeftBumper()){
            powerMulti = 0.5;
        }else if(controller.getRightBumper()){
            powerMulti = 2.3;
        }else{
            powerMulti = 1.0;
        }

    }
}
