package com.group4.legacy_system_java.DTO;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
    @NotBlank(message = "The login must not be empty")
    String login,

    @NotBlank(message = "The password must not be empty")
    String password){ }

