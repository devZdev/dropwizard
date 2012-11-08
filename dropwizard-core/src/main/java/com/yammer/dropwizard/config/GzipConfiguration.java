package com.yammer.dropwizard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.yammer.dropwizard.util.Size;

import java.util.Set;

/**
 * Configuration for the compression of HTTP entities with the GZIP algorithm.
 */
@SuppressWarnings("UnusedDeclaration")
public class GzipConfiguration {

    /**
     * Whether to enable GZIP compression.
     *
     * @default true
     */
    @JsonProperty
    private boolean enabled = true;

    /**
     * The optional minimum {@link Size} threshold for entities to require compression.
     * <p/>
     * Entities that are smaller than this threshold will not be compressed.
     */
    @JsonProperty
    private Size minimumEntitySize = null;

    /**
     * The {@link Size} of the GZIP compression buffer.
     */
    @JsonProperty
    private Size bufferSize = null;

    /**
     * An optional {@link ImmutableSet set} of user agents to exclude from compression.
     * <p/>
     * Entities destined for clients with any of the defined user agents will never be compressed.
     * <p/>
     * When omitted, all user agents are eligible for compression.
     */
    @JsonProperty
    private ImmutableSet<String> excludedUserAgents = null;

    /**
     * An optional {@link ImmutableSet set} of MIME types to compress.
     * <p/>
     * When defined, only entities of one of the defined MIME types will be compressed. All other
     * entities will not be compressed.
     * <p/>
     * When omitted, all entities will be eligible for compression, except entities with the MIME
     * type <i>"application/gzip"</i>
     */
    @JsonProperty
    private ImmutableSet<String> compressedMimeTypes = null;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Optional<Size> getMinimumEntitySize() {
        return Optional.fromNullable(minimumEntitySize);
    }

    public void setMinimumEntitySize(Size size) {
        this.minimumEntitySize = size;
    }

    public Optional<Size> getBufferSize() {
        return Optional.fromNullable(bufferSize);
    }

    public void setBufferSize(Size size) {
        this.bufferSize = size;
    }

    public Optional<ImmutableSet<String>> getExcludedUserAgents() {
        return Optional.fromNullable(excludedUserAgents);
    }

    public void setExcludedUserAgents(Set<String> userAgents) {
        this.excludedUserAgents = (userAgents == null) ? null : ImmutableSet.copyOf(userAgents);
    }

    public Optional<ImmutableSet<String>> getCompressedMimeTypes() {
        return Optional.fromNullable(compressedMimeTypes);
    }

    public void setCompressedMimeTypes(Set<String> mimeTypes) {
        this.compressedMimeTypes = (mimeTypes == null) ? null : ImmutableSet.copyOf(mimeTypes);
    }
}
