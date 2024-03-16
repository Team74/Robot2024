package frc.robot;

public class Auton_Move extends Auton {

    public Auton_Move(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
    }
    int time;
    String currentState = "Starting";
    //Shoot pre load then move away. Good for when teammates want to do things and there is a fear of a collision
    public void run(double time2){
       switch (currentState) {
        case "Starting":
        time = 0;
        driveBase.driveSet(0, -1, 0, 0);
        intake.setPower(0);
        shooter.setPower(0);
        currentState = "Shoot";
            break;
        
        case "Shoot":
        shooter.setTargetRPS(90);
        if (shooter.getRPS() > 88){
            intake.setPower(0.8);
        }
        if (intake.hasPiece() == false){
            currentState = "Delay";
            time = 0;
        }
            break;
    
        case "Delay":
        driveBase.driveSet(0, 0, 0, 0);
        intake.setPower(0);
        shooter.setPower(0);
        if (time > 500){
            currentState = "Move";
            time = 0;
        }
            break;

        case "Move":
        driveBase.driveSet(0, -1, 0, 1);
        if (time > 74){
            time = 0;
            currentState = "Stop";
        }
            break;

        case "Stop":
        driveBase.driveSet(0, 0, 0, 0);
        intake.setPower(0);
        shooter.setPower(0);
        time = 0; 
            break;
       }
       time++;
    }
    
}
/* if(time < 0.7){ //Start Shooter, wait for rev up
            driveBase.driveSet(0, 0, 0, 0);
            shooter.setTargetRPS(80);
        }else if (time < 2){ //Feed piece into Shooter
            shooter.setTargetRPS(80);
            intake.setPower(0.8);
        }else if (time < 10){ //Turn off Shooter and Intake
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0);
        }else if(time < 11){//Wait for a while, then drive out
            driveBase.driveSet(0, -1, 0, 1);
        }else{ //Auton Done, stop motors
            shooter.setPower(0);
            intake.setPower(0);
            driveBase.driveSet(0, 0, 0, 0);
        } */