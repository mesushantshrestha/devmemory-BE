package com.shrestha.devmemory_BE.config;

import jakarta.servlet.http.HttpServletRequest;
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
                                            ClientRegistrationRepository repo) throws Exception {

        var successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl(targetUrl);
        successHandler.setAlwaysUseDefaultTargetUrl(true);

        OAuth2AuthorizationRequestResolver resolver = customAuthorizationRequestResolver(repo);

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
                        .authorizationEndpoint(a -> a.authorizationRequestResolver(resolver))
                        .successHandler(successHandler)
                );

        return http.build();
    }

    private OAuth2AuthorizationRequestResolver customAuthorizationRequestResolver(
            ClientRegistrationRepository repo
    ) {
        // Normal: /oauth2/authorization/{registrationId}
        DefaultOAuth2AuthorizationRequestResolver normal =
                new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization");

        // Switch-account: /oauth2/authorization-switch/{registrationId}
        DefaultOAuth2AuthorizationRequestResolver switchResolver =
                new DefaultOAuth2AuthorizationRequestResolver(repo, "/oauth2/authorization-switch");

        // Only switch endpoint forces account chooser
        switchResolver.setAuthorizationRequestCustomizer(builder ->
                builder.additionalParameters(params -> params.put("prompt", "select_account"))
        );

        return new DelegatingAuthRequestResolver(normal, switchResolver);
    }

    static class DelegatingAuthRequestResolver implements OAuth2AuthorizationRequestResolver {
        private final OAuth2AuthorizationRequestResolver normal;
        private final OAuth2AuthorizationRequestResolver switchResolver;

        DelegatingAuthRequestResolver(OAuth2AuthorizationRequestResolver normal,
                                      OAuth2AuthorizationRequestResolver switchResolver) {
            this.normal = normal;
            this.switchResolver = switchResolver;
        }

        @Override
        public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
            String uri = request.getRequestURI();
            if (uri != null && uri.startsWith("/oauth2/authorization-switch/")) {
                return switchResolver.resolve(request);
            }
            return normal.resolve(request);
        }

        @Override
        public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
            String uri = request.getRequestURI();
            if (uri != null && uri.startsWith("/oauth2/authorization-switch/")) {
                return switchResolver.resolve(request, clientRegistrationId);
            }
            return normal.resolve(request, clientRegistrationId);
        }
    }
}

