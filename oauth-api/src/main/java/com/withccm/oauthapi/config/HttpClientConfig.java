package com.withccm.oauthapi.config;

import java.nio.charset.StandardCharsets;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {

    private static final int SOCKET_TIMEOUT = 5000;
    private static final int CONNECT_TIMEOUT = 5000;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate() {
            {
                setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient()));
                getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            }
        };
    }

    @Bean
    public CloseableHttpClient httpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(3, true));
        httpClientBuilder.setConnectionManager(connectionManager());
        httpClientBuilder.setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(SOCKET_TIMEOUT).build());
        httpClientBuilder.setDefaultRequestConfig(RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build());
        httpClientBuilder.disableCookieManagement();
        return httpClientBuilder.build();
    }

    @Bean
    public PoolingHttpClientConnectionManager connectionManager() {
        return new PoolingHttpClientConnectionManager() {
            {
                setMaxTotal(200);
                setDefaultMaxPerRoute(50);
            }
        };
    }

}
