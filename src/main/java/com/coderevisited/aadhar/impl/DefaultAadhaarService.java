package com.coderevisited.aadhar.impl;

import com.coderevisited.aadhar.api.AadhaarService;
import com.coderevisited.aadhar.api.ConnectionSettings;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Author : Suresh
 * Date : 07/06/15.
 */
public class DefaultAadhaarService implements AadhaarService {


    private final CloseableHttpClient httpClient;

    public DefaultAadhaarService(ConnectionSettings settings) {

        this.httpClient = HttpClientFactory.create(settings);
    }

    @Override
    public String registerDemoAuthRequest(String body) throws IOException {

        HttpPost post = new HttpPost("auth/raw");
        return executeAndGetResponseBody(post, body);
    }


    private String executeAndGetResponseBody(final HttpEntityEnclosingRequestBase request, String body) throws IOException {
        return EntityUtils.toString(executeRequest(request, body).getEntity());
    }


    private CloseableHttpResponse executeRequest(final HttpEntityEnclosingRequestBase request, String body) throws IOException {
        final CloseableHttpResponse response;

        request.setHeader(HTTP.CONTENT_TYPE, "application/json; charset=utf-8");
        request.setEntity(new StringEntity(body));
        response = httpClient.execute(request);
        final int code = response.getStatusLine().getStatusCode();
        if (code != HttpStatus.SC_OK) {
            throw new IOException("Http Status code " + code);
        }
        return response;

    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }


}
