package frc.robot;

public class Auton_Center_2P extends Auton {

    public Auton_Center_2P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
    }
    //-y = forward
    
//Test Code, ignore for now
    /*double timeToRun = 0.0;
    if(time < timeToRun)
        Boolean firstTime = true;
            if(firstTime == true){
                firstTime = false;
                timeToRun = time + 1;
            }*/
    int time;
    String currentState = "Starting";

    public void run(double time2){
        switch (currentState) {
            case "Starting":
            driveBase.driveSet(0, -1, 0, 0);
            intake.setPower(0);
            shooter.setPower(0);
            currentState = "Shoot'n";
            time = 0;
            break;
            
            case "Shoot'n":
            driveBase.driveSet(0, -1, 0, 0);
            shooter.setTargetRPS(80);
            intake.setPower(0);
            if (shooter.getRPS() > 77){
                intake.setPower(0.8);
                if (intake.hasPiece() == false) {
                    shooter.setPower(0);
                    intake.setPower(0);
                    time = 0;
                    currentState = "Drive'n To Piece";
                }
            }
            
            break;

            case "Drive'n To Piece":
            driveBase.driveSetWithGyro(0, -1, 0, 0.8);
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            if (intake.hasPiece() == true || time > 150) {
                driveBase.driveSetWithGyro(0, -1, 0, 0);
                intake.setPower(0);
                time = 0;
                currentState = "Drive'n Back";
            }
            break;
            
            case "Drive'n Back":
            if (intake.hasPiece() == true) {
               driveBase.driveSetWithGyro(0, 1, 0, 0.7);
                if (time > 65) {
                    driveBase.driveSetWithGyro(0, -1, 0, 0);
                    currentState = "Shoote'n Again";
                    time = 0;
                }
            }
            
            break;

            case "Shoote'n Again":
            shooter.setTargetRPS(80);
            intake.setPower(0);
            if (shooter.getRPS() > 77) {
                intake.setPower(0.8);
                if (intake.hasPiece() == false) {
                    time = 0;
                    currentState = "Leave'n";
                }
            }
           
            break;

            case "Leave'n":
            driveBase.driveSetWithGyro(0, -1, 0, 0.8);
            shooter.setPower(0);
            intake.setPower(0);
            if (time > 250) {
                driveBase.driveSetWithGyro(0, -1, 0, 0);
                currentState = "Stop'n";
                time = 0;
            }
            break;

            case "Stop'n":
            driveBase.driveSet(0, 0, 0, 0);
            shooter.setPower(0);
            intake.setPower(0);
            time = 0;
            break;

            default: 
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0,0, 0, 0);
            break;
            }
        time++;
    }
}

/* if(time < 1){ //Turn on shooter, wait for rev up
            driveBase.driveSetWithGyro(0, -1, 0, 0);
            shooter.setTargetRPS(80);
        }else if (time < 2.2){ //Feed preload into shooter, wait for piece to leave
            shooter.setTargetRPS(80);
            intake.setPower(0.8);
        }else if (time < 3){ //Turn off Shooter, Keep intake running, but Check if we have a Piece, Start moving to next piece
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0, -1, 0, 1);
        }else if (time < 4){ //Stop to pick up piece
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0, 0, 0, 0);
        }else if(time < 5.3){ //drive back 
            intake.setPowerUntilPiece(0.8);
            driveBase.driveSetWithGyro(0, 1, 0, 0.7);
        }else if(time < 6){
            shooter.setTargetRPS(80);
            driveBase.driveSetWithGyro(0, 1, 0, 0.6);
        }else if(time < 6.5){ //shoot
            shooter.setTargetRPS(80);
            driveBase.driveSetWithGyro(0, 0, 0, 0);
            intake.setPower(0.8);
        }else{
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0); 
        } */