package com.springboot.springstudy.common.testing.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserTestReq(@NotNull @Min(value = 0) Integer seed) {
}
