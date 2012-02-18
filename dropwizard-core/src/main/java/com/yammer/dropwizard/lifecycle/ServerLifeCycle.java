package com.yammer.dropwizard.lifecycle;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.yammer.dropwizard.AbstractService;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.config.ServerFactory;
import com.yammer.dropwizard.logging.Log;
import org.eclipse.jetty.server.Server;

/**
 * Controls the lifecycle of a Service's Server.
 * 
 * This serves as a convenient abstraction for application containers (e.g. 
 * {@link com.yammer.dropwizard.cli.ServerCommand}) to manage a Server for a
 * Service.
 */
public class ServerLifeCycle<T extends Configuration> {
    private final Log log = Log.forClass(ServerLifeCycle.class);

    private final AbstractService<T> service;
    private final T configuration;
    
    private Server server = null;
    private ServerLifeCycle.State state = State.DESTROYED;

    public enum State {
        RUNNING, STOPPED, DESTROYED
    }

    public ServerLifeCycle(AbstractService<T> service, T configuration) throws Exception {
        this.service = service;
        this.configuration = configuration;
    }

    /**
     * Get the Server this ServerLifeCycle manages.
     *
     * It is preferable to control the server through {@link ServerLifeCycle}
     * rather than  directly through the Server instance.
     * 
     * @return the Server this ServerLifeCycle is managing or null if no Server 
     *   has been initialized.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Gets the current state of this ServerLifeCycle.
     * 
     * New ServerLifeCycles that have yet to be initialized will have a state of
     * DESTROYED. Call {@link ServerLifeCycle#init()} to initialize them.
     * 
     * @return Either DESTROYED, STOPPED or RUNNING.
     */
    public State getState() {
        return state;
    }

    /**
     * Destroys the Server.
     *
     * If the Server is currently running, it will first be stopped.
     *
     * NOTE: This will destroy the Server's underlying LifeCycle, so it must be
     * re-initialized once it has been destroyed.
     *
     * @return This ServerLifeCycle, for method chaining.
     *
     * @throws Exception
     */
    public ServerLifeCycle<T> destroy() throws Exception{
        if (state == State.RUNNING) {
            doStop();
            doDestroy();
        } else if (state == State.STOPPED) {
            doDestroy();
        }
        
        state = State.DESTROYED;
        
        return this;
    }

    /**
     * Initializes this Server.
     *
     * @return This ServerLifeCycle, for method chaining.
     *
     * @throws Exception
     */
    public ServerLifeCycle<T> init() throws Exception {
        if (state == State.DESTROYED) {
            doInit();
        }
        
        state = State.STOPPED;
        
        return this;
    }

    /**
     * Starts the Server.
     * 
     * If the Server has not been initialized, it will first be initialized.
     *
     * @return This ServerLifeCycle, for method chaining.
     *
     * @throws Exception
     */
    public ServerLifeCycle<T> start() throws Exception {
        if (state == State.DESTROYED) {
            doInit();
            doStart();
        } else if (state == State.STOPPED) {
            doStart();
        }
        
        state = State.RUNNING;
        
        return this;
    }

    /**
     * Stops the Server.
     *
     * If the Server has not been initialized, it will first be initialized.
     *
     * @return This ServerLifeCycle, for method chaining.
     *
     * @throws Exception
     */
    public ServerLifeCycle<T> stop() throws Exception {
        if (state == State.DESTROYED) {
            doInit();
        } else if (state == State.RUNNING) {
            doStop();
        }

        state = State.STOPPED;
        
        return this;
    }

    /**
     * Joins the Server, blocking the current thread until it stops.
     *
     * @return This ServerLifeCycle, for method chaining.
     *
     * @throws Exception
     */
    public ServerLifeCycle<T> join() throws Exception {
        if (state != State.RUNNING) {
            start();
        }
        
        server.join();
        
        return this;
    }
    
    private void doDestroy() throws Exception {
        server.destroy();
    }
    
    private void doInit() throws Exception {
        // initialize Service environment and build a server, but don't start it
        final Environment environment = new Environment(configuration, service);
        service.initializeWithBundles(configuration, environment);
        server = new ServerFactory(configuration.getHttpConfiguration()).buildServer(environment);
    }
    
    private void doStart() throws Exception {
        try {
            final String banner = Resources.toString(Resources.getResource("banner.txt"), Charsets.UTF_8);
            log.info("Starting {}\n{}", service.getName(), banner);
        } catch (IllegalArgumentException ignored) {
            // don't display the banner if there isn't one
            log.info("Starting {}", service.getName());
        }

        try {
            server.start();
        } catch (Exception e) {
            // failed to start, explicitly stop it just to keep things consistent
            server.stop();
            throw e;
        }
    }
    
    private void doStop() throws Exception {
        server.stop();
    }
}
