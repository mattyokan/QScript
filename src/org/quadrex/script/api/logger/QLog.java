package org.quadrex.script.api.logger;

import org.quadrex.script.ScriptPlugin;

import java.util.function.Supplier;
import java.util.logging.Level;

public class QLog {

    private static Level minimumLogLevel = Level.INFO;

    /**
     * Log a message.
     * @param level The log level to use.
     * @param message Supply a message.
     */
    public static void log(Level level, Supplier<String> message) {
        if(level.intValue() >= minimumLogLevel.intValue()) {
            ScriptPlugin.getInstance().getLogger().log(level, message.get());
        }
    }

    /**
     * Logs a message as info.
     * @param message Supply a message.
     */
    public static void log(Supplier<String> message) {
        log(Level.INFO, message);
    }

    /**
     * Special designation for error.
     * @param message Supply a message.
     */
    public static void error(Supplier<String> message) {
        log(Level.WARNING, () -> " [ERROR] " + message.get());
    }

    /**
     * (W)hat a (T)errible (F)ailure. An error that should never happen.
     * @param message Supply a message.
     */
    public static void wtf(Supplier<String> message) {
        log(Level.SEVERE, () -> " [WTF] " + message.get());
    }

}
