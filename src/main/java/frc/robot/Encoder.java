package frc.robot;

import javax.tools.Diagnostic;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DutyCycleEncoder;

public class Encoder {
    
    //This is a separate class in order to be easier to refactor with new encoders. 
    
    DutyCycleEncoder  encoder; //this would have to be a digital if the encoder was digital

    int encoderMax = 4096; //the max of the encoder. This is so that we can replace this one number and not have to hard code anything

    //creates the encoder at the id passed into it.
    Encoder(int encoderID){
        encoder = new DutyCycleEncoder(encoderID);
        encoderInit();
    }

    //inits the encoder
    void encoderInit(){
    }

    //this gets the value of the encoder by averaging the past few encoder values 
    int getAverageValue(){
        return (int) Math.round(encoder.getAbsolutePosition() * 4096);
    }

    //Gets the current value. Unused I believe 
    double getDoubleValue()
    {
        return encoder.getAbsolutePosition();
    }
}
