package frc.robot;

public class Auton_AmpSide_2P extends Auton {

    public Auton_AmpSide_2P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
    }

    //NOTE: +X means move forward, +Y means move to the left. 
    //TODO make it work on the blue side 
    //2 Piece Auto from Amp side position. Start with robot hugging the Speaker
    public void run(double time){
        if(time < 0.7){ //Turn on shooter, wait for rev up
            shooter.setTargetRPS(80);
        }else if (time < 2){ //Feed preload into shooter, wait for piece to leave
            shooter.setTargetRPS(80);
            intake.setPower(0.8);
        }else if (time < 3){ //Turn off Shooter, Keep intake running, but Check if we have a Piece, Start moving to next piece
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(1, 0.3, 0.5, 1.5);
        }else if (time < 4){ // Move to next piece
            driveBase.driveSet(0, 0, 1, 1.5);
        }else if (time < 4.5){ // Stop for a sec, don't want to make robot pop a wheelie or make it rotate a little, ect
            driveBase.driveSet(0, 0, 0, 0);
        }else if(time < 5.5){ // Start going back to Speaker
            driveBase.driveSet(0, 0, -1, 1.5);
        }else if (time < 7){ //Move back to Speaker, new Angle of direction
            driveBase.driveSet(-1, -0.3, -0.5, 1.5);
        }else if (time < 7.5){ //rotate back to spot
            driveBase.driveSet(-0.5, 0, 0, 1);
            shooter.setTargetRPS(80);
        }else if (time < 9){ //shoot
            shooter.setTargetRPS(80);
            intake.setPower(0.8);
        }else{ //Auton Done, stop everything
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0);
        }//TODO make the robot face the driver station and reset gyro
    }
    
}
