package com.example.servicea.config;

import io.grpc.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

public class GrpcSecurityInterceptor implements ServerInterceptor {
    private static final Metadata.Key<String> AUTHORIZATION_HEADER =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String authHeader = headers.get(AUTHORIZATION_HEADER);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Validate token and populate Spring Security Context
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(token, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        try {
            return next.startCall(call, headers);
        } finally {
            SecurityContextHolder.clearContext(); // Prevent context leaking on Virtual Threads
        }
    }
}
