package org.example.digital_banking.repositories;

import org.example.digital_banking.entities.BankAccount;
import org.example.digital_banking.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepo extends JpaRepository<Operation,Long> {
    List<Operation> findByBankAccount(BankAccount bankAccount);

}
