package com.coderevisited.aadhar.impl;

import com.coderevisited.aadhar.api.ConnectionSettings;
import org.apache.http.*;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.TimeUnit;

/**
 * Author : Suresh
 * Date : 07/06/15.
 */
public class HttpClientFactory {

    public static CloseableHttpClient create(ConnectionSettings settings) {

        final SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).
                setSoTimeout(settings.getSocketTimeout()).build();
        final ConnectionConfig connectionConfig = ConnectionConfig.custom().setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8).build();

        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(settings.getConnectionTTL(), TimeUnit.MILLISECONDS);
        cm.setDefaultMaxPerRoute(settings.getMaxConnections());
        cm.setMaxTotal(settings.getMaxConnections());
        cm.setDefaultSocketConfig(socketConfig);
        cm.setDefaultConnectionConfig(connectionConfig);

        final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(settings.getConnectionTimeout())
                .setConnectionRequestTimeout(settings.getConnectionTimeout()).setSocketTimeout(settings.getSocketTimeout())
                .setCookieSpec(CookieSpecs.DEFAULT).build();

        return HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig)
                .setRoutePlanner(new DefaultHostRoutePlanner(settings))
                .setRequestExecutor(new DefaultEndpointHttpRequestExecutor(settings))
                .setUserAgent("Aadhaar based Authenticated System")
                .disableAutomaticRetries()
                .build();


    }

    private static final class DefaultHostRoutePlanner extends DefaultRoutePlanner {

        private final HttpHost defaultHost;

        public DefaultHostRoutePlanner(final SchemePortResolver schemePortResolver, final ConnectionSettings settings) {
            super(schemePortResolver);
            this.defaultHost = URIUtils.extractHost(settings.getEndpoint());

        }

        public DefaultHostRoutePlanner(ConnectionSettings settings) {
            this(DefaultSchemePortResolver.INSTANCE, settings);
        }

        @Override
        public HttpRoute determineRoute(HttpHost host, HttpRequest request, HttpContext context) throws HttpException {
            final HttpHost target = host != null ? host : defaultHost;
            return super.determineRoute(target, request, context);
        }
    }

    private static class DefaultEndpointHttpRequestExecutor extends HttpRequestExecutor {


        private final URI baseURI;

        public DefaultEndpointHttpRequestExecutor(final ConnectionSettings settings) {
            this.baseURI = settings.getEndpoint();
        }


        @Override
        protected HttpResponse doSendRequest(HttpRequest request, HttpClientConnection conn, HttpContext context) throws IOException, HttpException {
            final HttpRequestWrapper newRequest = HttpRequestWrapper.wrap(request);
            final URI newURL = combine(baseURI, newRequest.getURI());
            newRequest.setURI(newURL);
            return super.doSendRequest(newRequest, conn, context);
        }

        private URI combine(URI baseURI, URI relative) {

            if (relative.isAbsolute()) {
                return relative;
            }
            String basePath = baseURI.getPath();
            if (!basePath.endsWith("/")) {
                basePath = basePath + "/";
            }
            String relativePath = relative.toString();
            if (relativePath.startsWith("/")) {
                relativePath = relativePath.substring(1);
            }
            final URI path = URI.create(basePath + relativePath);
            return path.normalize();
        }
    }

}
