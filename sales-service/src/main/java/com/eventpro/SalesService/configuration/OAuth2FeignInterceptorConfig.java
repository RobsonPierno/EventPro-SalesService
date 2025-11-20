package com.eventpro.SalesService.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

import feign.RequestInterceptor;

@Configuration
public class OAuth2FeignInterceptorConfig {

    private final OAuth2AuthorizedClientManager clientManager;

    public OAuth2FeignInterceptorConfig(OAuth2AuthorizedClientManager clientManager) {
        this.clientManager = clientManager;
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return template -> {
            OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                    .withClientRegistrationId("sales-client")
                    .principal("sales-service")
                    .build();

            OAuth2AuthorizedClient client = clientManager.authorize(request);

            if (client == null) {
                throw new IllegalStateException("Unable to retrieve access token");
            }

            template.header(
                    "Authorization",
                    "Bearer " + client.getAccessToken().getTokenValue()
            );
        };
    }
}
