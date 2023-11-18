package com.pcc.PCC.webConfig;

import com.pcc.PCC.webClient.PrimaryBankClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientConfig {

    @Bean
    public WebClient primaryBankWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8082")
                .build();
    }

    @Bean
    public PrimaryBankClient primaryBankClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(primaryBankWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(PrimaryBankClient.class);
    }
}
