package frc.robot;

public class Auton_Center_4P extends Auton {

    int time;
    String currentState = "Starting";
    double dir = 1.0;

    public Auton_Center_4P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
        if(isBlue){
            dir = -1.0;
        }else{
            dir = 1.0;
        }
    }

    public void run(double time2){
        switch (currentState) {
            case "Starting":
            driveBase.driveSet(0, -1, 0, 0);
            intake.setPower(0);
            shooter.setPower(0);
            currentState = "Shoot'n";
            System.out.println("Running right auto");
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
            if (intake.hasPiece() == true || time > 70) {
                driveBase.driveSetWithGyro(0, -1, 0, 0);
                intake.setPower(0);
                time = 0;
                currentState = "Pause for Piece 1";
            }
            break;

            case "Pause for Piece 1":
            driveBase.driveSetWithGyro(0, -1, 0, 0);
            intake.setPower(0.8);
            if(intake.hasPiece() == true || time > 150){
                intake.setPower(0);
                time = 0;
                currentState = "Drive'n Back";
            }
            
            case "Drive'n Back":
            if (intake.hasPiece() == true) {
               driveBase.driveSetWithGyro(0, 1, 0, 0.7);
                if (time > 80) {
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
            driveBase.driveSetWithGyro(0, -0.6, dir * -0.85, 0.8);
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            if (time > 60) {
                driveBase.driveSetWithGyro(0, -1, 0, 0);
                currentState = "Foward to Piece 3";
                time = 0;
            }
            break;

            case"Foward to Piece 3":
            driveBase.driveSetWithGyro(0, -1, 0, 0.4);
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            if (time > 19) {
                driveBase.driveSetWithGyro(0, -1, 0, 0);
                currentState = "Stop for Piece 3";
                time = 0;
            }
            break;

            case "Stop for Piece 3":
            driveBase.driveSetWithGyro(0, -1, 0, 0.0);
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            if (intake.hasPiece() == true || time > 80) {
                driveBase.driveSetWithGyro(0, -1, 0, 0);
                currentState = "Coming Back from Piece 3";
                time = 0;
            }
            break;

            case "Coming Back from Piece 3":
            driveBase.driveSetWithGyro(0, 0.6,  dir *0.9, 0.8);
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            if (time > 60) {
                driveBase.driveSetWithGyro(0, 1, 0, 0);
                currentState = "Backward from Piece 3";
                time = 0;
            }
            break;

            case "Backward from Piece 3":
            driveBase.driveSetWithGyro(0, 1, 0, 0.4);
            shooter.setPower(0);
            intake.setPowerUntilPiece(0.8);
            if (time > 22) {
                driveBase.driveSetWithGyro(0, 1, 0, 0);
                currentState = "Shoot Piece 3";
                time = 0;
            }
            break;

            case "Shoot Piece 3":
            shooter.setTargetRPS(80);
            intake.setPower(0);
            if (shooter.getRPS() > 77) {
                intake.setPower(0.8);
                if (intake.hasPiece() == false) {
                    time = 0;
                    currentState = "Stop'n";
                }
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
        System.out.println(time);
        time++;
    }
    }

