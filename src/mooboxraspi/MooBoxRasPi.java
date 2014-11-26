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
import com.pi4j.io.gpio.PinState;
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
    private static final double frHz = 100;
    private static final double leftP = 0.25;
    private static final double rightP = 3;
    private static final double middleP = 1.5;
    
    private static final int GPIO1 = 0;
    private static final int GPIO2 = 1;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        System.out.println(")> Welcome to moo box project )>");
        Gpio.wiringPiSetup();
        final GpioController gpio = GpioFactory.getInstance();

        SoftPwm.softPwmCreate(GPIO1, 0, 100);

        SoftPwm.softPwmCreate(GPIO2, 0, 100);

        double msPerCycle = 1000 / frHz;

        double pos1 = leftP * msPerCycle;
        double pos2 = middleP * msPerCycle;
        double pos3 = rightP * msPerCycle;

       
        SoftPwm.softPwmWrite(GPIO1, (int) pos1);
        SoftPwm.softPwmWrite(GPIO2, (int) pos1);
        Thread.sleep(500);
        SoftPwm.softPwmWrite(GPIO1, (int) pos3);
        SoftPwm.softPwmWrite(GPIO2, (int) pos3);
        Thread.sleep(500);

        int interval = 500;
        //pin.high();

        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_UP);

        final GpioPinDigitalInput myButton2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP);

        // create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

                if (event.getState().equals(PinState.LOW)) {
                    Thread thread = new Thread(new PinSignalThread(pos1, pos3, GPIO1, runningServo1));
                    if (!runningServo1) {
                        thread.start();
                    }
                    // display pin state on console
                }
            }
        });

        myButton2.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
                if (event.getState().equals(PinState.LOW)) {
                    Thread thread = new Thread(new PinSignalThread(pos1, pos3, GPIO2, runningServo2));
                    if (!runningServo2) {
                        thread.start();
                    }
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
