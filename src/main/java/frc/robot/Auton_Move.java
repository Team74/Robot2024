package frc.robot;

public class Auton_Move extends Auton {

    public Auton_Move(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
    }

    //Shoot pre load then move away. Good for when teammates want to do things and there is a fear of a collision
    public void run(double time){
        if(time < 0.3){ //Start Shooter, wait for rev up
            driveBase.driveSet(0, 0, 0, 0);
            shooter.setTargetRPS(80);
        }else if (time < 0.7){ //Feed piece into Shooter
            shooter.setTargetRPS(80);
            intake.setPower(0.8);
        }else if (time < 1){ //Turn off Shooter and Intake, move away
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0);
        }else if(time < 13){//Wait for a while, then drive out
            driveBase.driveSet(0, 0, 1, 1);
        }else{ //Auton Done, stop motors
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0);
        }
    }
    
}
