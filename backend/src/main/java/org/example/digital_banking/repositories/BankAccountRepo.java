package org.example.digital_banking.repositories;

import org.example.digital_banking.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {

    // You can add custom queries here if needed
}
