package mooboxraspi;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by mpacini on 16/03/15.
 */
public class Utils {
    private static int maxSize = 5 * 1024 * 1024;
    private static int maxFile = 5;
    private static boolean append = true;

    public static void initLogging(Logger logger) {
        FileHandler fh;

        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("logs/moobox.log", maxSize, maxFile, append);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> extractProperties(String inputFromWebSocket)
    {
        HashMap<String, String> properties = new HashMap<>();

        for (String s : inputFromWebSocket.split("&")) {
            String[] split = s.split("=");
            if(split.length == 2)
            {
                properties.put(split[0], split[1]);
            }
        }

        return properties;
    }
}
