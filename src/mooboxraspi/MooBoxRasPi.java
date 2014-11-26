/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mooboxraspi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author darksidious
 */
public class MooBoxRasPi {

    public static boolean runningServo1 = false;
    public static boolean runningServo2 = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        Gpio.wiringPiSetup();
        final GpioController gpio = GpioFactory.getInstance();
        double frHz = 100;
        double leftP = 0.25;
        double rightP = 3;
        double middleP = 1.5;

        SoftPwm.softPwmCreate(0, 0, 100);

        SoftPwm.softPwmCreate(1, 0, 100);

        double msPerCycle = 1000 / frHz;

        double pos1 = leftP * msPerCycle;
        double pos2 = middleP * msPerCycle;
        double pos3 = rightP * msPerCycle;

        System.out.println(")> Welcome to moo box project )>");
        SoftPwm.softPwmWrite(0, (int) pos1);
        SoftPwm.softPwmWrite(1, (int) pos1);
        Thread.sleep(500);
        SoftPwm.softPwmWrite(0, (int) pos3);
        SoftPwm.softPwmWrite(1, (int) pos3);
        Thread.sleep(500);

        int interval = 500;
        //pin.high();

        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_DOWN);

        final GpioPinDigitalInput myButton2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);

        // create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        if (!runningServo1) {
                            runningServo1 = true;
                            System.out.println(" --> executing process: mooooooooooooo");
                            try {
                                SoftPwm.softPwmWrite(0, (int) pos1);
                                Thread.sleep(2500);

                                SoftPwm.softPwmWrite(0, (int) pos3);
                                Thread.sleep(750);

                            } catch (InterruptedException ex) {
                                Logger.getLogger(MooBoxRasPi.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            runningServo1 = false;
                        }
                    }
                });
                if (!runningServo1) {
                    thread.start();
                }

                // display pin state on console
            }
        });

        myButton2.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        if (!runningServo2) {
                            runningServo2 = true;
                            System.out.println(" --> executing process: mooooooooooooo");
                            try {
                                SoftPwm.softPwmWrite(1, (int) pos1);
                                Thread.sleep(2500);

                                SoftPwm.softPwmWrite(1, (int) pos3);
                                Thread.sleep(750);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(MooBoxRasPi.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            runningServo2 = false;
                        }
                    }
                });
                if (!runningServo2) {
                    thread.start();
                }

                // display pin state on console
            }
        });
        
        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(500);
        }

        //pin.low();
    }

}
