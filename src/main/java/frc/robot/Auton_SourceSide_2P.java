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
        if(time < 1){ //Turn on shooter, wait for rev up
            driveBase.driveSetWithGyro(0, -1, 0, 0);
            shooter.setTargetRPS(80);
        }else if (time < 2.2){ //Feed preload into shooter, wait for piece to leave
            shooter.setTargetRPS(80);
            intake.setPower(0.8);
        }else if (time < 3){ //Turn off Shooter, Keep intake running, but Check if we have a Piece, Start moving to next piece
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSetWithGyro(blueDirection * 0.3, -1, 0, 0.7);
        }else if (time < 4){
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,-0.5, blueDirection * 0.5, 0.7);
        }else if (time < 5){
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0,0.5, blueDirection * -0.5, 0);
        }else if (time < 6){
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,0.5, blueDirection * -0.5, 0.7);
        }else if (time < 7){
            shooter.setTargetRPS(90);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(blueDirection * 0.3, -1, 0, 0.7);
        }else if (time < 8){
            shooter.setTargetRPS(90);
            intake.setPower(0.8);
            driveBase.driveSet(0,0,0,0);
        }else{
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0);
        }
}
    
}
