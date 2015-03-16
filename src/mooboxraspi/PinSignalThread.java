/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mooboxraspi;

import com.pi4j.wiringpi.SoftPwm;

import javax.mail.MessagingException;
import java.io.IOException;
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
    private String subject;
    private final int gpio;
    private static final Logger logger = Logger.getLogger(PinSignalThread.class.getName());
    private String scriptName;

    public PinSignalThread(double positionMeuuuuh, double posistionDeReposInitial, int gpio, String subject, String scriptName) {
        Utils.initLogging(logger);
        this.positionMeuuuuh = positionMeuuuuh;
        this.posistionDeReposInitial = posistionDeReposInitial;
        this.gpio = gpio;
        this.subject = subject;
        this.scriptName=scriptName;
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


/*
            String[] recipients = {"trigger@recipe.ifttt.com"};
            try {
                ServiceMail.sendMail(recipients,subject,"Moooooooooooooo");
            }
            catch (MessagingException e) {
                logger.warning(e.toString() + e.getMessage());
            }
*/

            Thread thread = new Thread(() -> {
                try {
                    logger.info("let's try to send a mail...");
                    String[] commands = new String[]{"/opt/MooBoxProject/scripts/"+scriptName};
                    Runtime.getRuntime().exec(commands);
                }
                catch (IOException e) {
                    logger.log(Level.SEVERE, null, e);
                }
            });

            thread.start();


            running = false;
        }

    }


}
