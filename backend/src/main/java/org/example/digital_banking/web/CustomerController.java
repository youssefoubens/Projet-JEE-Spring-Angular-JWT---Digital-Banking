package org.example.digital_banking.web;

import org.example.digital_banking.dtos.CustomerDTO;
import org.example.digital_banking.exceptions.CustomerNotFoundException;
import org.example.digital_banking.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // GET /customers — lister tous les clients
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllClients();
        return ResponseEntity.ok(customers);
    }

    // GET /customers/{id} — afficher un client
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        try {
            CustomerDTO customer = customerService.getClientById(id);
            return ResponseEntity.ok(customer);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST /customers — créer un nouveau client
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            CustomerDTO savedCustomer = customerService.createClient(customerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // PUT /customers/{id} — modifier un client
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerDTO customerDTO) {
        try {
            CustomerDTO updatedCustomer = customerService.updateClient(id, customerDTO);
            return ResponseEntity.ok(updatedCustomer);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /customers/{id} — supprimer un client
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteClient(id);
            return ResponseEntity.noContent().build();
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}