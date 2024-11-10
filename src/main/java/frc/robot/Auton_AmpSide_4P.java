package frc.robot;

public class Auton_AmpSide_4P extends Auton{

    //This will be the only Auton I will comment
    //There are many ways to do this, and many of them are better
    //This was very "low level", but also time consuming
    //If you look into tools like Path Planner, this will be useless
    //If you need something before comp, this will work

    //I was going to make this run the 2 piece first as this extends it, but I ran out of time to bug fix it. 
   Auton twoPieceAuto;
   //This uses a state method. You will see it better below
   String currentState = "Starting";
    int time;
    //This is to flip left/right if needed
    double dir = 1.0;
  
    public Auton_AmpSide_4P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        twoPieceAuto = new Auton_AmpSide_2P(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
        if(isBlue){
            dir = -1.0;
        }else{
            dir = 1.0;
        }
    }

    //Notes for below: 
    //There is a lot of reused code, so if you go this route, try making functions that take in the next state and preforms the action
    //Also this takes a lot of trial and error, so make sure you have time.
    //Also, if you run into time issues, try to push the robot and time needed to do things. 
    //I think we could have consistently gotten the 4th piece if I pushed it more

    //NOTE: +X means move forward, +Y means move to the left. 
    //2 Piece Auto from Amp side position. Start with robot hugging the Speaker
    public void run(double time2){
        //This switch statement does everything. Every tick this function will check its state and do the correct action
       switch (currentState){
        //This is the start of the auton. It will set everything to zero so we can start
        case "Starting":
        time = 0;
        driveBase.driveSetWithGyro(0,-1, 0, 0);
        intake.setPower(0);
        shooter.setPower(0);
        //This is the beauty of the state machine. Now next tick it will run the next function 
        currentState = "Rev Up 1";
        break;

        case "Rev Up 1":
        //This will rev up the flywheel
        shooter.setTargetRPS(80);
        intake.setPower(0);
        //Spin to the currect angle
        driveBase.driveSetWithGyro(dir * 0.3, 0.1, 0, 1.5);
        //After the angle is reached or after a certain time, go to the next state
        if(time > 55 || driveBase.gyro.getAngle() > 35.5){
            currentState = "Shoot Note 1";
            time = 0;
        }
        break;

        case "Shoot Note 1":
        driveBase.driveSetWithGyro(0, 0, 0, 0.7);
        //This will rev up the flywheel
        //After 20 ticks, start to shoot
        shooter.setTargetRPS(90);
        if(time > 20){
        shooter.setTargetRPS(90);
        intake.setPower(0.8);
        }
        //After 50 ticks or the piece is gone, go to the next state
        if(time > 50 || intake.hasPiece() == false){
            currentState = "Spin Back 1";
            time = 0;
        }
        break;

        //Undo the rotate 
        case "Spin Back 1":
        shooter.setPower(0);
        intake.setPowerUntilPiece(0);
        driveBase.driveSetWithGyro(dir * -0.3, -0.1, 0, 1.5);
        if(time > 90 || driveBase.gyro.getAngle() < 2){
            currentState = "Drive to Piece 1";
            time = 0;
        }
        break;

        //Drive with the gyro to the next piece
        case "Drive to Piece 1":
        intake.setPowerUntilPiece(0.8);
        driveBase.driveSetWithGyro(0,-1, dir * 0.55, 0.7);
        if(time > 75 || intake.hasPiece() == true){
            currentState = "Stop 1";
            time = 0;
        }
        break;

        //Intake until too much time or we have a piece
        case "Stop 1":
        intake.setPowerUntilPiece(0.8);
        driveBase.driveSetWithGyro(0,-1, dir * 0.55, 0.0);
        if(time > 30 || intake.hasPiece() == true){
            currentState = "Drive Back 1";
            time = 0;
        }
        break;

        //I will stop commenting as it is a lot of the same code
        case "Drive Back 1":
        shooter.setPower(0);
        intake.setPowerUntilPiece(0.8);
        driveBase.driveSetWithGyro(0, 1, dir * -0.55, 0.62);
        if(time > 88){
            currentState = "Spin Back 2";
            time = 0;
       }
        break;

       case "Spin Back 2":
       shooter.setTargetRPS(90);
       intake.setPowerUntilPiece(0.8);
       driveBase.driveSetWithGyro(dir * 0.35,0,0,0.9);
       if(time > 75 ||  driveBase.gyro.getAngle() > 35.5){
            currentState = "Shoot 2";
            time = 0;
       }
       break;

       case "Shoot 2":
       driveBase.driveSetWithGyro(0, 0.1, 0, 0.7);
        shooter.setTargetRPS(90);
        if(time > 20){
        shooter.setTargetRPS(80);
        intake.setPower(0.8);
        }
        if(time > 50 || intake.hasPiece() == false){
            currentState = "Leave 1";
            time = 0;
        }
       break;

       case "Leave 1":
       shooter.setPower(0);
        intake.setPowerUntilPiece(0);
        driveBase.driveSetWithGyro(dir * -0.1,-1, dir * 0.45, 0.7);
        if(time > 100){
            currentState = "Leave 2";
            time = 0;
        }
        break;

        case "Leave 2":
       shooter.setPower(0);
        intake.setPowerUntilPiece(0);
        driveBase.driveSetWithGyro(0,-1, dir * 0.05, 1.3);
        if(time > 78){
            currentState = "Grab 1";
            time = 0;
        }
        break;

        case "Grab 1":
        shooter.setPower(0);
        intake.setPowerUntilPiece(0.8);
        driveBase.driveSet(0,-1, 0, 0.1);
        if(time > 40 || intake.hasPiece() == true){
            currentState = "Back Up 1";
            time = 0;
        }
        break;

        case "Back Up 1":
        shooter.setPower(0);
        intake.setPowerUntilPiece(0.8);
        driveBase.driveSetWithGyro(0,1, dir * -0.1, 0.9);
        if(time > 80){
            currentState = "Bloop 1";
            time = 0;
        }
        break;

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
        break;

        case "Stop":
        shooter.setPower(0);
        intake.setPower(0);
        driveBase.driveSet(0,0, 0, 0);
        break;

        default: 
        shooter.setPower(0);
        intake.setPower(0);
        driveBase.driveSet(0,0, 0, 0);
        break;

    }
    System.out.println(currentState + " Time: " + time);
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
