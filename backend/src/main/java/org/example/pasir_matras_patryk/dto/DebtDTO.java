package org.example.pasir_matras_patryk.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebtDTO {
    private Long debtorId;
    private Long creditorId;
    private Long groupId;
    private Double amount;
    private String title;
}
