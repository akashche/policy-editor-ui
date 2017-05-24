package net.sourceforge.jnlp.util.logging;

/**
 * User: alexkasko
 * Date: 5/24/17
 */
public class OutputController {
    public static Logger getLogger() {
        return new Logger();
    }

    public static class Logger {

        public void log(Level level, Object message) {
            System.out.println(level.toString() + ": " + message);
        }

        public void log(String message) {
            System.out.println("DEFAULT: " + message);
        }

        public void log(Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public enum Level {
        WARNING_DEBUG,
        ERROR_DEBUG,
        ERROR_ALL
    }
}
