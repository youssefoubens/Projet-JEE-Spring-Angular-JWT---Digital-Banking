package org.example.digital_banking.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {
    private Long id;
    private double balance;
    private Date createdAt;
    private String status;
    private CustomerDTO customerDTO;
    private String type; // "SAV" or "CUR"
}