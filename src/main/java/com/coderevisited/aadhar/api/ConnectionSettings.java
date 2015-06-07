package com.coderevisited.aadhar.api;

import java.net.URI;

/**
 * Author : Suresh
 * Date : 07/06/15.
 */
public class ConnectionSettings {

    public String KL_HOST_URL = "https://ac.khoslalabs.com/hackgate/hackathon/";
    private URI endpoint;
    private int maxConnections = 50;
    private int socketTimeout = 60 * 1000;
    private long connectionTTL = -1;
    private int connectionTimeout = 50 * 1000;

    public ConnectionSettings() {
        this.endpoint = URI.create(KL_HOST_URL);
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setConnectionTTL(long connectionTTL) {
        this.connectionTTL = connectionTTL;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setEndpoint(String url) {
        this.endpoint = URI.create(url);
    }

    public URI getEndpoint() {
        return endpoint;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public long getConnectionTTL() {
        return connectionTTL;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }


}
