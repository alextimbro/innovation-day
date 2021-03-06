package ca.expedia.innovationday.newageretroarduino.service;

/**
 * This weapon belongs to the New Age Retro Arduino, and it's used whenever
 * there is a problem in Jenkins.
 */
public interface NastySound {

    void on(ArduinoSound sound);
    
    void off();
    
    void on(long miliseconds, ArduinoSound sound);
    
}
