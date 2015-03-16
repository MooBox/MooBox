package mooboxraspi;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
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

            while ((line = in.readLine()) != null && !line.equals(".")) {
                if (line.equals("1")) {
                    MooBoxRasPi.animServo1.run();
                }
                else if (line.equals("2")) {
                    MooBoxRasPi.animServo2.run();
                }
                else if (line.equals("1&2")) {
                    Thread thread1 = new Thread(MooBoxRasPi.animServo1);
                    Thread thread2 = new Thread(MooBoxRasPi.animServo2);
                    thread1.start();
                    thread2.start();
                }
                // Now write to the client
                out.println("Mooing " + line);
                logger.info("Overall message is:" + line);
            }

            server.close();
        }
        catch (IOException ioe) {
            logger.severe("IOException on socket listen: " + ioe);
        }
    }

}
