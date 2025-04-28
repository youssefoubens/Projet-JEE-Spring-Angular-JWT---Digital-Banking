package org.example.digital_banking.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity



public abstract class BankAccount {
    @Id
    private Long id;


}
