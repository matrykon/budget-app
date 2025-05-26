package org.example.pasir_matras_patryk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message = "Email nie może być pusty")
    private String email;

    @NotBlank(message = "Hasło nie może być puste")
    private String password;
}
