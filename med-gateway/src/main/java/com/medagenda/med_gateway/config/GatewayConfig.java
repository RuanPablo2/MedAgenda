package com.medagenda.med_gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class GatewayConfig {

    @Value("${route.auth.url}")
    private String authServiceUrl;

    @Value("${route.appointment.url}")
    private String appointmentServiceUrl;

    @Value("${route.clinical.url}")
    private String clinicalServiceUrl;

    @Value("${route.document.url}")
    private String documentServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> authRoute() {
        System.out.println("🚦 [MED GATEWAY] Registering routes for Auth Service: " + authServiceUrl);

        return route("auth-service-route")
                .route(RequestPredicates.path("/api/v1/auth/**")
                        .or(RequestPredicates.path("/api/v1/users/**")), http())
                .before(uri(authServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> appointmentRoute() {
        System.out.println("🚦 [MED GATEWAY] Registering routes for Appointment Service: " + appointmentServiceUrl);

        return route("appointment-service-route")
                .route(RequestPredicates.path("/api/v1/appointments/**")
                        .or(RequestPredicates.path("/api/v1/catalog/**"))
                        .or(RequestPredicates.path("/api/v1/patients/**")), http())
                .before(uri(appointmentServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> clinicalRoute() {
        System.out.println("🚦 [MED GATEWAY] Registering routes for Clinical Service: " + clinicalServiceUrl);

        return route("clinical-service-route")
                .route(RequestPredicates.path("/api/v1/clinical/**"), http())
                .before(uri(clinicalServiceUrl))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> documentRoute() {
        System.out.println("🚦 [MED GATEWAY] Registering routes for Document Service: " + documentServiceUrl);

        return route("document-service-route")
                .route(RequestPredicates.path("/api/v1/documents/**"), http())
                .before(uri(documentServiceUrl))
                .build();
    }
}