package org.example.digital_banking.services;

import org.example.digital_banking.dtos.*;
import java.util.List;

public interface CustomerServiceinterface {
    // Customer management
    List<CustomerDTO> getAllClients();
    CustomerDTO getClientById(Long id);
    CustomerDTO createClient(CustomerDTO customerDTO);
    CustomerDTO updateClient(Long id, CustomerDTO customerDTO);
    boolean deleteClient(Long id);

    // Bank account management
    List<BankAccountDTO> getAllAccounts();
    BankAccountDTO getAccount(Long id);
    BankAccountDTO createAccount(BankAccountRequestDTO accountDTO);
    BankAccountDTO updateAccount(Long id, BankAccountDTO bankAccountDTO);
    boolean deleteAccount(Long id);

    // Operations
    void debit(Long accountId, CreditDebitRequestDTO requestDTO);
    void credit(Long accountId, CreditDebitRequestDTO requestDTO);
    void transfer(TransferRequestDTO transferRequestDTO);
    List<AccountOperationDTO> getAccountOperations(Long accountId);
}