package frc.robot;

public class Auton_SourceSide_2P extends Auton{

    double blueDirection = 1.0;
    public Auton_SourceSide_2P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
        if(isBlue){
            blueDirection = -1.0;
        }else{
            blueDirection = 1.0;
        }
    }

    //2 Piece Auto from Amp side position. Start with robot hugging the Speaker
    public void run(double time){
        if(time < 0.5){ //Turn on shooter
            driveBase.driveSetWithGyro(0, -1, 0, 0);
            shooter.setTargetRPS(80);
        }else if (time < 1){ //spin to speaker
            shooter.setTargetRPS(80);
             driveBase.driveSetWithGyro(0.3, 0, 0, 0.7);
        }else if (time < 2){ //shoot
            shooter.setTargetRPS(80);
            intake.setPower(0.8);
            driveBase.driveSetWithGyro(0, 0, 0, 0.7);
        }else if (time < 4){ //stop motors, spin back
            shooter.setPower(0);
            intake.setPowerUntilPiece(0);
           driveBase.driveSetWithGyro(-0.3, 0, 0, 0.7);
        }else if (time < 4.5){ // drive to next piece
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0,-1, 0, 0.7);
        }else if (time < 6){//stop for a sec
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,0, 0, 0.0);
        }else if (time < 7){ // drive back
            shooter.setTargetRPS(90);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0, 1, 0, 0.7);
        }else if (time < 8){// turn on motors
            shooter.setTargetRPS(90);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0,0,0,0);
        }else if(time < 8.5){ //spin to speaker
            shooter.setTargetRPS(90);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0.3,0,0,0);
        }else if(time < 10){ // shoot
            shooter.setTargetRPS(90);
            intake.setPower(0.8);
        }else{
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0);
        }
}
    
}
