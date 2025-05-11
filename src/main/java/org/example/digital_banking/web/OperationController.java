package org.example.digital_banking.web;

import org.example.digital_banking.dtos.*;
import org.example.digital_banking.exceptions.BankAccountNotFoundException;
import org.example.digital_banking.exceptions.InsufficientBalanceException;
import org.example.digital_banking.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comptes")
public class OperationController {

    private final CustomerService customerService;

    public OperationController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // POST /comptes/debit — effectuer un débit
    @PostMapping("/debit")
    public ResponseEntity<Void> debit(@RequestBody CreditDebitRequestDTO requestDTO) {
        try {
            customerService.debit(requestDTO.getAccountId(), requestDTO);
            return ResponseEntity.ok().build();
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // POST /comptes/credit — effectuer un crédit
    @PostMapping("/credit")
    public ResponseEntity<Void> credit(@RequestBody CreditDebitRequestDTO requestDTO) {
        try {
            customerService.credit(requestDTO.getAccountId(), requestDTO);
            return ResponseEntity.ok().build();
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /comptes/transfer — effectuer un virement
    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody TransferRequestDTO transferRequestDTO) {
        try {
            customerService.transfer(transferRequestDTO);
            return ResponseEntity.ok().build();
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientBalanceException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // GET /comptes/{accountId}/operations — afficher l'historique des opérations
    @GetMapping("/{accountId}/operations")
    public ResponseEntity<List<AccountOperationDTO>> getAccountOperations(
            @PathVariable Long accountId) {
        try {
            List<AccountOperationDTO> operations = customerService.getAccountOperations(accountId);
            return ResponseEntity.ok(operations);
        } catch (BankAccountNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}