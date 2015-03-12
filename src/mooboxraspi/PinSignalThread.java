/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mooboxraspi;

import com.pi4j.wiringpi.SoftPwm;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author darksidious
 */
public class PinSignalThread implements Runnable {
    private final double positionMeuuuuh;
    private final double posistionDeReposInitial;
    private boolean running = false;
    private final int gpio;
    private static final Logger logger = Logger.getLogger(PinSignalThread.class.getName());

    public PinSignalThread(double positionMeuuuuh, double posistionDeReposInitial, int gpio) {
        this.positionMeuuuuh = positionMeuuuuh;
        this.posistionDeReposInitial = posistionDeReposInitial;
        this.gpio = gpio;
    }
    
    

    @Override
    public void run() {

        if(!running)
        {   running = true;
            logger.info(" --> executing process: mooooooooooooo");
            try {
                SoftPwm.softPwmWrite(gpio, (int) positionMeuuuuh);
                Thread.sleep(1750);

                SoftPwm.softPwmWrite(gpio, (int) posistionDeReposInitial);
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(MooBoxRasPi.class.getName()).log(Level.SEVERE, null, ex);
            }

            running = false;
        }

    }


}
