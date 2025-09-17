package com.tamer.oauth2_client_login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class SecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain asFilterChain(HttpSecurity http) throws Exception{
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(
                c -> c
                        .requestMatchers("/oauth2/token", "/oauth2/introspect", "/.well-known/**").permitAll() // Allow public access to OAuth2 endpoints
                        .anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(){

        // Client (used by Client application) to obtain Opaque access tokens
        RegisteredClient opaqueClientCredentialsClient =
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("client")
                        .clientSecret("{noop}secret")
                        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .tokenSettings(TokenSettings.builder().accessTokenFormat(OAuth2TokenFormat.REFERENCE)
                                .accessTokenTimeToLive(Duration.ofHours(12)).build())
                        .scope("CUSTOM")
                        .build();

        // Client (used by resource server) to introspect Opaque tokens
        RegisteredClient opaqueResourceServer =
                RegisteredClient.withId(UUID.randomUUID().toString())
                        .clientId("resource_server_client")
                        .clientSecret("{noop}resource_server_secret")
                        .clientAuthenticationMethod(
                                ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                        .authorizationGrantType(
                                AuthorizationGrantType.CLIENT_CREDENTIALS)
                        .build();

        return new InMemoryRegisteredClientRepository(opaqueClientCredentialsClient, opaqueResourceServer);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder().build();
    }
}
