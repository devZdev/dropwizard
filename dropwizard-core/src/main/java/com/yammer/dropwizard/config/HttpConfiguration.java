package com.yammer.dropwizard.config;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.yammer.dropwizard.util.Duration;
import com.yammer.dropwizard.util.Size;
import com.yammer.dropwizard.validation.ValidationMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Configuration for a Dropwizard HTTP server.
 */
@SuppressWarnings({"FieldCanBeLocal", "FieldMayBeFinal", "CanBeFinal"})
public class HttpConfiguration {

    /**
     * Configuration for the HTTP request log.
     */
    @Valid
    @NotNull
    @JsonProperty
    protected RequestLogConfiguration requestLog = new RequestLogConfiguration();

    /**
     * Configuration for GZip compression of requests and responses.
     */
    @Valid
    @NotNull
    @JsonProperty
    protected GzipConfiguration gzip = new GzipConfiguration();

    /**
     * Configuration for SSL (Secure Socket Layer).
     * <p/>
     * When defined, all connections to the configured HTTP server must be secured with SSL.
     * <p/>
     * When omitted, SSL connections are not supported (default).
     */
    @Valid
    @JsonProperty
    protected SslConfiguration ssl = null;

    @NotNull
    @JsonProperty
    protected ImmutableMap<String, String> contextParameters = ImmutableMap.of();

    public enum ConnectorType {
        SOCKET,
        BLOCKING_CHANNEL,
        SELECT_CHANNEL,
        SOCKET_SSL,
        SELECT_CHANNEL_SSL
    }

    /**
     * The port the HTTP server should bind to for application requests.
     * <p/>
     * As Java has no native way to de-escalate privileges (i.e. by switching user), ports that
     * require escalated privileges (0-1024) are not permitted.
     *
     * @default {@code 8080}
     * @min 1025
     * @max 65535
     */
    @Min(1025)
    @Max(65535)
    @JsonProperty
    protected int port = 8080;

    /**
     * The port the HTTP server should bind to for administration requests.
     * <p/>
     * As Java has no native way to de-escalate privileges (i.e. by switching user), ports that
     * require escalated privileges (0-1024) are not permitted.
     *
     * @default 8081
     * @min 1025
     * @max 65535
     */
    @Min(1025)
    @Max(65535)
    @JsonProperty
    protected int adminPort = 8081;

    /**
     * The maximum number of {@link Thread}s to use to handle HTTP requests.
     * <p/>
     * The thread-pool for handling requests may not exceed this many threads.
     * <p/>
     * The behavior of the server once this limit is reached is dependent on the configured {@link
     * HttpConfiguration#connectorType}.
     *
     * @default 254
     * @min 2
     * @max 1000000
     * @see HttpConfiguration#connectorType
     */
    @Min(2)
    @Max(1000000)
    @JsonProperty
    protected int maxThreads = 254;

    /**
     * The minimum number of {@link Thread}s to keep around for handling HTTP requests.
     * <p/>
     * The thread-pool for handling requests must retain at least this many threads.
     *
     * @default 8
     * @min 1
     * @max 1000000
     */
    @Min(1)
    @Max(1000000)
    @JsonProperty
    protected int minThreads = 8;

    /**
     * The root path for all requests this HTTP server will handle.
     * <p/>
     * Requests to resources outside this path are ignored, as they are considered to be destined
     * for a different HTTP server.
     *
     * @default /*
     */
    @NotNull
    @JsonProperty
    protected String rootPath = "/*";

    /**
     * The type of connector to handle HTTP requests with.
     * <p/>
     * Available options:
     * <dl>
     *     <dt><i>blocking</i></dt>
     *     <dd>One thread per connection. Concurrent client <i>connections</i> bounded by {@link
     *     HttpConfiguration#maxThreads}.</dd>
     *
     *     <dt><i>nonblocking</i></dt>
     *     <dd>Threads are allocated to requests, allowing clients to maintain idle connections
     *     without tying up a server thread. Concurrent <i>requests</i> bounded by {@link
     *     HttpConfiguration#maxThreads}.</dd>
     *
     *     <dt><i>nonblocking+ssl</i></dt>
     *     <dd>As above, but for SSL connections.</dd>
     *
     *     <dt><i>legacy</i></dt>
     *     <dd>Like "blocking", but uses the legacy {@link java.net.Socket} API.</dd>
     *
     *     <dt><i>legacy+ssl</i></dt>
     *     <dd>As above, but for SSL connections.</dd>
     * </dl>
     *
     * @default blocking
     */
    @NotNull
    @Pattern(regexp = "^(blocking|nonblocking|nonblocking\\+ssl|legacy|legacy\\+ssl)$",
             flags = {Pattern.Flag.CASE_INSENSITIVE})
    @JsonProperty
    protected String connectorType = "blocking";

    /**
     * The maximum time a connection may remain idle before being closed by the server.
     *
     * @default 200 seconds
     */
    @NotNull
    @JsonProperty
    protected Duration maxIdleTime = Duration.seconds(200);

    /**
     * The number of {@link Thread}s to use for accepting new connections.
     * <p/>
     * Once a new connection has been accepted, it is handed off to the configured {@link
     * HttpConfiguration#connectorType} for handling requests.
     *
     * @default 1
     * @min 1
     * @max 128
     */
    @Min(1)
    @Max(128)
    @JsonProperty
    protected int acceptorThreadCount = 1;

    /**
     * The amount to offset the {@link Thread} priority for accepting new connections.
     * <p/>
     * Positive values favor accepting new connections over handling already dispatched
     * connections.
     * <p/>
     * Negative values favor handling already dispatched connections over accepting new
     * connections.
     *
     * @default 0
     * @min -5
     * @max 5
     */
    @Min(-Thread.NORM_PRIORITY)
    @Max(Thread.NORM_PRIORITY)
    @JsonProperty
    protected int acceptorThreadPriorityOffset = 0;

    /**
     * The maximum size of the queue of accepted connections that are waiting to be handled by the
     * configured {@link HttpConfiguration#connectorType}.
     * <p/>
     * To leave the queue unbounded, set to {@code -1}.
     *
     * @default -1
     * @min -1
     */
    @Min(-1)
    @JsonProperty
    protected int acceptQueueSize = -1;

    /**
     * The maximum number of buffers to use for requests and responses.
     *
     * @default 1024
     * @min 1
     */
    @Min(1)
    @JsonProperty
    protected int maxBufferCount = 1024;

    /**
     * The {@link Size} of each request buffer.
     *
     * @default 16KB
     */
    @NotNull
    @JsonProperty
    protected Size requestBufferSize = Size.kilobytes(16);

    /**
     * The {@link Size} of each request header buffer.
     *
     * @default 6KB
     */
    @NotNull
    @JsonProperty
    protected Size requestHeaderBufferSize = Size.kilobytes(6);

    /**
     * The {@link Size} of each response buffer.
     *
     * @default 32KB
     */
    @NotNull
    @JsonProperty
    protected Size responseBufferSize = Size.kilobytes(32);

    /**
     * The {@link Size} of each response header buffer.
     *
     * @default 6KB
     */
    @NotNull
    @JsonProperty
    protected Size responseHeaderBufferSize = Size.kilobytes(6);

    /**
     * Whether to allow other sockets to bind to the {@link HttpConfiguration#port} and {@link
     * HttpConfiguration#adminPort} when this server is not actively listening on them.
     * <p/>
     * Normally, when a process ends, the kernel will close the bound sockets but keep the port(s)
     * in a state that blocks new processes from binding to the port(s). This ensures that when a
     * process ends, its port will be immediately released for other processes to bind to.
     * <p/>
     * It's recommended to keep this enabled to allow your application to restart without
     * encountering <i>"Address already in use"</i> errors.
     *
     * @default true
     */
    @JsonProperty
    protected boolean reuseAddress = true;

    /**
     * The time to keep a socket around for after the connection is closed.
     */
    @JsonProperty
    protected Duration soLingerTime = null;

    /**
     * The total number of concurrent connections that triggers "low-resource mode" for the {@code
     * nonblocking} {@link HttpConfiguration#connectorType}.
     * <p/>
     * If the threshold is set to {@code 0}, it is disabled.
     * <p/>
     * This option has no effect for other connectors.
     *
     * @default 0
     */
    @JsonProperty
    protected int lowResourcesConnectionThreshold = 0;

    /**
     * The {@link HttpConfiguration#maxIdleTime maximum idle time} for connections when
     * "low-resource mode" has been {@link HttpConfiguration#lowResourcesConnectionThreshold
     * triggered}.
     * <p/>
     * As low-resource mode indicates that there are too many concurrent connections, it is a good
     * idea to set this to a substantially lower time than for {@link
     * HttpConfiguration#maxIdleTime}.
     *
     * @default 0 seconds
     */
    @NotNull
    @JsonProperty
    protected Duration lowResourcesMaxIdleTime = Duration.seconds(0);

    /**
     * The maximum time to wait, during shutdown, for currently executing requests to complete.
     *
     * @default 2 seconds
     */
    @NotNull
    @JsonProperty
    protected Duration shutdownGracePeriod = Duration.seconds(2);

    /**
     * Whether to send the {@code Server} header in responses.
     * <p/>
     * The {@code Server} header contains details of the server software and version that may not be
     * desirable to send to clients.
     *
     * @default false
     */
    @JsonProperty
    protected boolean useServerHeader = false;

    /**
     * Whether to send the {@code Date} header in responses.
     * <p/>
     * The {@code Date} header indicates the time and date (incl. timezone) that the response was
     * generated at.
     *
     * @default true
     */
    @JsonProperty
    protected boolean useDateHeader = true;

    /**
     * Whether to use headers forwarded by a proxy.
     * <p/>
     * When true, if the {@code X-Forwarded-For} headers are set in the request, they will be used
     * to identify the client instead of the originating proxy's headers.
     *
     * @default true
     */
    @JsonProperty
    protected boolean useForwardedHeaders = true;

    /**
     * Whether to use direct buffers for non-legacy {@link HttpConfiguration#connectorType}s.
     * <p/>
     * When enabled, the buffers used for requests, responses and headers will be allocated
     * off-heap.
     * <p/>
     * When disabled, these buffers will be allocated on-heap (inside the JVM), contributing to the
     * memory and GC footprint of the application.
     *
     * @default true
     */
    @JsonProperty
    protected boolean useDirectBuffers = true;

    /**
     * The host to bind this HTTP server to.
     * <p/>
     * When specified, this HTTP server will only handle requests that define the host in their
     * {@code Host} header.
     * <p/>
     * This is useful if your application contains multiple configured HTTP servers, bound for
     * different hosts.
     */
    @JsonProperty
    protected String bindHost = null;

    /**
     * The username to require for the integrated administrative HTTP interface.
     * <p/>
     * When defined, all access to the administrative HTTP interface on {@link
     * HttpConfiguration#adminPort} needs to be authenticated by this user and the {@link
     * HttpConfiguration#adminPassword}.
     */
    @JsonProperty
    protected String adminUsername = null;

    /**
     * The password to require for the integrated administrative HTTP interface.
     * <p/>
     * When defined, all access to the administrative HTTP interface on {@link
     * HttpConfiguration#adminPort} needs to be authenticated by this password and the {@link
     * HttpConfiguration#adminUsername}.
     */
    @JsonProperty
    protected String adminPassword = null;

    @ValidationMethod(message = "must have an SSL configuration when using SSL connection")
    public boolean isSslConfigured() {
        final ConnectorType type = getConnectorType();
        return !((ssl == null) && ((type == ConnectorType.SOCKET_SSL) ||
                                   (type == ConnectorType.SELECT_CHANNEL_SSL)));
    }

    @ValidationMethod(message = "must have a smaller minThreads than maxThreads")
    public boolean isThreadPoolSizedCorrectly() {
        return minThreads <= maxThreads;
    }
    
    @ValidationMethod(message = "must have adminUsername if adminPassword is defined")
    public boolean isAdminUsernameDefined() {
        return (adminPassword == null) || (adminUsername != null);
    }

    public RequestLogConfiguration getRequestLogConfiguration() {
        return requestLog;
    }

    public GzipConfiguration getGzipConfiguration() {
        return gzip;
    }

    public SslConfiguration getSslConfiguration() {
        return ssl;
    }

    public ImmutableMap<String, String> getContextParameters() {
        return contextParameters;
    }
    
    public ConnectorType getConnectorType() {
        if ("blocking".equalsIgnoreCase(connectorType)) {
            return ConnectorType.BLOCKING_CHANNEL;
        } else if ("legacy".equalsIgnoreCase(connectorType)) {
            return ConnectorType.SOCKET;
        } else if ("legacy+ssl".equalsIgnoreCase(connectorType)) {
            return ConnectorType.SOCKET_SSL;
        } else if ("nonblocking".equalsIgnoreCase(connectorType)) {
            return ConnectorType.SELECT_CHANNEL;
        } else if ("nonblocking+ssl".equalsIgnoreCase(connectorType)) {
            return ConnectorType.SELECT_CHANNEL_SSL;
        } else {
            throw new IllegalStateException("Invalid connector type: " + connectorType);
        }
    }

    public int getPort() {
        return port;
    }

    public int getAdminPort() {
        return adminPort;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public int getMinThreads() {
        return minThreads;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public int getAcceptorThreadCount() {
        return acceptorThreadCount;
    }

    public int getAcceptorThreadPriorityOffset() {
        return acceptorThreadPriorityOffset;
    }

    public int getAcceptQueueSize() {
        return acceptQueueSize;
    }

    public int getMaxBufferCount() {
        return maxBufferCount;
    }

    public Size getRequestBufferSize() {
        return requestBufferSize;
    }

    public Size getRequestHeaderBufferSize() {
        return requestHeaderBufferSize;
    }

    public Size getResponseBufferSize() {
        return responseBufferSize;
    }

    public Size getResponseHeaderBufferSize() {
        return responseHeaderBufferSize;
    }

    public boolean isReuseAddressEnabled() {
        return reuseAddress;
    }

    public Optional<Duration> getSoLingerTime() {
        return Optional.fromNullable(soLingerTime);
    }

    public int getLowResourcesConnectionThreshold() {
        return lowResourcesConnectionThreshold;
    }

    public Duration getLowResourcesMaxIdleTime() {
        return lowResourcesMaxIdleTime;
    }

    public Duration getShutdownGracePeriod() {
        return shutdownGracePeriod;
    }

    public boolean useForwardedHeaders() {
        return useForwardedHeaders;
    }

    public boolean useDirectBuffers() {
        return useDirectBuffers;
    }

    public Optional<String> getBindHost() {
        return Optional.fromNullable(bindHost);
    }

    public boolean isDateHeaderEnabled() {
        return useDateHeader;
    }

    public boolean isServerHeaderEnabled() {
        return useServerHeader;
    }

    public String getRootPath() {
        return rootPath;
    }

    public Optional<String> getAdminUsername() {
        return Optional.fromNullable(adminUsername);
    }

    public Optional<String> getAdminPassword() {
        return Optional.fromNullable(adminPassword);
    }
}
