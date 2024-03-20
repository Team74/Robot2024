package frc.robot;

public class Auton_SourceSide_2P extends Auton{

    double dir = 1.0;
    String currentState = "Starting";
    int time;
    public Auton_SourceSide_2P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
        if(isBlue){
            dir = -1.0;
        }else{
            dir = 1.0;
        }
    }

    //2 Piece Auto from Amp side position. Start with robot hugging the Speaker
    public void run(double time2){
        switch (currentState){
            case "Starting":
            time = 0;
            driveBase.driveSetWithGyro(0,-1, 0, 0);
            intake.setPower(0);
            shooter.setPower(0);
            currentState = "Rev Up 1";
            break;
    
            case "Rev Up 1":
            shooter.setTargetRPS(80);
            intake.setPower(0);
            driveBase.driveSetWithGyro(dir * -0.3, 0.1, 0, 1.5);
            if(time > 55 || driveBase.gyro.getAngle() < -35.5){
                currentState = "Shoot Note 1";
                time = 0;
            }
            break;
    
            case "Shoot Note 1":
            driveBase.driveSetWithGyro(0, 0, 0, 0.7);
            shooter.setTargetRPS(90);
            if(time > 20){
            shooter.setTargetRPS(90);
            intake.setPower(0.8);
            }
            if(time > 50 || intake.hasPiece() == false){
                currentState = "Spin Back 1";
                time = 0;
            }
            break;
    
            case "Spin Back 1":
            shooter.setPower(0);
            intake.setPowerUntilPiece(0);
            driveBase.driveSetWithGyro(dir * 0.3, -0.1, 0, 1.5);
            if(time > 90 || driveBase.gyro.getAngle() > -2){
                currentState = "Drive to Piece 1";
                time = 0;
            }
            break;
    
            case "Drive to Piece 1":
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,-1, dir * -0.55, 0.7);
            if(time > 50 || intake.hasPiece() == true){
                currentState = "Stop 1";
                time = 0;
            }
            break;
    
            case "Stop 1":
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,-1, dir * -0.55, 0.0);
            if(time > 30 || intake.hasPiece() == true){
                currentState = "Drive Back 1";
                time = 0;
            }
            break;
    
            case "Drive Back 1":
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0, 1, dir * 0.55, 0.62);
            if(time > 78){
                currentState = "Spin Back 2";
                time = 0;
           }
            break;
    
           
           case "Spin Back 2":
           shooter.setTargetRPS(90);
           intake.setPowerUntilPiece(0.8);
           driveBase.driveSetWithGyro(dir * -0.35,0,0,0.9);
           if(time > 88 || driveBase.gyro.getAngle() < -35.5){
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
                currentState = "Leave 0";
                time = 0;
            }
           break;

           case "Leave 0":
           shooter.setPower(0);
            intake.setPowerUntilPiece(0);
            driveBase.driveSetWithGyro(0,0, dir * -0.45, 0.9);
            if(time > 150){
                currentState = "Leave 1";
                time = 0;
            }
            break;
    
           case "Leave 1":
           shooter.setPower(0);
            intake.setPowerUntilPiece(0);
            driveBase.driveSetWithGyro(dir * 0.2,-1, dir * -0.45, 0.7);
            if(time > 100){
                currentState = "Leave 2";
                time = 0;
            }
            break;
    
            case "Leave 2":
           shooter.setPower(0);
            intake.setPowerUntilPiece(0);
            driveBase.driveSetWithGyro(0,-1, dir * -0.05, 1.3);
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
            driveBase.driveSetWithGyro(0,1, dir * 0.1, 0.9);
            if(time > 60){
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
/*if(time < 0.5){ //Turn on shooter
            driveBase.driveSetWithGyro(0, -1, 0, 0);
            shooter.setTargetRPS(80);
            intake.setPower(0);
        }else if (time < 1){ //spin to speaker
            shooter.setTargetRPS(80);
            driveBase.driveSetWithGyro(-0.3, 0.1, 0, 1.5);
        }else if (time < 2.3){ //shoot
            shooter.setTargetRPS(80);
            intake.setPower(0.8);
            driveBase.driveSetWithGyro(0, 0, 0, 0.7);
        }else if (time < 2.7){ //stop motors, spin back
            shooter.setPower(0);
            intake.setPowerUntilPiece(0);
           driveBase.driveSetWithGyro(0.3, 0, 0, 0.9);
        }else if (time < 4.5){ // drive to next piece
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,-1, -0.2, 0.7);
        }else if (time < 6){//stop for a sec
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0,0, 0, 0.0);
        }else if (time < 8){ // drive back
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0, 1, 0.2, 0.7);
        }else if (time < 9){// turn on motors
            shooter.setTargetRPS(90);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0,0,0,0);
        }else if(time < 9.5){ //spin to speaker
            shooter.setTargetRPS(90);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSet(0.3,0,0,0);
        }else if(time < 11.5){ // shoot
            shooter.setTargetRPS(90);
            intake.setPower(0.8);
        }else{
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0);
        }
 */