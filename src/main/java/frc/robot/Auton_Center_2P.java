package frc.robot;

public class Auton_Center_2P extends Auton {

    public Auton_Center_2P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
    }
    //-y = forward
    
    public void run(double time){
        if(time < 1){ //Turn on shooter, wait for rev up
            driveBase.driveSet(0, -1, 0, 0);
            shooter.setTargetRPS(80);
        }else if (time < 2.2){ //Feed preload into shooter, wait for piece to leave
            shooter.setTargetRPS(80);
            intake.setPower(0.8);
        }else if(time < 3){//
            shooter.setPower(0);
            intake.setPower(0);
        }else if (time < 4){ //Turn off Shooter, Keep intake running, but Check if we have a Piece, Start moving to next piece
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0, -1, 0, 1);
        }else if (time < 5){ //Stop to pick up piece
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0, 0, 0, 0);
        }else if(time < 6.3){ //drive back
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0, 1, 0, 0.7);
        }else if(time < 8){
            shooter.setTargetRPS(80);
            driveBase.driveSet(0, 0, 0, 0);
        }else if(time < 9){ //shoot
            shooter.setTargetRPS(80);
            driveBase.driveSet(0, 0, 0, 0);
            intake.setPower(0.8);
        }else{
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0); 
        }
    }
}
