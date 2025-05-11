package org.example.digital_banking.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity
@DiscriminatorValue("Cur")
@Data

@AllArgsConstructor
@NoArgsConstructor
public class CurrentAccount extends BankAccount {
    private double overdraft;

}
