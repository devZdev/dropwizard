package com.yammer.dropwizard.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.TimeZone;

import static com.yammer.dropwizard.config.LoggingConfiguration.*;

/**
 * Configuration for logging HTTP requests.
 */
@SuppressWarnings("UnusedDeclaration")
public class RequestLogConfiguration {

    /**
     * Configuration for logging requests to the console.
     */
    @NotNull
    @JsonProperty("console")
    private ConsoleConfiguration consoleConfiguration = new ConsoleConfiguration();

    /**
     * Configuration for logging requests to a file.
     */
    @NotNull
    @JsonProperty("file")
    private FileConfiguration fileConfiguration = new FileConfiguration();

    /**
     * Configuration for logging requests to syslog.
     */
    @NotNull
    @JsonProperty("syslog")
    private SyslogConfiguration syslogConfiguration = new SyslogConfiguration();

    /**
     * The {@link TimeZone} for the timestamps for log messages.
     * <p/>
     * This must be a valid timezone identifier, e.g. "<i>Europe/London</i>", "<i>EST</i>".
     *
     * @default UTC
     */
    @NotNull
    @JsonProperty
    private TimeZone timeZone = UTC;

    public ConsoleConfiguration getConsoleConfiguration() {
        return consoleConfiguration;
    }

    public void setConsoleConfiguration(ConsoleConfiguration consoleConfiguration) {
        this.consoleConfiguration = consoleConfiguration;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public void setFileConfiguration(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    public SyslogConfiguration getSyslogConfiguration() {
        return syslogConfiguration;
    }

    public void setSyslogConfiguration(SyslogConfiguration syslogConfiguration) {
        this.syslogConfiguration = syslogConfiguration;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }
}
