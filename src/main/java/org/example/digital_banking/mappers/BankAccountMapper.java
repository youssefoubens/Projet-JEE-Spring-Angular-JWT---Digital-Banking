package org.example.digital_banking.mappers;

import org.example.digital_banking.dtos.*;
import org.example.digital_banking.entities.*;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapper {
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setCustomer_id(customer.getCustomer_id());
        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPassword(customer.getPassword());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setCity(customer.getCity());
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setCustomer_id(customerDTO.getCustomer_id());
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPassword(customerDTO.getPassword());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setCity(customerDTO.getCity());
        return customer;
    }

    public SavingAccountDTO fromSavingAccount(SavingAccount savingAccount) {
        SavingAccountDTO savingAccountDTO = new SavingAccountDTO();
        savingAccountDTO.setId(savingAccount.getIdBankAccount());
        savingAccountDTO.setBalance(savingAccount.getBalance());
        savingAccountDTO.setCreatedAt(savingAccount.getCreatedAt());
        savingAccountDTO.setStatus(savingAccount.getStatus().toString());
        savingAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingAccountDTO.setType("SAV");
        savingAccountDTO.setInterestRate(savingAccount.getInterestRate());
        return savingAccountDTO;
    }

    public CurrentAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
        CurrentAccountDTO currentAccountDTO = new CurrentAccountDTO();
        currentAccountDTO.setId(currentAccount.getIdBankAccount());
        currentAccountDTO.setBalance(currentAccount.getBalance());
        currentAccountDTO.setCreatedAt(currentAccount.getCreatedAt());
        currentAccountDTO.setStatus(currentAccount.getStatus().toString());
        currentAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentAccountDTO.setType("CUR");
        currentAccountDTO.setOverdraft(currentAccount.getOverdraft());
        return currentAccountDTO;
    }

    public BankAccountDTO fromBankAccount(BankAccount bankAccount) {
        if (bankAccount instanceof SavingAccount) {
            return fromSavingAccount((SavingAccount) bankAccount);
        } else {
            return fromCurrentAccount((CurrentAccount) bankAccount);
        }
    }

    public AccountOperationDTO fromOperation(Operation operation) {
        AccountOperationDTO operationDTO = new AccountOperationDTO();
        operationDTO.setId(operation.getId());
        operationDTO.setOperationDate(operation.getOperationDate());
        operationDTO.setAmount(operation.getAmount());
        operationDTO.setOperationType(operation.getOperationType());
        operationDTO.setDescription(operation.getDescription());
        return operationDTO;
    }
}