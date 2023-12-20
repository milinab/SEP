package com.pcc.PCC.webConfig;

import com.pcc.PCC.webClient.PrimaryBankClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private LoadBalancedExchangeFilterFunction filterFunction;
    @Bean
    public WebClient primaryBankWebClient() {
        return WebClient.builder()
                .baseUrl("http://secondarybank")
                .filter(filterFunction)
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
