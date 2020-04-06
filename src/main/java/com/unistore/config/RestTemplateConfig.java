package com.unistore.config;

import java.util.concurrent.TimeUnit;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("deprecation")
@Configuration
public class RestTemplateConfig {


  @Bean

  public AsyncRestTemplate asyncRestTemplate() {
    final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
    requestFactory.setTaskExecutor(new SimpleAsyncTaskExecutor());
    requestFactory.setConnectTimeout(100);
    requestFactory.setReadTimeout(100);

    AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
    asyncRestTemplate.setAsyncRequestFactory(requestFactory);
    return asyncRestTemplate;
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    PoolingHttpClientConnectionManager connectionManger = new PoolingHttpClientConnectionManager();
    connectionManger.closeExpiredConnections();
    connectionManger.closeIdleConnections(1, TimeUnit.MILLISECONDS);
    connectionManger.setDefaultMaxPerRoute(10);
    connectionManger.setMaxTotal(10);
    connectionManger.setValidateAfterInactivity(100);

    CloseableHttpClient httpClient =
        HttpClientBuilder.create().setConnectionTimeToLive(10, TimeUnit.SECONDS)
            .setConnectionManager(connectionManger).build();
    ClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory(httpClient);
    RestTemplate restTemplate = restTemplateBuilder.build();
    restTemplate.setRequestFactory(requestFactory);
    return restTemplate;

  }
}
