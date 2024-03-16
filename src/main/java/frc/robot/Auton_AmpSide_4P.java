package frc.robot;

public class Auton_AmpSide_4P extends Auton{

   Auton twoPieceAuto;
   String currentState = "Starting";
    int time;
  
    public Auton_AmpSide_4P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        twoPieceAuto = new Auton_AmpSide_2P(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
    }

    //NOTE: +X means move forward, +Y means move to the left. 
    //TODO make it work on the blue side 
    //2 Piece Auto from Amp side position. Start with robot hugging the Speaker
    public void run(double time2){
       switch (currentState){
        case "Starting":
        time = 0;
        driveBase.driveSetWithGyro(0, -1, 0, 0);
        intake.setPower(0);
        shooter.setPower(0);
        currentState = "Rev Up 1";
        break;

        case "Rev Up 1":
        shooter.setTargetRPS(80);
        intake.setPower(0);
        driveBase.driveSetWithGyro(0.3, 0.1, 0, 1.5);
        if(time > 65){
            currentState = "Shoot Note 1";
            time = 0;
        }
        break;

        case "Shoot Note 1":
        shooter.setTargetRPS(80);
        intake.setPower(0.8);
        driveBase.driveSetWithGyro(0, 0, 0, 0.7);
        if(time > 25 || intake.hasPiece() == false){
            currentState = "Spin Back 1";
            time = 0;
        }
        break;

        case "Spin Back 1":
        shooter.setPower(0);
        intake.setPowerUntilPiece(0);
        driveBase.driveSetWithGyro(-0.3, 0, 0, 0.9);
        if(time > 90){
            currentState = "Drive to Piece 1";
            time = 0;
        }

        case "Drive to Piece 1":
        intake.setPowerUntilPiece(0.8);
        driveBase.driveSetWithGyro(0,-1, 0.55, 0.7);
        if(time > 75 || intake.hasPiece() == true){
            currentState = "Drive Back 1";
            time = 0;
        }

        case "Drive Back 1":
        shooter.setPower(0);
        intake.setPowerUntilPiece(0.8);
        driveBase.driveSetWithGyro(0, 1, -0.55, 0.62);
        if(time > 50){
            currentState = "Spin Back 2";
            time = 0;
       }

       case "Spin Back 2":
       shooter.setTargetRPS(90);
       intake.setPowerUntilPiece(0.8);
       driveBase.driveSet(0.35,0,0,0.9);
       if(time > 75){
            currentState = "Shoot 2";
            time = 0;
       }

       case "Shoot 2":
       shooter.setTargetRPS(90);
       driveBase.driveSet(0,0,0,0);
       intake.setPower(0.8);
       if(time > 50 || intake.hasPiece() == false){
         currentState = "Leave 1";
            time = 0;
       }

       case "Leave 1":
       shooter.setPower(0);
        intake.setPowerUntilPiece(0);
        driveBase.driveSetWithGyro(-0.2,-1, 0.55, 0.7);
        if(time > 100){
            currentState = "Leave 2";
            time = 0;
        }

        case "Leave 2":
       shooter.setPower(0);
        intake.setPowerUntilPiece(0);
        driveBase.driveSetWithGyro(0,-1, 0.15, 1.3);
        if(time > 75){
            currentState = "Grab 1";
            time = 0;
        }

        case "Grab 1":
        shooter.setPower(0);
        intake.setPowerUntilPiece(0.8);
        driveBase.driveSetWithGyro(0,-1, 0, 0.1);
        if(time > 40 || intake.hasPiece() == true){
            currentState = "Back Up 1";
            time = 0;
        }

        case "Back Up 1":
        shooter.setPower(0);
        intake.setPowerUntilPiece(0.8);
        driveBase.driveSetWithGyro(0,1, 0, 0.7);
        if(time > 50){
            currentState = "Bloop 1";
            time = 0;
        }

        case "Bloop 1":
        shooter.setTargetRPS(15);
        if(shooter.getRPS() > 13){
            intake.setPower(0.8);
        }
        driveBase.driveSetWithGyro(0,1, 0, 0.1);
        if(time > 30 || intake.hasPiece() == false){
            currentState = "Stop";
            time = 0;
        }

        case "Stop":
        shooter.setPower(0);
        intake.setPower(0);
        driveBase.driveSet(0,0, 0, 0);

    }
    time++;
}
    
    
}
/*if(time < 9.1){
        twoPieceAuto.run(time); //Run the two piece auto, as this extends it
       }else if(time < 10){ //drive away from speaker
        driveBase.driveSet(1, 0.3, 0.5, 1.5);
       }else if(time < 11){ //drive forward and turn
        driveBase.driveSet(2.14, 0.1, 0.9, 1.5);
       }else if (time < 12){ // drive left and turn on intake
        driveBase.driveSet(2.14, 1, 0, 1.5);
        intake.setPowerUntilPiece(0.8);
       }else if(time < 12.5){ // intake
        driveBase.driveSet(0, 0, 0, 0);
        intake.setPowerUntilPiece(0.8);
       }else if(time < 13.5){ // drive right and rotate
        driveBase.driveSet(-2.14, -1, 0, 1.5);
       }else if (time < 14.5){ // drive back and rotate
        driveBase.driveSet(-1, -0.3, -0.5, 1.5);
        shooter.setTargetRPS(80);
       }else if(time < 15){ //shoot
        shooter.setTargetRPS(80);
        intake.setPower(0.8);
       }else{ //Auton Done, stop everything
        shooter.setPower(0);
        intake.setPower(0);
        driveBase.driveSet(0, 0, 0, 0);
        }//TODO make the robot face the driver station and reset gyro */
