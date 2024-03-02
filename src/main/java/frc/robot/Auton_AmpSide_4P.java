package frc.robot;

public class Auton_AmpSide_4P extends Auton{

   Auton twoPieceAuto;
  
    public Auton_AmpSide_4P(SwerveDrive driveBase, Shooter shooter, Intake intake, boolean isBlue) {
        super(driveBase, shooter, intake, isBlue);
        twoPieceAuto = new Auton_AmpSide_2P(driveBase, shooter, intake, isBlue);
        //TODO Auto-generated constructor stub
    }

    //NOTE: +X means move forward, +Y means move to the left. 
    //TODO make it work on the blue side 
    //2 Piece Auto from Amp side position. Start with robot hugging the Speaker
    public void run(double time){
       if(time < 9.1){
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
        }//TODO make the robot face the driver station and reset gyro
    }
    
}
