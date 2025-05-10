package org.example.digital_banking.web;

import org.example.digital_banking.entities.Customer;

import org.example.digital_banking.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService clientService;

    public CustomerController(CustomerService clientService) {
        this.clientService = clientService;
    }

    // GET /customers — lister tous les clients
    @GetMapping
    public List<Customer> getAllCustomers() {
        return clientService.getAllClients();
    }

    // GET /customers/{id} — afficher un client
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = clientService.getClientById(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    // POST /customers — créer un nouveau client
    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = clientService.createClient(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
    }

    // PUT /customers/{id} — modifier un client
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) {
        Customer updatedCustomer = clientService.updateClient(id, customerDetails);
        return updatedCustomer != null ? ResponseEntity.ok(updatedCustomer) : ResponseEntity.notFound().build();
    }

    // DELETE /customers/{id} — supprimer un client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean deleted = clientService.deleteClient(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
