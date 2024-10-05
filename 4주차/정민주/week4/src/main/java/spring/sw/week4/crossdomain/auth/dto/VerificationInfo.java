package spring.sw.week4.crossdomain.auth.dto;

import jakarta.validation.constraints.NotNull;

public record VerificationInfo(
        @NotNull
        String email,
        @NotNull
        String phone,
        @NotNull
        String password
) {}
