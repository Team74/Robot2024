package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class Encoder {
    //This is a seperate class in order to be easier to refactor with new encoders. 
    
    AnalogInput encoder;

    int encoderMax = 4096;

    Encoder(int encoderID){
        encoder = new AnalogInput(encoderID);
    }

    void encoderInit(){
        encoder.setAverageBits(4);
    }

    int getAverageValue(){
        return encoder.getAverageValue();
    }
}
