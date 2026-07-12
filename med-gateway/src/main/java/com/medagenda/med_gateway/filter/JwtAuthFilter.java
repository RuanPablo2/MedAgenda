package com.medagenda.med_gateway.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class JwtAuthFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Value("${api.security.token.secret}")
    private String secret;

    @Override
    public ServerResponse filter(ServerRequest request, HandlerFunction<ServerResponse> next) throws Exception {
        String path = request.path();
        String method = request.method().name();

        if (isPublicRoute(path, method)) {
            return next.handle(request);
        }

        String authHeader = request.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("❌ [GATEWAY] Blocked: Missing or malformed token on route " + path);
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.replace("Bearer ", "");

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            DecodedJWT decodedJWT = JWT.require(algorithm)
                    .withIssuer("med-auth-service")
                    .build()
                    .verify(token);

            String userId = String.valueOf(decodedJWT.getClaim("userId").asLong());
            String role = decodedJWT.getClaim("role").asString();

            System.out.println("✅ [GATEWAY] Valid token for UserID: " + userId + " | Role: " + role);

            ServerRequest mutatedRequest = ServerRequest.from(request)
                    .header("X-User-Id", userId)
                    .header("X-User-Role", role)
                    .build();

            return next.handle(mutatedRequest);

        } catch (JWTVerificationException exception) {
            System.out.println("❌ [GATEWAY] Blocked: Invalid or expired token. Detail: " + exception.getMessage());
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    private boolean isPublicRoute(String path, String method) {
        if (path.equals("/api/v1/users") && method.equals("POST")) {
            return true;
        }
        return path.equals("/api/v1/auth/login");
    }
}