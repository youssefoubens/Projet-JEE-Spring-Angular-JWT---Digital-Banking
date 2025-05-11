package org.example.digital_banking.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.digital_banking.enums.Operation_type;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private Operation_type operationType;
    private String description;
}