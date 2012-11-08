package com.yammer.dropwizard.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.yammer.dropwizard.util.Duration;
import com.yammer.dropwizard.util.Size;
import com.yammer.dropwizard.validation.ValidationMethod;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Map;

/**
 * An object representation of the {@code http} section of the YAML configuration file.
 */
@SuppressWarnings("UnusedDeclaration")
public class HttpConfiguration {
    public enum ConnectorType {
        BLOCKING,
        LEGACY,
        LEGACY_SSL,
        NONBLOCKING,
        NONBLOCKING_SSL;

        @Override
        @JsonValue
        public String toString() {
            return super.toString().replace("_", "+").toLowerCase(Locale.ENGLISH);
        }

        @JsonCreator
        public static ConnectorType parse(String type) {
            return valueOf(type.toUpperCase(Locale.ENGLISH).replace('+', '_'));
        }
    }

    /**
     * Configuration for the HTTP request log.
     */
    @Valid
    @NotNull
    @JsonProperty("requestLog")
    private RequestLogConfiguration requestLogConfiguration = new RequestLogConfiguration();

    /**
     * Configuration for GZip compression of requests and responses.
     */
    @Valid
    @NotNull
    @JsonProperty("gzip")
    private GzipConfiguration gzipConfiguration = new GzipConfiguration();

    /**
     * Configuration for SSL (Secure Socket Layer).
     * <p/>
     * When defined, all connections to the configured HTTP server must be secured with SSL.
     * <p/>
     * When omitted, SSL connections are not supported (default).
     */
    @Valid
    @JsonProperty("ssl")
    private SslConfiguration sslConfiguration = null;

    @NotNull
    @JsonProperty
    private ImmutableMap<String, String> contextParameters = ImmutableMap.of();

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
    private int port = 8080;

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
    private int adminPort = 8081;

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
    private int maxThreads = 254;

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
    private int minThreads = 8;

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
    private String rootPath = "/*";
    
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
    @JsonProperty
    private ConnectorType connectorType = ConnectorType.BLOCKING;

    /**
     * The maximum time a connection may remain idle before being closed by the server.
     *
     * @default 200 seconds
     */
    @NotNull
    @JsonProperty
    private Duration maxIdleTime = Duration.seconds(200);

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
    private int acceptorThreads = 1;

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
    private int acceptorThreadPriorityOffset = 0;

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
    private int acceptQueueSize = -1;

    /**
     * The maximum number of buffers to use for requests and responses.
     *
     * @default 1024
     * @min 1
     */
    @Min(1)
    @JsonProperty
    private int maxBufferCount = 1024;

    /**
     * The {@link Size} of each request buffer.
     *
     * @default 16KB
     */
    @NotNull
    @JsonProperty
    private Size requestBufferSize = Size.kilobytes(16);

    /**
     * The {@link Size} of each request header buffer.
     *
     * @default 6KB
     */
    @NotNull
    @JsonProperty
    private Size requestHeaderBufferSize = Size.kilobytes(6);

    /**
     * The {@link Size} of each response buffer.
     *
     * @default 32KB
     */
    @NotNull
    @JsonProperty
    private Size responseBufferSize = Size.kilobytes(32);

    /**
     * The {@link Size} of each response header buffer.
     *
     * @default 6KB
     */
    @NotNull
    @JsonProperty
    private Size responseHeaderBufferSize = Size.kilobytes(6);

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
    private boolean reuseAddress = true;

    /**
     * The time to keep a socket around for after the connection is closed.
     */
    @JsonProperty
    private Duration soLingerTime = null;

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
    private int lowResourcesConnectionThreshold = 0;

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
    private Duration lowResourcesMaxIdleTime = Duration.seconds(0);

    /**
     * The maximum time to wait, during shutdown, for currently executing requests to complete.
     *
     * @default 2 seconds
     */
    @NotNull
    @JsonProperty
    private Duration shutdownGracePeriod = Duration.seconds(2);

    /**
     * Whether to send the {@code Server} header in responses.
     * <p/>
     * The {@code Server} header contains details of the server software and version that may not be
     * desirable to send to clients.
     *
     * @default false
     */
    @JsonProperty
    private boolean useServerHeader = false;

    /**
     * Whether to send the {@code Date} header in responses.
     * <p/>
     * The {@code Date} header indicates the time and date (incl. timezone) that the response was
     * generated at.
     *
     * @default true
     */
    @JsonProperty
    private boolean useDateHeader = true;

    /**
     * Whether to use headers forwarded by a proxy.
     * <p/>
     * When true, if the {@code X-Forwarded-For} headers are set in the request, they will be used
     * to identify the client instead of the originating proxy's headers.
     *
     * @default true
     */
    @JsonProperty
    private boolean useForwardedHeaders = true;

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
    private boolean useDirectBuffers = true;

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
    private String bindHost = null;

    /**
     * The username to require for the integrated administrative HTTP interface.
     * <p/>
     * When defined, all access to the administrative HTTP interface on {@link
     * HttpConfiguration#adminPort} needs to be authenticated by this user and the {@link
     * HttpConfiguration#adminPassword}.
     */
    @JsonProperty
    private String adminUsername = null;

    /**
     * The password to require for the integrated administrative HTTP interface.
     * <p/>
     * When defined, all access to the administrative HTTP interface on {@link
     * HttpConfiguration#adminPort} needs to be authenticated by this password and the {@link
     * HttpConfiguration#adminUsername}.
     */
    @JsonProperty
    private String adminPassword = null;

    @ValidationMethod(message = "must have an SSL configuration when using SSL connection")
    public boolean isSslConfigured() {
        final ConnectorType type = getConnectorType();
        return !((sslConfiguration == null) && ((type == ConnectorType.LEGACY_SSL) ||
                                   (type == ConnectorType.NONBLOCKING_SSL)));
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
        return requestLogConfiguration;
    }

    public void setRequestLogConfiguration(RequestLogConfiguration config) {
        this.requestLogConfiguration = config;
    }

    public GzipConfiguration getGzipConfiguration() {
        return gzipConfiguration;
    }

    public void setGzipConfiguration(GzipConfiguration config) {
        this.gzipConfiguration = config;
    }

    public SslConfiguration getSslConfiguration() {
        return sslConfiguration;
    }

    public void setSslConfiguration(SslConfiguration config) {
        this.sslConfiguration = config;
    }

    public ImmutableMap<String, String> getContextParameters() {
        return contextParameters;
    }

    public void setContextParameters(Map<String, String> contextParameters) {
        this.contextParameters = ImmutableMap.copyOf(contextParameters);
    }

    public ConnectorType getConnectorType() {
        return connectorType;
    }

    public void setConnectorType(ConnectorType type) {
        this.connectorType = type;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getAdminPort() {
        return adminPort;
    }

    public void setAdminPort(int port) {
        this.adminPort = port;
    }

    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int count) {
        this.maxThreads = count;
    }

    public int getMinThreads() {
        return minThreads;
    }

    public void setMinThreads(int count) {
        this.minThreads = count;
    }

    public Duration getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(Duration duration) {
        this.maxIdleTime = duration;
    }

    public int getAcceptorThreads() {
        return acceptorThreads;
    }

    public void setAcceptorThreads(int count) {
        this.acceptorThreads = count;
    }

    public int getAcceptorThreadPriorityOffset() {
        return acceptorThreadPriorityOffset;
    }

    public void setAcceptorThreadPriorityOffset(int priorityOffset) {
        this.acceptorThreadPriorityOffset = priorityOffset;
    }

    public int getAcceptQueueSize() {
        return acceptQueueSize;
    }

    public void setAcceptQueueSize(int size) {
        this.acceptQueueSize = size;
    }

    public int getMaxBufferCount() {
        return maxBufferCount;
    }

    public void setMaxBufferCount(int count) {
        this.maxBufferCount = count;
    }

    public Size getRequestBufferSize() {
        return requestBufferSize;
    }

    public void setRequestBufferSize(Size size) {
        this.requestBufferSize = size;
    }

    public Size getRequestHeaderBufferSize() {
        return requestHeaderBufferSize;
    }

    public void setRequestHeaderBufferSize(Size size) {
        this.requestHeaderBufferSize = size;
    }

    public Size getResponseBufferSize() {
        return responseBufferSize;
    }

    public void setResponseBufferSize(Size size) {
        this.responseBufferSize = size;
    }

    public Size getResponseHeaderBufferSize() {
        return responseHeaderBufferSize;
    }

    public void setResponseHeaderBufferSize(Size size) {
        this.responseHeaderBufferSize = size;
    }

    public boolean isReuseAddressEnabled() {
        return reuseAddress;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public Optional<Duration> getSoLingerTime() {
        return Optional.fromNullable(soLingerTime);
    }

    public void setSoLingerTime(Duration duration) {
        this.soLingerTime = duration;
    }

    public int getLowResourcesConnectionThreshold() {
        return lowResourcesConnectionThreshold;
    }

    public void setLowResourcesConnectionThreshold(int connectionCount) {
        this.lowResourcesConnectionThreshold = connectionCount;
    }

    public Duration getLowResourcesMaxIdleTime() {
        return lowResourcesMaxIdleTime;
    }

    public void setLowResourcesMaxIdleTime(Duration duration) {
        this.lowResourcesMaxIdleTime = duration;
    }

    public Duration getShutdownGracePeriod() {
        return shutdownGracePeriod;
    }

    public void setShutdownGracePeriod(Duration duration) {
        this.shutdownGracePeriod = duration;
    }

    public boolean useForwardedHeaders() {
        return useForwardedHeaders;
    }

    public void setUseForwardedHeaders(boolean useForwardedHeaders) {
        this.useForwardedHeaders = useForwardedHeaders;
    }

    public boolean useDirectBuffers() {
        return useDirectBuffers;
    }

    public void setUseDirectBuffers(boolean useDirectBuffers) {
        this.useDirectBuffers = useDirectBuffers;
    }

    public Optional<String> getBindHost() {
        return Optional.fromNullable(bindHost);
    }

    public void setBindHost(String host) {
        this.bindHost = host;
    }

    public boolean isDateHeaderEnabled() {
        return useDateHeader;
    }

    public void setUseDateHeader(boolean useDateHeader) {
        this.useDateHeader = useDateHeader;
    }

    public boolean isServerHeaderEnabled() {
        return useServerHeader;
    }

    public void setUseServerHeader(boolean useServerHeader) {
        this.useServerHeader = useServerHeader;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String path) {
        this.rootPath = path;
    }

    public Optional<String> getAdminUsername() {
        return Optional.fromNullable(adminUsername);
    }

    public void setAdminUsername(String username) {
        this.adminUsername = username;
    }

    public Optional<String> getAdminPassword() {
        return Optional.fromNullable(adminPassword);
    }

    public void setAdminPassword(String password) {
        this.adminPassword = password;
    }
}
