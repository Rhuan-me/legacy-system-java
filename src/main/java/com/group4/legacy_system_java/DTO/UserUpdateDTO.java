package com.group4.legacy_system_java.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @NotBlank(message = "The name must not be empty")
        String nome,

        @NotBlank(message = "The login must not be empty")
        @Size(min = 3, max = 100)
        String login,

        @NotBlank(message = "The password must not be empty.")
        @Size(min = 8, message = "The password must have at least 8 characters.")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
                message = "The password must contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
        String password
        )
{
}
