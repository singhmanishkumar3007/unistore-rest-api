package com.unistore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;

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
}
