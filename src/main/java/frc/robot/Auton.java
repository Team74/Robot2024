package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Auton {
    //This is the main auton class. It has the parts that every auton has
    protected SwerveDrive driveBase; //Wheels Class Reference 
    protected Intake intake; //Intake Class Reference 
    protected Shooter shooter; //Shooter Class Reference 
    protected boolean isBlue; // So we can flip left/right

    //Auto Init
    public Auton(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue){
        this.driveBase = driveBase;
        this.shooter = shooter;
        this.intake = intake;
        this.isBlue = isBlue;

    }

    //Needed
    public void run(double time){
    }
}
