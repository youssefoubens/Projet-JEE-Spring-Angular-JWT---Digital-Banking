package org.example.digital_banking.services;

import jakarta.transaction.Transactional;
import org.example.digital_banking.entities.BankAccount;
import org.example.digital_banking.entities.Customer;
import org.example.digital_banking.entities.Operation;
import org.example.digital_banking.enums.Operation_type;
import org.example.digital_banking.exceptions.BankAccountNotFoundException;
import org.example.digital_banking.exceptions.CustomerNotFoundException;
import org.example.digital_banking.exceptions.InsufficientBalanceException;
import org.example.digital_banking.repositories.BankAccountRepo;
import org.example.digital_banking.repositories.CustomerRepo;
import org.example.digital_banking.repositories.OperationRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class CustomerService  implements CustomerServiceinterface{
    private final CustomerRepo customerRepo;
    private final BankAccountRepo bankAccountRepo;
    private final OperationRepo operationRepo;

    public CustomerService(CustomerRepo customerRepo, BankAccountRepo bankAccountRepo, OperationRepo operationRepo) {
        this.customerRepo = customerRepo;
        this.bankAccountRepo = bankAccountRepo;
        this.operationRepo = operationRepo;
    }

    public List<Customer> getAllClients() {
        try{
            return customerRepo.findAll();
        }
        catch (Exception e){
          System.out.println(e.getMessage());
          return null;
        }

    }

    public Customer getClientById(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + id));
    }

    public Customer createClient(Customer customer) {
        return customerRepo.save(customer);
    }

    public Customer updateClient(Long id, Customer customerDetails) {
        Customer existingCustomer = getClientById(id);
        existingCustomer.setName(customerDetails.getName());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setPhone(customerDetails.getPhone());
        existingCustomer.setAddress(customerDetails.getAddress());
        existingCustomer.setCity(customerDetails.getCity());
        existingCustomer.setPassword(customerDetails.getPassword());
        return customerRepo.save(existingCustomer);
    }

    public boolean deleteClient(Long id) {
        if (customerRepo.existsById(id)) {
            customerRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public List<BankAccount> getAllAccounts() {
        return bankAccountRepo.findAll();
    }

    public BankAccount getAccount(String id) {
        return bankAccountRepo.findById(Long.valueOf(id))
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found with id: " + id));
    }

    public BankAccount createAccountForCustomer(Long clientId, BankAccount account) {
        Customer customer = getClientById(clientId);
        account.setCustomer(customer);
        return bankAccountRepo.save(account);
    }

    public BankAccount updateAccount(String id, BankAccount updatedAccount) {
        BankAccount existing = getAccount(id);

        existing.setBalance(updatedAccount.getBalance());
        existing.setCurrency(updatedAccount.getCurrency());
        existing.setStatus(updatedAccount.getStatus());
        return bankAccountRepo.save(existing);
    }

    public boolean deleteAccount(String id) {
        Long accountId = Long.valueOf(id);
        if (bankAccountRepo.existsById(accountId)) {
            bankAccountRepo.deleteById(accountId);
            return true;
        }
        return false;
    }

    public void debit(String id, double amount, String description) {
        BankAccount account = getAccount(id);
        if (account.getBalance() < amount)
            throw new InsufficientBalanceException("Insufficient balance for account ID: " + id);
        account.setBalance(account.getBalance() - amount);
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setOperationType(Operation_type.DEBIT);
        operation.setBankAccount(account);
        operationRepo.save(operation);
        bankAccountRepo.save(account);
    }

    public void credit(String id, double amount, String description) {
        BankAccount account = getAccount(id);
        account.setBalance(account.getBalance() + amount);
        Operation operation = new Operation();
        operation.setAmount(amount);
        operation.setOperationType(Operation_type.CREDIT);
        operation.setBankAccount(account);
        operationRepo.save(operation);
        bankAccountRepo.save(account);
    }

    public List<Operation> getAccountOperations(String id) {
        BankAccount account = getAccount(id);
        List<Operation> operations = operationRepo.findByBankAccount(account);
        if (operations.isEmpty()) {
            throw new BankAccountNotFoundException("Account not found with id: " + id);
        }
        return operations;
    }
}
