package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class Encoder {
    //This is a seperate class in order to be easier to refactor with new encoders. 
    
    AnalogInput encoder; //this would have to be a digital if the encoder was digital

    int encoderMax = 4096; //the max of the encoder. This is so that we can replace this one number and not have to hard code anything

    //creates the encoder at the id passed into it.
    Encoder(int encoderID){
        encoder = new AnalogInput(encoderID);
    }

    //inits the encoder
    void encoderInit(){
        encoder.setAverageBits(4);
    }

    //this gets the value of the encoder by averaging the past few encoder values 
    int getAverageValue(){
        return encoder.getAverageValue();
    }
}
