package com.minor.store.utils.log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.spi.AbstractLogger;
import org.apache.logging.log4j.spi.ExtendedLoggerWrapper;

public class LogWrapper {

    public static LogWrapper create(Class<?> clazz) {
        LogWrapper logWrapper = new LogWrapper();
        Logger wrapped = LogManager.getLogger(clazz);
        logWrapper.mLogger = new ExtendedLoggerWrapper((AbstractLogger)wrapped,wrapped.getName(),wrapped.getMessageFactory()); //ACNLogger.create(clazz);
        return logWrapper;
    }

    public void debugMask(String message, Object... arguments) {
        mLogger.debug(message, this.mask(arguments));
    }

    public void infoMask(String message, Object... arguments) {
        mLogger.info(message, this.mask(arguments));
    }

    public void info(String message, Object... arguments) {
        mLogger.info(message, arguments);
    }

    public void error(String message, Exception e) {
        mLogger.error(message, e);
    }


    private Object[] mask(Object... arguments) {

        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] instanceof ILogMask) {
                arguments[i] = ((ILogMask) arguments[i]).toLog();
            }
        }

        return arguments;
    }

    private ExtendedLoggerWrapper mLogger;
}
