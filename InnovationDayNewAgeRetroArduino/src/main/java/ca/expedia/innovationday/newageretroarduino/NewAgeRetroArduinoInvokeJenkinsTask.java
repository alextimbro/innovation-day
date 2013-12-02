package ca.expedia.innovationday.newageretroarduino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.expedia.innovationday.newageretroarduino.service.BrushTeethToBlindService;


@Component("newAgeRetroArduinoTask")
public class NewAgeRetroArduinoInvokeJenkinsTask {
 
    @Autowired
    private BrushTeethToBlindService service;
    
    public void executeBrushTeeth() {
        service.brushTeeth();
    } 
}
