package org.example.digital_banking.web;

import org.example.digital_banking.entities.BankAccount;
import org.example.digital_banking.services.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RestController
@RequestMapping("/comptes")
public class CompteController {

    private final CustomerService customerService;

    public CompteController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // GET /comptes — lister tous les comptes
    @GetMapping
    public List<BankAccount> getAllAccounts() {
        return customerService.getAllAccounts();
    }

    // GET /comptes/{id} — afficher un compte spécifique
    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getAccountById(@PathVariable String id) {
        BankAccount account = customerService.getAccount(id);
        return account != null ? ResponseEntity.ok(account) : ResponseEntity.notFound().build();
    }

    // POST /clients/{clientId}/comptes — créer un compte pour un client
    @PostMapping("/clients/{clientId}/comptes")
    public ResponseEntity<BankAccount> createAccountForCustomer(@PathVariable Long clientId, @RequestBody BankAccount account) {
        BankAccount newAccount = customerService.createAccountForCustomer(clientId, account);
        return ResponseEntity.ok(newAccount);
    }

    // PUT /comptes/{id} — modifier un compte
    @PutMapping("/{id}")
    public ResponseEntity<BankAccount> updateAccount(@PathVariable String id, @RequestBody BankAccount updatedAccount) {
        BankAccount account = customerService.updateAccount(id, updatedAccount);
        return account != null ? ResponseEntity.ok(account) : ResponseEntity.notFound().build();
    }

    // DELETE /comptes/{id} — supprimer un compte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        boolean deleted = customerService.deleteAccount(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
