package com.yammer.dropwizard.config;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;

import java.util.TimeZone;

import static com.yammer.dropwizard.config.LoggingConfiguration.*;

@SuppressWarnings({ "FieldMayBeFinal", "FieldCanBeLocal" })
/**
 * Configuration for logging HTTP requests.
 */
public class RequestLogConfiguration {

    /**
     * Configuration for logging requests to the console.
     */
    @NotNull
    @JsonProperty
    protected ConsoleConfiguration console = new ConsoleConfiguration();

    /**
     * Configuration for logging requests to a file.
     */
    @NotNull
    @JsonProperty
    protected FileConfiguration file = new FileConfiguration();

    /**
     * Configuration for logging requests to syslog.
     */
    @NotNull
    @JsonProperty
    protected SyslogConfiguration syslog = new SyslogConfiguration();

    /**
     * The timezone for the timestamps in the request log.
     */
    @NotNull
    @JsonProperty
    protected TimeZone timeZone = UTC;

    public ConsoleConfiguration getConsoleConfiguration() {
        return console;
    }

    public FileConfiguration getFileConfiguration() {
        return file;
    }

    public SyslogConfiguration getSyslogConfiguration() {
        return syslog;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }
}
