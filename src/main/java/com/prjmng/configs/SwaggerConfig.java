package com.prjmng.configs;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    // Pulled from application.yml
    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    private static final String OAUTH_SCHEME = "keycloak_oauth2";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME, keycloakSecurityScheme()))
                .addSecurityItem(new SecurityRequirement()
                        .addList(OAUTH_SCHEME));
    }

    private Info apiInfo() {
        return new Info()
                .title("Project Management API")
                .description("REST API for the Project Management Tool")
                .version("1.0.0")
                .contact(new Contact()
                        .name("Your Name")
                        .email("your@email.com"));
    }

    private SecurityScheme keycloakSecurityScheme() {
        // Keycloak OpenID Connect URLs
        String baseUrl = authServerUrl + "/realms/" + realm + "/protocol/openid-connect";

        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("Authenticate via Keycloak")
                .flows(new OAuthFlows()
                        // Authorization Code Flow — for Swagger UI login button
                        .authorizationCode(new OAuthFlow()
                                .authorizationUrl(baseUrl + "/auth")
                                .tokenUrl(baseUrl + "/token")
                                .refreshUrl(baseUrl + "/token")
                                .scopes(new Scopes()
                                        .addString("openid", "OpenID Connect scope")
                                        .addString("profile", "User profile info")
                                        .addString("email", "User email")))
                        // Password Flow — for direct testing in Swagger
                        .password(new OAuthFlow()
                                .tokenUrl(baseUrl + "/token")
                                .scopes(new Scopes()
                                        .addString("openid", "OpenID Connect scope")
                                        .addString("profile", "User profile info")
                                        .addString("email", "User email"))));
    }
}