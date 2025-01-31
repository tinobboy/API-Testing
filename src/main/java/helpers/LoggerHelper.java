package helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static helpers.StringHelper.*;

public class LoggerHelper {

    private static Logger logger;

    public LoggerHelper(Class paramClass) {
        logger = LoggerFactory.getLogger(paramClass);
    }

    public static void printTraceMessage(String message){
        logger.trace(formatText("||| %s |||",message));
    }

    public static void printWarningMessage(String message){
        logger.warn(formatText("||| %s |||",message));
    }

    public static void printErrorMessage(String message){
        logger.error(formatText("||| %s |||",message));
    }

    public static void printInfoMessage(String message){
        logger.info(formatText("||| %s |||",message));
    }
}
