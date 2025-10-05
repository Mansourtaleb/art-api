package com.esprit.artdigital_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyEmailRequest {
    @NotBlank(message = "Le code de vérification est requis")
    @Size(min = 6, max = 6, message = "Le code doit contenir 6 caractères")
    private String code;
}