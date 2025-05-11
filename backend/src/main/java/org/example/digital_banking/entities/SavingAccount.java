package org.example.digital_banking.entities;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Entity

@Data
@DiscriminatorValue("SAV")
@AllArgsConstructor @NoArgsConstructor
public class SavingAccount  extends  BankAccount{
    private double interestRate;
}
