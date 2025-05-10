package org.example.digital_banking.repositories;

import org.example.digital_banking.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepo extends JpaRepository<Operation,Long> {

}
