package top.rafii2198.Utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger("Wynnic Enchantments Helper");

    public static void info(String Message){
        LOGGER.info(Message);
    }
}
