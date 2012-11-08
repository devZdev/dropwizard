package com.yammer.dropwizard.config;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.yammer.dropwizard.validation.ValidationMethod;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Configuration for general application logging.
 */
@SuppressWarnings("UnusedDeclaration")
public class LoggingConfiguration {
    static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    public static class ConsoleConfiguration {

        /**
         * Whether to enable logging to the console.
         *
         * @default true
         */
        @JsonProperty
        private boolean enabled = true;

        /**
         * The log level threshold to log messages from.
         * <p/>
         * All messages of this level and above will appear in the log.
         * <p/>
         * Available levels are (in order, highest first):
         * <ul>
         *     <li>OFF</li>
         *     <li>ERROR</li>
         *     <li>WARN</li>
         *     <li>INFO</li>
         *     <li>DEBUG</li>
         *     <li>TRACE</li>
         *     <li>ALL</li>
         * </ul>
         *
         * @see Level
         * @default ALL
         */
        @NotNull
        @JsonProperty
        private Level threshold = Level.ALL;

        /**
         * The timezone for the timestamps for log messages.
         * <p/>
         * This must be a valid timezone identifier, e.g. "<i>Europe/London</i>", "<i>EST</i>".
         *
         * @default UTC
         */
        @NotNull
        @JsonProperty
        private TimeZone timeZone = UTC;

        /**
         * The format of log messages.
         * <p/>
         * The format must be a pattern that is valid for Logbacks' {@link
         * ch.qos.logback.classic.PatternLayout}.
         *
         * @see <a href="http://logback.qos.ch/manual/layouts.html#PatternLayout">PatternLayout</a>
         */
        @JsonProperty
        private String logFormat;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Level getThreshold() {
            return threshold;
        }

        public void setThreshold(Level threshold) {
            this.threshold = threshold;
        }

        public TimeZone getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
        }

        public Optional<String> getLogFormat() {
            return Optional.fromNullable(logFormat);
        }
    }

    public static class FileConfiguration {

        /**
         * Whether to enable logging to a file.
         *
         * @default true
         */
        @JsonProperty
        private boolean enabled = false;

        /**
         * The log level threshold to log messages from.
         * <p/>
         * All messages of this level and above will appear in the log.
         * <p/>
         * Available levels are (in order, highest first):
         * <ul>
         *     <li>OFF</li>
         *     <li>ERROR</li>
         *     <li>WARN</li>
         *     <li>INFO</li>
         *     <li>DEBUG</li>
         *     <li>TRACE</li>
         *     <li>ALL</li>
         * </ul>
         *
         * @see Level
         * @default ALL
         */
        @NotNull
        @JsonProperty
        private Level threshold = Level.ALL;

        /**
         * The full path to the file to log messages to.
         */
        @JsonProperty
        private String currentLogFilename;

        /**
         * Whether to enable archving of old log files.
         * <p/>
         * Archiving is achieved by "rolling" the log file based on the pattern defined by {@link
         * FileConfiguration#archivedLogFilenamePattern}.
         *
         * @default true
         */
        @JsonProperty
        private boolean archive = true;

        /**
         * The pattern to use for archived log files.
         * <p/>
         * The pattern must be a valid Logback {@link
         * ch.qos.logback.core.rolling.TimeBasedRollingPolicy} file name pattern.
         *
         * @see <a href="http://logback.qos.ch/manual/appenders.html#TimeBasedRollingPolicy">
         *     TimeBasedRollingPolicy</a>
         */
        @JsonProperty
        private String archivedLogFilenamePattern;

        /**
         * The maximum number of archived log files to retain.
         * <p/>
         * Once this limit is reached, old archived log files will be removed whenever a new log
         * file is archived.
         *
         * @default 5
         */
        @Min(1)
        @Max(50)
        @JsonProperty
        private int archivedFileCount = 5;

        /**
         * The timezone for the timestamps for log messages.
         * <p/>
         * This must be a valid timezone identifier, e.g. "<i>Europe/London</i>", "<i>EST</i>".
         *
         * @default UTC
         */
        @NotNull
        @JsonProperty
        private TimeZone timeZone = UTC;

        /**
         * The format of log messages.
         * <p/>
         * The format must be a pattern that is valid for Logbacks' {@link
         * ch.qos.logback.classic.PatternLayout}.
         *
         * @see <a href="http://logback.qos.ch/manual/layouts.html#PatternLayout">PatternLayout</a>
         */
        @JsonProperty
        private String logFormat;

        @ValidationMethod(message = "must have logging.file.archivedLogFilenamePattern if logging.file.archive is true")
        public boolean isValidArchiveConfiguration() {
            return !enabled || !archive || (archivedLogFilenamePattern != null);
        }

        @ValidationMethod(message = "must have logging.file.currentLogFilename if logging.file.enabled is true")
        public boolean isConfigured() {
            return !enabled || (currentLogFilename != null);
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Level getThreshold() {
            return threshold;
        }

        public void setThreshold(Level level) {
            this.threshold = level;
        }

        public String getCurrentLogFilename() {
            return currentLogFilename;
        }

        public void setCurrentLogFilename(String filename) {
            this.currentLogFilename = filename;
        }

        public boolean isArchive() {
            return archive;
        }

        public void setArchive(boolean archive) {
            this.archive = archive;
        }

        public int getArchivedFileCount() {
            return archivedFileCount;
        }

        public void setArchivedFileCount(int count) {
            this.archivedFileCount = count;
        }

        public String getArchivedLogFilenamePattern() {
            return archivedLogFilenamePattern;
        }

        public void setArchivedLogFilenamePattern(String pattern) {
            this.archivedLogFilenamePattern = pattern;
        }

        public TimeZone getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
        }

        public Optional<String> getLogFormat() {
            return Optional.fromNullable(logFormat);
        }

        public void setLogFormat(String logFormat) {
            this.logFormat = logFormat;
        }
    }

    public static class SyslogConfiguration {
        public enum Facility {
            AUTH, AUTHPRIV, DAEMON, CRON, FTP, LPR, KERN, MAIL, NEWS, SYSLOG, USER, UUCP,
            LOCAL0, LOCAL1, LOCAL2, LOCAL3, LOCAL4, LOCAL5, LOCAL6, LOCAL7;

            @Override
            @JsonValue
            public String toString() {
                return super.toString().replace("_", "+").toLowerCase(Locale.ENGLISH);
            }

            @JsonCreator
            public static Facility parse(String facility) {
                return valueOf(facility.toUpperCase(Locale.ENGLISH).replace('+', '_'));
            }
        }

        /**
         * Whether to enable logging to a file.
         *
         * @default true
         */
        @JsonProperty
        private boolean enabled = false;

        /**
         * The log level threshold to log messages from.
         * <p/>
         * All messages of this level and above will appear in the log.
         * <p/>
         * Available levels are (in order, highest first):
         * <ul>
         *     <li>OFF</li>
         *     <li>ERROR</li>
         *     <li>WARN</li>
         *     <li>INFO</li>
         *     <li>DEBUG</li>
         *     <li>TRACE</li>
         *     <li>ALL</li>
         * </ul>
         *
         * @default ALL
         */
        @NotNull
        @JsonProperty
        private Level threshold = Level.ALL;

        @NotNull
        @JsonProperty
        private String host = "localhost";

        @NotNull
        @JsonProperty
        private Facility facility = Facility.LOCAL0;

        /**
         * The timezone for the timestamps for log messages.
         * <p/>
         * This must be a valid timezone identifier, e.g. "<i>Europe/London</i>", "<i>EST</i>".
         *
         * @default UTC
         */
        @NotNull
        @JsonProperty
        private TimeZone timeZone = UTC;

        /**
         * The format of log messages.
         * <p/>
         * The format must be a pattern that is valid for Logbacks' {@link
         * ch.qos.logback.classic.PatternLayout}.
         *
         * @see <a href="http://logback.qos.ch/manual/layouts.html#PatternLayout">PatternLayout</a>
         */
        @JsonProperty
        private String logFormat;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Level getThreshold() {
            return threshold;
        }

        public void setThreshold(Level threshold) {
            this.threshold = threshold;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Facility getFacility() {
            return facility;
        }

        public void setFacility(Facility facility) {
            this.facility = facility;
        }

        public TimeZone getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(TimeZone timeZone) {
            this.timeZone = timeZone;
        }

        public Optional<String> getLogFormat() {
            return Optional.fromNullable(logFormat);
        }

        public void setLogFormat(String logFormat) {
            this.logFormat = logFormat;
        }
    }

    /**
     * The log level threshold to log messages from.
     * <p/>
     * All messages of this level and above will appear in the log.
     * <p/>
     * Available levels are (in order, highest first):
     * <ul>
     *     <li>OFF</li>
     *     <li>ERROR</li>
     *     <li>WARN</li>
     *     <li>INFO</li>
     *     <li>DEBUG</li>
     *     <li>TRACE</li>
     *     <li>ALL</li>
     * </ul>
     *
     * @see Level
     * @default ALL
     */
    @NotNull
    @JsonProperty
    private Level level = Level.INFO;

    /**
     * An {@link ImmutableMap} of custom log {@link Level}s for specific classes or packages.
     * <p/>
     * Provides fine-grained filtering on the {@link Level} of log messages for specific classes or
     * packages. The name of the mapping is the fully-qualified {@link Class} or {@link Package} for
     * the logger and the value is the minimum {@link Level} that messages must have in order to be
     * logged.
     */
    @NotNull
    @JsonProperty
    private ImmutableMap<String, Level> loggers = ImmutableMap.of();

    /**
     * Configuration for logging to the console.
     */
    @Valid
    @NotNull
    @JsonProperty("console")
    private ConsoleConfiguration consoleConfiguration = new ConsoleConfiguration();

    /**
     * Configuration for logging to a file.
     */
    @Valid
    @NotNull
    @JsonProperty("file")
    private FileConfiguration fileConfiguration = new FileConfiguration();

    /**
     * Configuration for logging to syslog.
     */
    @Valid
    @NotNull
    @JsonProperty("syslog")
    private SyslogConfiguration syslogConfiguration = new SyslogConfiguration();

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public ImmutableMap<String, Level> getLoggers() {
        return loggers;
    }

    public void setLoggers(Map<String, Level> loggers) {
        this.loggers = ImmutableMap.copyOf(loggers);
    }

    public ConsoleConfiguration getConsoleConfiguration() {
        return consoleConfiguration;
    }

    public void setConsoleConfiguration(ConsoleConfiguration config) {
        this.consoleConfiguration = config;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public void setFileConfiguration(FileConfiguration config) {
        this.fileConfiguration = config;
    }

    public SyslogConfiguration getSyslogConfiguration() {
        return syslogConfiguration;
    }

    public void setSyslogConfiguration(SyslogConfiguration config) {
        this.syslogConfiguration = config;
    }
}
