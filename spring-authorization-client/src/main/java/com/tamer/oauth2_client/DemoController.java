package com.tamer.oauth2_client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private OAuth2AuthorizedClientManager clientManager;

    @Value("${authorization.server.registration-id}")
    private String registrationId;

    @Value("${authorization.server.client-id}")
    private String clientId;

    @GetMapping("/token")
    public String token(){
        OAuth2AuthorizeRequest request =
                OAuth2AuthorizeRequest
                        .withClientRegistrationId(registrationId)
                        .principal(clientId).build();

        OAuth2AuthorizedClient client = clientManager.authorize(request);

        return client.getAccessToken().getTokenValue();
    }
}
