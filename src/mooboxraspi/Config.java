package mooboxraspi;

import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;

import java.io.*;
import java.util.Properties;

/**
 * Created by mpacini on 17/03/15.
 */
public class Config {
    public static Properties properties = null;


    public static String getProperty(String key, String defaultValue) {
        init();
        String value = defaultValue;
        if (properties != null) {
            value = properties.getProperty(key, defaultValue);
        }
        return value;
    }

    public static int getPropertyAsInt(String key, int defaultValue) {
        String propertyValue = getProperty(key, defaultValue + "");
        return Integer.valueOf(propertyValue);
    }

    public static double getPropertyAsDouble(String key, double defaultValue) {
        String propertyValue = getProperty(key, defaultValue + "");
        return Double.valueOf(propertyValue);
    }

    public static boolean getPropertyAsBoolean(String key, boolean defaultValue) {
        String propertyValue = getProperty(key, defaultValue + "");
        return Boolean.valueOf(propertyValue);
    }


    public static void init() {
        if (properties == null) {
            File file = new File("conf/config.properties");

            if (file.exists()) {
                try {
                    InputStream inputStream = new FileInputStream(file);

                    Reader reader = new UTF8Reader(inputStream);
                    properties = new Properties();

                    properties.load(reader);
                }
                catch (IOException e) {
                    properties = null;
                    e.printStackTrace();
                }
            }
        }
    }
}
