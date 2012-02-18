package com.yammer.dropwizard.cli;

import com.yammer.dropwizard.AbstractService;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.lifecycle.ServerLifeCycle;
import com.yammer.dropwizard.logging.Log;
import org.apache.commons.cli.CommandLine;

// TODO: 10/12/11 <coda> -- write tests for ServerCommand

/**
 * Runs a service as an HTTP server.
 *
 * @param <T> the {@link Configuration} subclass which is loaded from the configuration file
 */
public class ServerCommand<T extends Configuration> extends ConfiguredCommand<T> {
    private final Class<T> configurationClass;

    /**
     * Creates a new {@link ServerCommand} with the given configuration class.
     *
     * @param configurationClass    the configuration class the YAML file is parsed as
     */
    public ServerCommand(Class<T> configurationClass) {
        super("server", "Starts an HTTP server running the service");
        this.configurationClass = configurationClass;
    }

    /*
     * Since we don't subclass ServerCommand, we need a concrete reference to the configuration
     * class.
     */
    @Override
    protected Class<T> getConfigurationClass() {
        return configurationClass;
    }

    @Override
    protected void run(AbstractService<T> service,
                       T configuration,
                       CommandLine params) throws Exception {
        ServerLifeCycle lifeCycle = new ServerLifeCycle<T>(service, configuration);

        try {
            lifeCycle.init().start().join();
        } catch (Exception e) {
            final Log log = Log.forClass(ServerCommand.class);
            log.error(e, "Unable to start server, shutting down");
            lifeCycle.stop();
        }
    }
}
