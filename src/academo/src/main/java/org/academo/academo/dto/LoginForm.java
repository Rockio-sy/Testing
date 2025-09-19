package org.academo.academo.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginForm(@NotBlank(message = "Username cannot be null") String username,
                        @NotBlank(message = "Password cannot be empty") String password) {
}
