package org.example.digital_banking;

import org.example.digital_banking.entities.BankAccount;
import org.example.digital_banking.entities.CurrentAccount;
import org.example.digital_banking.entities.Customer;
import org.example.digital_banking.entities.SavingAccount;
import org.example.digital_banking.enums.AccountStatus;
import org.example.digital_banking.enums.Operation_type;
import org.example.digital_banking.repositories.BankAccountRepo;
import org.example.digital_banking.repositories.CustomerRepo;
import org.example.digital_banking.repositories.OperationRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class DigitalBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }


    @Bean
    public CommandLineRunner commandLineRunner(CustomerRepo customerRepo, BankAccountRepo bankAccountRepo, OperationRepo operationRepo) {
        return args -> {
            for (int i = 0; i < 10; i++) {
                Customer customer = Customer.builder()
                        .phone("0654823654"+"000"+i)
                        .email(i+"abc@gmail.com")
                        .city("Mohammedia"+i)
                        .name("youssef"+i)
                        .build();
                customerRepo.save(customer);
                if(i%2==0)
                {
                    SavingAccount savingAccount = SavingAccount.builder()
                            .currency("MAD")
                            .status(AccountStatus.ACTIVE)
                            .customer(customer)
                            .interestRate(2.5 * i)
                            .balance(Math.random() * 10000)
                            .build();
                    bankAccountRepo.save(savingAccount);


                }
                else{
                    CurrentAccount currentAccount = CurrentAccount.builder()
                            .currency("MAD")
                            .status(AccountStatus.ACTIVE)
                            .customer(customer)
                            .overdraft(5000.0 * i)
                            .balance(Math.random() * 10000)
                            .build();
                    bankAccountRepo.save(currentAccount);
                }


            }

        };
    }
}
