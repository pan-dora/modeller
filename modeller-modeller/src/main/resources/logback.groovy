import ch.qos.logback.classic.encoder.PatternLayoutEncoder

import static ch.qos.logback.classic.Level.DEBUG

def LOG_PATH = "/tmp/modeller-logs"

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n"
    }
}

appender("File-Appender", FileAppender) {
    file = "${LOG_PATH}/logfile.log"
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{5} - %msg%n"
    }
}

logger("org.apache.http", DEBUG, ["File-Appender"], false)
root(DEBUG, ["STDOUT"])
