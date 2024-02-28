package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Auton {
    protected SwerveDrive driveBase;
    protected Intake intake;
    protected Shooter shooter;
    protected boolean isBlue;

    public Auton(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue){
        this.driveBase = driveBase;
        this.shooter = shooter;
        this.intake = intake;
        this.isBlue = isBlue;

    }

    public void run(double time){
    }
}
