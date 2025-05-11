package org.example.digital_banking.services;

import jakarta.transaction.Transactional;
import org.example.digital_banking.dtos.*;
import org.example.digital_banking.entities.*;
import org.example.digital_banking.enums.AccountStatus;
import org.example.digital_banking.enums.Operation_type;
import org.example.digital_banking.exceptions.BankAccountNotFoundException;
import org.example.digital_banking.exceptions.CustomerNotFoundException;
import org.example.digital_banking.exceptions.InsufficientBalanceException;
import org.example.digital_banking.mappers.BankAccountMapper;
import org.example.digital_banking.repositories.BankAccountRepo;
import org.example.digital_banking.repositories.CustomerRepo;
import org.example.digital_banking.repositories.OperationRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class CustomerService implements CustomerServiceinterface {

    private final CustomerRepo customerRepo;
    private final BankAccountRepo bankAccountRepo;
    private final OperationRepo operationRepo;
    private final BankAccountMapper bankAccountMapper;

    public CustomerService(CustomerRepo customerRepo,
                           BankAccountRepo bankAccountRepo,
                           OperationRepo operationRepo,
                           BankAccountMapper bankAccountMapper) {
        this.customerRepo = customerRepo;
        this.bankAccountRepo = bankAccountRepo;
        this.operationRepo = operationRepo;
        this.bankAccountMapper = bankAccountMapper;
    }

    // Customer management methods
    @Override
    public List<CustomerDTO> getAllClients() {
        return customerRepo.findAll().stream()
                .map(bankAccountMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getClientById(Long id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO createClient(CustomerDTO customerDTO) {
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepo.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateClient(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));

        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setCity(customerDTO.getCity());
        customer.setPassword(customerDTO.getPassword());

        Customer updatedCustomer = customerRepo.save(customer);
        return bankAccountMapper.fromCustomer(updatedCustomer);
    }

    @Override
    public boolean deleteClient(Long id) {
        if (!customerRepo.existsById(id)) {
            return false;
        }
        customerRepo.deleteById(id);
        return true;
    }

    // Bank account management methods
    @Override
    public List<BankAccountDTO> getAllAccounts() {
        return bankAccountRepo.findAll().stream()
                .map(account -> {
                    if (account instanceof SavingAccount) {
                        return bankAccountMapper.fromSavingAccount((SavingAccount) account);
                    } else {
                        return bankAccountMapper.fromCurrentAccount((CurrentAccount) account);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getAccount(Long id) {  // Changed from String to Long
        BankAccount account = bankAccountRepo.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found with id: " + id));

        if (account instanceof SavingAccount) {
            return bankAccountMapper.fromSavingAccount((SavingAccount) account);
        } else {
            return bankAccountMapper.fromCurrentAccount((CurrentAccount) account);
        }
    }

    @Override
    public BankAccountDTO createAccount(BankAccountRequestDTO accountDTO) {
        Customer customer = customerRepo.findById(accountDTO.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + accountDTO.getCustomerId()));

        BankAccount account;
        if (accountDTO.getType().equals("SAV")) {
            SavingAccount savingAccount = new SavingAccount();
            savingAccount.setInterestRate(accountDTO.getInterestRate());
            account = savingAccount;
        } else {
            CurrentAccount currentAccount = new CurrentAccount();
            currentAccount.setOverdraft(accountDTO.getOverdraft());
            account = currentAccount;
        }

        account.setBalance(accountDTO.getInitialBalance());
        account.setCustomer(customer);
        account.setCreatedAt(new Date());
        account.setStatus(AccountStatus.ACTIVE);

        BankAccount savedAccount = bankAccountRepo.save(account);
        return bankAccountMapper.fromBankAccount(savedAccount);
    }

    @Override
    public BankAccountDTO updateAccount(Long id, BankAccountDTO bankAccountDTO) {  // Changed from String to Long
        BankAccount account = bankAccountRepo.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found with id: " + id));

        account.setBalance(bankAccountDTO.getBalance());
        account.setStatus(AccountStatus.valueOf(bankAccountDTO.getStatus()));

        if (account instanceof SavingAccount savingAccount && bankAccountDTO instanceof SavingAccountDTO savingAccountDTO) {
            savingAccount.setInterestRate(savingAccountDTO.getInterestRate());
        } else if (account instanceof CurrentAccount currentAccount && bankAccountDTO instanceof CurrentAccountDTO currentAccountDTO) {
            currentAccount.setOverdraft(currentAccountDTO.getOverdraft());
        }

        BankAccount updatedAccount = bankAccountRepo.save(account);
        return bankAccountMapper.fromBankAccount(updatedAccount);
    }


    @Override
    public boolean deleteAccount(Long id) {  // Changed from String to Long
        if (!bankAccountRepo.existsById(id)) {
            return false;
        }

        // First delete all operations associated with this account
        operationRepo.deleteByBankAccountId(id);

        // Then delete the account
        bankAccountRepo.deleteById(id);
        return true;
    }

    // Operation methods
    @Override
    public void debit(Long accountId, CreditDebitRequestDTO requestDTO) {
        if (requestDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }

        BankAccount account = bankAccountRepo.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found with id: " + accountId));

        // Check balance based on account type
        if (account instanceof CurrentAccount currentAccount) {
            if (currentAccount.getBalance() + currentAccount.getOverdraft() < requestDTO.getAmount()) {
                throw new InsufficientBalanceException("Insufficient balance including overdraft");
            }
        } else if (account.getBalance() < requestDTO.getAmount()) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        // Perform debit
        account.setBalance(account.getBalance() - requestDTO.getAmount());

        // Record operation
        Operation operation = new Operation();
        operation.setOperationType(Operation_type.DEBIT);
        operation.setAmount(requestDTO.getAmount());
        operation.setDescription(requestDTO.getDescription());
        operation.setBankAccount(account);
        operation.setOperationDate(new Date());

        operationRepo.save(operation);
        bankAccountRepo.save(account);
    }

    @Override
    public void credit(Long accountId, CreditDebitRequestDTO requestDTO) {
        if (requestDTO.getAmount() <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }

        BankAccount account = bankAccountRepo.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found with id: " + accountId));

        // Perform credit
        account.setBalance(account.getBalance() + requestDTO.getAmount());

        // Record operation
        Operation operation = new Operation();
        operation.setOperationType(Operation_type.CREDIT);
        operation.setAmount(requestDTO.getAmount());
        operation.setDescription(requestDTO.getDescription());
        operation.setBankAccount(account);
        operation.setOperationDate(new Date());

        operationRepo.save(operation);
        bankAccountRepo.save(account);
    }

    @Override
    public void transfer(TransferRequestDTO transferRequestDTO) {
        // Convert String account IDs to Long if needed
        Long sourceAccountId = Long.valueOf(transferRequestDTO.getAccountSource());
        Long destAccountId = Long.valueOf(transferRequestDTO.getAccountDestination());

        // Create debit request
        CreditDebitRequestDTO debitRequest = new CreditDebitRequestDTO();
        debitRequest.setAccountId(sourceAccountId);
        debitRequest.setAmount(transferRequestDTO.getAmount());
        debitRequest.setDescription(transferRequestDTO.getDescription());

        // Create credit request
        CreditDebitRequestDTO creditRequest = new CreditDebitRequestDTO();
        creditRequest.setAccountId(destAccountId);
        creditRequest.setAmount(transferRequestDTO.getAmount());
        creditRequest.setDescription(transferRequestDTO.getDescription());

        // Execute operations
        debit(sourceAccountId, debitRequest);
        credit(destAccountId, creditRequest);
    }

    @Override
    public List<AccountOperationDTO> getAccountOperations(Long accountId) {  // Changed from String to Long
        BankAccount account = bankAccountRepo.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found with id: " + accountId));

        return operationRepo.findByBankAccountId(accountId).stream()
                .map(operation -> bankAccountMapper.fromOperation((Operation) operation))
                .collect(Collectors.toList());
    }
}