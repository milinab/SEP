package com.psp.PSPBackend.config;

import com.psp.PSPBackend.webClient.ApiGatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {

    @Autowired
    private LoadBalancedExchangeFilterFunction filterFunction;

    @Bean
    public WebClient apiGatewayWebClient() {
        return WebClient.builder()
                .baseUrl("http://apigateway")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public ApiGatewayClient apiGatewayClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(apiGatewayWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(ApiGatewayClient.class);
    }
}
