/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mooboxraspi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author darksidious
 */
public class MooBoxRasPi {

    private static final double frHz = 100;
    private static final double leftP = 0.5;
    private static final double rightP = 3;

    private static final int GPIO0 = 0;
    private static final int GPIO1 = 1;

    private static final double msPerCycle = 1000 / frHz;

    private static final double positionMeuuuuh = leftP * msPerCycle;
    private static final double posistionDeReposInitial = rightP * msPerCycle;



    private static GpioController gpio;
    
    public static final Logger logger = Logger.getLogger(MooBoxRasPi.class.getName());
    public static PinSignalThread animServo1;
    public static PinSignalThread animServo2;


    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Utils.initLogging(logger);
        logger.info("}> -Welcome to moo box project- <{");
        Gpio.wiringPiSetup();
        gpio = GpioFactory.getInstance();

        initServo();
        startAnim();
        initButton();

        initServer();

        // keep program running until user aborts (CTRL-C)
        while(true)
        {}
    }

    private static GpioPinListenerDigital initButtonServoAnimListner(PinSignalThread runnable) {
        return (GpioPinDigitalStateChangeEvent event) -> {
            logger.log(Level.INFO, " --> GPIO PIN STATE CHANGE: {0} = {1}", new Object[]{event.getPin(), event.getState()});
            if (event.getState().equals(PinState.LOW)) {
                new Thread(runnable).start();
            }
            // display pin state on console
        };
    }

    private static void initServo() {
        SoftPwm.softPwmCreate(GPIO0, 0, 100);
        SoftPwm.softPwmCreate(GPIO1, 0, 100);
    }

    private static void startAnim() throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                logger.info("let's try to send a mail...");
                String[] commands = new String[]{"/opt/MooBoxProject/scripts/wake.sh"};
                Runtime.getRuntime().exec(commands);
            }
            catch (IOException e) {
                logger.log(Level.SEVERE, null, e);
            }
        });

        thread.start();

        SoftPwm.softPwmWrite(GPIO0, (int) positionMeuuuuh);
        SoftPwm.softPwmWrite(GPIO1, (int) positionMeuuuuh);
        Thread.sleep(500);
        SoftPwm.softPwmWrite(GPIO0, (int) posistionDeReposInitial);
        SoftPwm.softPwmWrite(GPIO1, (int) posistionDeReposInitial);
        Thread.sleep(500);
    }

    private static void initButton() {
        final GpioPinDigitalInput servoButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.PULL_UP);

        final GpioPinDigitalInput servoButton2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP);

        animServo1 = new PinSignalThread(positionMeuuuuh, posistionDeReposInitial, GPIO0,"#GREENMOO","greenMoo.sh");
        animServo2 = new PinSignalThread(positionMeuuuuh, posistionDeReposInitial, GPIO1,"#REDMOO", "redMoo.sh");

        servoButton.addListener(initButtonServoAnimListner(animServo1));
        servoButton2.addListener(initButtonServoAnimListner(animServo2));
        
        
        final GpioPinDigitalInput shutdownButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_13, PinPullResistance.PULL_UP);

        shutdownButton.addListener(initButtonShutdownListner());
    }
    
        private static GpioPinListenerDigital initButtonShutdownListner() {
        return (GpioPinDigitalStateChangeEvent event) -> {
            logger.log(Level.INFO, " --> GPIO PIN STATE CHANGE: {0} = {1}", new Object[]{event.getPin(), event.getState()});
            if (event.getState().equals(PinState.LOW)) {
                Thread thread = new Thread(() -> {
                    try {
                        logger.info("let's try to send a mail...");
                        String[] commands = new String[]{"/opt/MooBoxProject/scripts/sleep.sh"};
                        Runtime.getRuntime().exec(commands);
                        Runtime.getRuntime().exec("halt");
                    }
                    catch (IOException e) {
                        logger.log(Level.SEVERE, null, e);
                    }
                });

                thread.start();
            }
        };
    }

    private static int port=8091, maxConnections=0;

    private static void initServer(){
        int i=0;

        try{
            ServerSocket listener = null;
            try {
                listener = new ServerSocket(port);
            }
            catch (IOException e) {
                logger.severe("IOException on socket listen: " + e);
            }
            Socket server;

            while((i++ < maxConnections) || (maxConnections == 0)){
                MooBoxRunnableServer connection;

                server = listener.accept();
                MooBoxRunnableServer conn_c= new MooBoxRunnableServer(server);
                Thread t = new Thread(conn_c);
                t.start();
            }
        } catch (IOException ioe) {
            logger.severe("IOException on socket listen: " + ioe);
        }
    }


}
