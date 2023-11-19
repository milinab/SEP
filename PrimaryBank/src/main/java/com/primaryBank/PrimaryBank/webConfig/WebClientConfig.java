package com.primaryBank.PrimaryBank.webConfig;

import com.primaryBank.PrimaryBank.webClient.PccClient;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient pccWebClient() {

        HttpClient httpClient = HttpClient.create()
                .doOnConnected(conn -> conn
                        .addHandler(new ReadTimeoutHandler(20)) // 10 sekundi
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://localhost:8083")
                .build();
    }

    @Bean
    public PccClient primaryBankClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(pccWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(PccClient.class);
    }
}
