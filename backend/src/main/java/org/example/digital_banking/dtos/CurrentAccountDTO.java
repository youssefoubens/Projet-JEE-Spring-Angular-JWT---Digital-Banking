package org.example.digital_banking.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAccountDTO extends BankAccountDTO {
    private double overdraft;
}