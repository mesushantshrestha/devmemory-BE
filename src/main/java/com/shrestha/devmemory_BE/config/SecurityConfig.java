package com.shrestha.devmemory_BE.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.util.function.Consumer;

@Configuration
public class SecurityConfig {

    @Value("${app.oauth2.success-redirect}")
    private String targetUrl;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            ClientRegistrationRepository clientRegistrationRepository) throws Exception {

        var successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl(targetUrl);
        successHandler.setAlwaysUseDefaultTargetUrl(true);

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/oauth2/**", "/login/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth -> oauth
                        .authorizationEndpoint(authorization -> authorization
                                .authorizationRequestResolver(
                                        googleAccountChooserResolver(clientRegistrationRepository)
                                )
                        )
                        .successHandler(successHandler)
                );

        return http.build();
    }

    private OAuth2AuthorizationRequestResolver googleAccountChooserResolver(
            ClientRegistrationRepository repo
    ) {
        var resolver = new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization");

        Consumer<OAuth2AuthorizationRequest.Builder> customizer = builder ->
                builder.additionalParameters(params -> {
                    // ✅ always show account chooser
                    params.put("prompt", "select_account");

                    // Optional:
//                     params.put("access_type", "offline");
//                     params.put("include_granted_scopes", "true");
                });

        resolver.setAuthorizationRequestCustomizer(customizer);
        return resolver;
    }
}
