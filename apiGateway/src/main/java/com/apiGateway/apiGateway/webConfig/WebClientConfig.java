package com.apiGateway.apiGateway.webConfig;

import com.apiGateway.apiGateway.webClient.PSPClient;
import com.apiGateway.apiGateway.webClient.PccClient;
import com.apiGateway.apiGateway.webClient.PrimaryBankClient;
import com.apiGateway.apiGateway.webClient.SecondaryBankClient;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Autowired
    private LoadBalancedExchangeFilterFunction filterFunction;

    @Bean
    public WebClient pspWebClient() {

        HttpClient httpClient = HttpClient.create()
                .doOnConnected(conn -> conn
                        .addHandler(new ReadTimeoutHandler(20)) // 10 sekundi
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://psp")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public PSPClient pspClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(pspWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(PSPClient.class);
    }

    @Bean
    public WebClient primaryBankWebClient() {

        HttpClient httpClient = HttpClient.create()
                .doOnConnected(conn -> conn
                        .addHandler(new ReadTimeoutHandler(20)) // 10 sekundi
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://primarybank")
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

    @Bean
    public WebClient pccWebClient() {

        HttpClient httpClient = HttpClient.create()
                .doOnConnected(conn -> conn
                        .addHandler(new ReadTimeoutHandler(20)) // 10 sekundi
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://pcc")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public PccClient pccClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(pccWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(PccClient.class);
    }

    @Bean
    public WebClient secondaryBankWebClient() {

        HttpClient httpClient = HttpClient.create()
                .doOnConnected(conn -> conn
                        .addHandler(new ReadTimeoutHandler(20)) // 10 sekundi
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("http://secondarybank")
                .filter(filterFunction)
                .build();
    }

    @Bean
    public SecondaryBankClient secondaryBankClient() {
        HttpServiceProxyFactory httpServiceProxyFactory
                = HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(secondaryBankWebClient()))
                .build();
        return httpServiceProxyFactory.createClient(SecondaryBankClient.class);
    }

    @Bean
        public WebClient payPalWebClient() {

            HttpClient httpClient = HttpClient.create()
                    .doOnConnected(conn -> conn
                            .addHandler(new ReadTimeoutHandler(20)) // 10 sekundi
                    );

            return WebClient.builder()
                    .clientConnector(new ReactorClientHttpConnector(httpClient))
                    .baseUrl("http://paypal")
                    .filter(filterFunction)
                    .build();
        }

        @Bean
        public PayPalClient payPalClient() {
            HttpServiceProxyFactory httpServiceProxyFactory
                    = HttpServiceProxyFactory
                    .builder(WebClientAdapter.forClient(payPalWebClient()))
                    .build();
            return httpServiceProxyFactory.createClient(PayPalClient.class);
        }
}
