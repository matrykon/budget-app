package org.example.pasir_matras_patryk.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.pasir_matras_patryk.model.TransactionType;

@Getter
@Setter
public class TransactionDTO {

    @NotNull(message = "Kwota nie może być pusta")
    @Min(value = 1, message = "Kwota musi być większa niż 0")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Typ transakcji nie może być pusty")
    private TransactionType type;

    @Size(max = 30, message = "Tagi nie mogą przekraczać 30 znaków")
    @NotBlank(message = "Tag nie może być pusty")
    private String tags;

    @Size(max = 255, message = "Notatki nie mogą przekraczać 255 znaków")
    private String notes;
}
