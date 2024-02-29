package frc.robot;

public class Auton_AmpSide_2P extends Auton {

    public Auton_AmpSide_2P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
    }

    //TODO make it work on the blue side 
    //2 Piece Auto from Amp side position. Start with robot hugging the Speaker
    public void run(double time){
        if(time < 0.3){ //Turn on shooter, wait for rev up
            shooter.setSpeed(80);
        }else if (time < 0.7){ //Feed preload into shooter, wait for piece to leave
            shooter.setSpeed(80);
            intake.setPower(0.8);
        }else if (time < 1){ //Turn off Shooter, Keep intake running, but Check if we have a Piece, Start moving to next piece
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(1, -0.3, 0.5, 1.5);
        }else if (time < 1.3){ // Move to next piece
            driveBase.driveSet(0, 0, 1, 1.5);
        }else if (time < 1.7){ // Stop for a sec, don't want to make robot pop a wheelie or make it rotate a little, ect
            driveBase.driveSet(0, 0, 0, 0);
        }else if(time < 2.3){ // Start going back to Speaker
            driveBase.driveSet(0, 0, -1, 1.5);
        }else if (time < 2.8){ //Move back to Speaker, new Angle of direction
            driveBase.driveSet(-1, 0.3, -0.5, 1.5);
        }else if(time < 3.1){ //Start Shooter TODO make it hit speaker then rotate to the correct angle
            shooter.setSpeed(80);
        }else if (time < 5){ //Feed Piece
            shooter.setSpeed(80);
            intake.setPower(0.8);
        }else{ //Auton Done, stop everything
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0);
        }//TODO make the robot face the driver station and reset gyro
    }
    
}
