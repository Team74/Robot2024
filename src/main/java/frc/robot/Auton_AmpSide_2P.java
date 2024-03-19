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
        time = 0;
        currentState = "Rev Up1";
        break;

        case "Rev Up1":
        driveBase.driveSet(0.3, 0.1, 0, 0.7);
        shooter.setTargetRPS(90);
        intake.setPower(0);
        time = 0;
        currentState = "Spin'n";
        break;

        case "Spin'n":
        //driveBase.driveSetWithGyro(0, 0, 0, 0);
        shooter.setTargetRPS(90);
        intake.setPower(0);
        if (shooter.getRPS() > 88){
            intake.setPower(0.8);
        } 
        if (intake.hasPiece() == false){
            time = 0;
            currentState = "Drive To Peice";
        }
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
            time = 0;
            currentState = "Drive'n Back";
        }
        break;

        case "Drive'n Back"://yeehaw
        if (time > 25 || intake.hasPiece() == true){
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSetWithGyro(0, 1, -0.55, 0.62);
            if (time > 100) {
                driveBase.driveSetWithGyro(0, 1, -0.55, 0);
                time = 0;
                currentState = "Rev Up2";
            }
            
        }
        break;

        case "rev Up2":
        driveBase.driveSetWithGyro(0, 1, -0.55, 0);
        shooter.setTargetRPS(90);
        intake.setPowerUntilPiece(0.8);
        time = 0;
        currentState = "Spin'n 2";
        break;

        case "Spin'n 2":
        driveBase.driveSetWithGyro(0.35, 0, 0, 0.9);
        shooter.setTargetRPS(90);
        intake.setPowerUntilPiece(0.8);
        if (shooter.getRPS() > 88) {
            intake.setPower(0.8);
            time = 0;
            currentState = "Shooty Shoot";
        }
        break;

        case "Shooty Shoot":
        if (shooter.getRPS() > 88) {
            intake.setPower(0.8);
            if (intake.hasPiece() == false) {
                intake.setPower(0);
                shooter.setPower(0);
                time = 0;
                currentState = "Stop";
            }
        }
        break;

        case "Stop":
        if (intake.hasPiece() == false) {
            driveBase.driveSetWithGyro(0, 0, 0, 0);
            shooter.setPower(0);
            intake.setPower(0);
            time = 0;
            //currentState = "Spin'n 3";
        }
        break;


        //following code isn't nessicary just here incase we want to go for a 3 piece...

        /*case "Spin'n 3":
        if (intake.hasPiece() == false) {
            driveBase.driveSetWithGyro(-0.2,-1, 0.55, 0.7);
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.9);
            time = 0;
            currentState = "Drive'n To Third";
        }
        break;

        case "Drive'n To Third":
        driveBase.driveSetWithGyro(0,-1, 0.15, 1.3);
        shooter.setPower(0);
        intake.setPowerUntilPiece(0.8);
        if (time > 100 || intake.hasPiece() == true) {
            driveBase.driveSetWithGyro(0,-1, 0.15, 0);
            time = 0;
            currentState = "Pick'n Up";
        }
        break;

        case "Pick'n Up":
        shooter.setPower(0);
        intake.setPower(0.8);
        if (intake.hasPiece() == true) {
            time = 0;
            currentState = "The Return";
        }
        break;

        case "The Return":
        driveBase.driveSetWithGyro(0,1, 0, 0.7);
        intake.setPowerUntilPiece(0.8);
        if (time > 100) {
            driveBase.driveSetWithGyro(0,1, 0, 0);
            time = 0;
            currentState = "Shoot'n Again";
        }
        break;

        case "Shoot'n Again":
        driveBase.driveSetWithGyro(0,0,0,0);
        shooter.setTargetRPS(90);
        intake.setPowerUntilPiece(0.8);
        if (shooter.getRPS() > 88) {
            intake.setPower(0.9);
            time = 0;
            currentState = "Just Check'n";
        }
        break;

        case "Just Check'n":
        intake.setPower(0.9);
        if (intake.hasPiece() == false) {
            driveBase.driveSetWithGyro(0,0,0,0);
            shooter.setPower(0);
            intake.setPower(0);
            time = 0;
        }
        break;*/

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