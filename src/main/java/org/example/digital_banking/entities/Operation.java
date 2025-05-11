package org.example.digital_banking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.digital_banking.enums.Operation_type;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Operation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Operation_type operationType;
    private double amount;
    private Date operationDate;
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idBankAccount")
    private BankAccount bankAccount;
}
