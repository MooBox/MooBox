package mooboxraspi;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by mpacini on 16/03/15.
 */
public class Utils {
    public static void initLogging(Logger logger){
        FileHandler fh;

        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("logs/moobox.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
