package com.yammer.dropwizard.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.File;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")

/**
 * Configuration for a socket/connector secured with SSL (Secure Socket Layer).
 */
public class SslConfiguration {

    /**
     * The path to the SSL Key Store.
     */
    @JsonProperty
    private File keyStore = null;

    /**
     * The password used to access the SSL Key Store file.
     */
    @JsonProperty
    private String keyStorePassword = null;

    /**
     * The password used to access the certificate's key entry in the Key Store.
     */
    @JsonProperty
    private String keyManagerPassword = null;

    /**
     * The type of the SSL Key Store.
     * <p/>
     * See the KeyStore section in the <a href=
     * "{@docRoot}/../technotes/guides/security/StandardNames.html#KeyStore">
     * Java Cryptography Architecture Standard Algorithm Name Documentation</a>
     * for information about standard keystore types.
     *
     * @default JKS
     */
    @JsonProperty
    private String keyStoreType = "JKS";

    /**
     * The alias to use for the SSL Certificate.
     */
    @JsonProperty
    private String certAlias = null;

    /**
     * The list of supported SSL protocols.
     * <p/>
     * Protocols omitted from this list cannot be used.
     *
     * @default SSLv3, TLSv1, TLSv1.1, TLSv1.2
     */
    @NotEmpty
    @JsonProperty
    private ImmutableList<String> supportedProtocols = ImmutableList.of("SSLv3",
                                                                        "TLSv1",
                                                                        "TLSv1.1",
                                                                        "TLSv1.2");

    public Optional<File> getKeyStore() {
        return Optional.fromNullable(keyStore);
    }

    public void setKeyStore(File keyStore) {
        this.keyStore = keyStore;
    }

    public Optional<String> getKeyStorePassword() {
        return Optional.fromNullable(keyStorePassword);
    }

    public void setKeyStorePassword(String password) {
        this.keyStorePassword = password;
    }

    public Optional<String> getKeyManagerPassword() {
        return Optional.fromNullable(keyManagerPassword);
    }

    public void setKeyManagerPassword(String keyManagerPassword) {
        this.keyManagerPassword = keyManagerPassword;
    }

    public Optional<String> getKeyStoreType() {
        return Optional.fromNullable(keyStoreType);
    }

    public void setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
    }

    public Optional<String> getCertAlias() {
        return Optional.fromNullable(certAlias);
    }

    public void setCertAlias(String alias) {
        this.certAlias = alias;
    }

    public ImmutableList<String> getSupportedProtocols() {
        return supportedProtocols;
    }

    public void setSupportedProtocols(List<String> protocols) {
        this.supportedProtocols = ImmutableList.copyOf(protocols);
    }
}
