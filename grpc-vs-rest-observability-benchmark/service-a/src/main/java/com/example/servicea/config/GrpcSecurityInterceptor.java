package com.example.servicea.config;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class GrpcSecurityInterceptor implements ServerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GrpcSecurityInterceptor.class);
    private static final Metadata.Key<String> AUTHORIZATION_HEADER =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    private final AuthenticationManager authenticationManager;

    public GrpcSecurityInterceptor(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {

        String authHeader = headers.get(AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            call.close(
                    Status.UNAUTHENTICATED.withDescription("Missing or invalid Authorization header format. Expected 'Bearer <password>'"),
                    new Metadata()
            );
            return new ServerCall.Listener<ReqT>() {};
        }

        String password = authHeader.substring(7).trim();

        Authentication authResult;
        try {
            UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken("user", password);
            authResult = authenticationManager.authenticate(authRequest);
        } catch (Exception e) {
            log.error("gRPC authentication failed: {}", e.getMessage());
            call.close(
                    Status.UNAUTHENTICATED.withDescription("Authentication failed: Invalid credentials"),
                    new Metadata()
            );
            return new ServerCall.Listener<ReqT>() {};
        }

        ServerCall.Listener<ReqT> delegate = next.startCall(call, headers);

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(delegate) {
            @Override
            public void onHalfClose() {
                runWithSecurityContext(super::onHalfClose);
            }

            @Override
            public void onMessage(ReqT message) {
                runWithSecurityContext(() -> super.onMessage(message));
            }

            private void runWithSecurityContext(Runnable action) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authResult);
                SecurityContextHolder.setContext(context);
                try {
                    action.run();
                } finally {
                    SecurityContextHolder.clearContext();
                }
            }
        };
    }
}