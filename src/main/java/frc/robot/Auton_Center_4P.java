package frc.robot;

public class Auton_Center_4P extends Auton {

    public Auton_Center_4P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
    }

    public void run(double time){
        if(time > 10){

        }else if(time < 7){ // drvie to the left note
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0, 1, 1, 1.5);
        }else if (time < 8){
            driveBase.driveSet(0, 0, 0, 0);
        }else{
            
        }
    }
}
