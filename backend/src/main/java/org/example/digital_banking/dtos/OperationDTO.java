package org.example.digital_banking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.digital_banking.enums.Operation_type;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperationDTO {
    private Long id;
    private Operation_type operationType;
    private double amount;
    private Long accountId;
}
