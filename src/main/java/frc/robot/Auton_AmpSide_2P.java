package frc.robot;

public class Auton_AmpSide_2P extends Auton {

    public Auton_AmpSide_2P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
    }
    int time;
    String currentState = "Starting";
    //NOTE: +X means move forward, +Y means move to the left. 
    //TODO make it work on the blue side 
    //2 Piece Auto from Amp side position. Start with robot hugging the Speaker
    public void run(double time2){
    switch (currentState) {
        case "Starting":
        driveBase.driveSet(0, -1, 0, 0);
        shooter.setTargetRPS(0);
        intake.setPower(0);
        currentState = "Rev Up1";
        time = 0;
            break;

        case "Rev Up1":
        driveBase.driveSet(0.3, 0.1, 0, 0.7);
        shooter.setTargetRPS(80);
        intake.setPower(0);
        currentState = "Spin'n";
        time = 0;
            break;

        case "Spin'n":
        //driveBase.driveSetWithGyro(0, 0, 0, 0);
        shooter.setTargetRPS(80);
        intake.setPower(0);
        if (shooter.getRPS() > 77){
            intake.setPower(0.8);
        } 
        if (intake.hasPiece() == false){
            currentState = "Drive To Peice";
        }
        time = 0;
            break;
        
        case "Drive To Peice":
        driveBase.driveSetWithGyro(-0.3, 0, 0, 0.9);
        shooter.setPower(0);
        intake.setPower(0);
        if (time > 80){
            intake.setPower(0.8);
            driveBase.driveSetWithGyro(0, -1, 0.55, 0.7);
        }
        if (intake.hasPiece() == true){
            currentState = "Drive'n Back";
        }
        time = 0;
            break;

        case "Drive'n Back":
        if (time > 25 || intake.hasPiece() == true){
            driveBase.driveSetWithGyro(0, time2, time2, time2);
        }
        

    }


    time++; 
    }
    
}

/* if(time < 0.5){ //Turn on shooter
            driveBase.driveSetWithGyro(0, -1, 0, 0);
            shooter.setTargetRPS(80);
            intake.setPower(0);
        }else if (time < 1){ //spin to speaker
            shooter.setTargetRPS(80);
            driveBase.driveSetWithGyro(0.3, 0.1, 0, 1.5);
        }else if (time < 2.3){ //shoot
            shooter.setTargetRPS(80);
            intake.setPower(0.8);
            driveBase.driveSetWithGyro(0, 0, 0, 0.7);
        }else if (time < 2.7){ //stop motors, spin back
            shooter.setPower(0);
            intake.setPowerUntilPiece(0);
           driveBase.driveSetWithGyro(-0.3, 0, 0, 0.9);
        }else if (time < 4.5){ // drive to next piece
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,-1, 0.55, 0.7);
        }else if (time < 6){//stop for a sec
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,0, 0, 0.0);
        }else if (time < 8){ // drive back
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0, 1, -0.55, 0.62);
        }else if (time < 9){// turn on motors
            shooter.setTargetRPS(90);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0,0,0,0);
        }else if(time < 9.5){ //spin to speaker
            shooter.setTargetRPS(90);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0.35,0,0,0.9);
        }else if(time < 11){ // shoot
            shooter.setTargetRPS(90);
            driveBase.driveSet(0,0,0,0);
            intake.setPower(0.8);
        }else if(time < 12.5){
            shooter.setPower(0);
            intake.setPowerUntilPiece(0);
            driveBase.driveSetWithGyro(-0.2,-1, 0.55, 0.7);
        }else if(time < 14.5){
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,-1, 0.15, 1.3);
        }else if(time < 16){
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
        }else if(time < 17){
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,1, 0, 0.7);
        }else if(time < 18){
            shooter.setTargetRPS(20);
            intake.setPower(0.8);
            driveBase.driveSetWithGyro(0,0, 0, 0);
        }else{
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0);
        }
 */