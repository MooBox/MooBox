/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mooboxraspi;

import com.pi4j.wiringpi.SoftPwm;
import java.util.logging.Level;
import java.util.logging.Logger;
import static mooboxraspi.MooBoxRasPi.runningServo2;

/**
 *
 * @author darksidious
 */
public class PinSignalThread implements Runnable {
    private double pos1;
    private double pos3;
    private boolean running;
    private int gpio;

    public PinSignalThread(double pos1, double pos3, int gpio, boolean running) {
        this.pos1 = pos1;
        this.pos3 = pos3;
        this.running = running;
        this.gpio = gpio;
    }
    
    

    @Override
    public void run() {
        if (!running) {
            running = true;
            System.out.println(" --> executing process: mooooooooooooo");
            try {
                SoftPwm.softPwmWrite(gpio, (int) pos1);
                Thread.sleep(1750);

                SoftPwm.softPwmWrite(gpio, (int) pos3);
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(MooBoxRasPi.class.getName()).log(Level.SEVERE, null, ex);
            }

            running = false;
        }
    }

}