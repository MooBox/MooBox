package mooboxraspi;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by mpacini on 11/03/15.
 */
public class MooBoxRunnableServer implements Runnable {

    private Socket server;
    private String line, input;
    private Logger logger = Logger.getLogger(MooBoxRunnableServer.class.getName());

    MooBoxRunnableServer(Socket server) {
        Utils.initLogging(logger);
        this.server = server;
    }

    public void run() {

        input = "";

        try {
            // Get input from the client
            DataInputStream in = new DataInputStream(server.getInputStream());
            PrintStream out = new PrintStream(server.getOutputStream());
            String input = "";

            while ((line = in.readLine()) != null && !line.equals(".")) {
                input += line;
            }

            logger.info("input = "+input);
            HashMap<String, String> properties = Utils.extractProperties(input);

            logger.info("properties = " + properties.values());

            String function = properties.getOrDefault("moobox.function", "");
            String mooer = properties.getOrDefault("moobox.mooer", "Anonymous");
            if (function.equals("1")) {
                MooBoxRasPi.animServo1.run(mooer);
            }
            else if (function.equals("2")) {
                MooBoxRasPi.animServo2.run(mooer);
            }
            else if (function.equals("1AND2")) {
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MooBoxRasPi.animServo1.run(mooer);

                    }
                });
                Thread thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        MooBoxRasPi.animServo2.run(mooer);

                    }
                });
                thread1.start();
                thread2.start();
            }
            else {
                logger.severe("Bad function" + function.length() + " value=" + function);

            }
            out.println(input);


            server.close();
        }
        catch (IOException ioe) {
            logger.severe("IOException on socket listen: " + ioe);
        }
    }

}
