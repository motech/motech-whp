package org.motechproject.whp.importer.csv.logger;

import org.apache.log4j.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ImporterLogger {
    private static Logger logger = Logger.getLogger("importer");
    private static String appenderName = "dataImporter";

    public static void loadAppender(String filePath){
        FileAppender appender = (FileAppender)logger.getAppender(appenderName);
        appender.setFile(filePath);
        appender.activateOptions();
        logger.addAppender(appender);
    }
    public static void warn(String message) {

        logger.log(Level.WARN, message);
    }

    public static void info(String message){
        logger.log(Level.INFO, message);
    }

    public static void error(String message) {
        logger.log(Level.ERROR, message);
    }

    public static void error(Exception exception) {
        ByteArrayOutputStream stackTrace = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(stackTrace);
        exception.printStackTrace(printStream);
        printStream.flush();
        error(stackTrace.toString() + "\n");
    }
}
