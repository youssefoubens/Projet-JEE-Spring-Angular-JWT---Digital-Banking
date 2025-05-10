package org.example.digital_banking.entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.digital_banking.enums.AccountStatus;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type",length = 4)


@AllArgsConstructor
@NoArgsConstructor
public abstract class BankAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idBankAccount;
    private double balance;
    private AccountStatus status;
    private String currency;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
