package org.example.digital_banking.repositories;

import org.example.digital_banking.entities.BankAccount;
import org.example.digital_banking.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OperationRepo extends JpaRepository<Operation, Long> {

    // Get all operations by BankAccount entity
    List<Operation> findByBankAccount(BankAccount bankAccount);

    // Get all operations by bank account ID
    @Query("SELECT o FROM Operation o WHERE o.bankAccount.idBankAccount = :accountId")
    List<Operation> findByBankAccountId(Long accountId);



    // Delete all operations by bank account ID
    @Modifying
    @Transactional
    @Query("DELETE FROM Operation o WHERE o.bankAccount.idBankAccount = :id")
    void deleteByBankAccountId(Long id);
}
