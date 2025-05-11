package org.example.digital_banking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreditDebitRequestDTO {
    private Long accountId;
    private double amount;
    private String description;
}