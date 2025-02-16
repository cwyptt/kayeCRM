package io.github.cwyptt.crm.controller;

import io.github.cwyptt.crm.dto.CustomerDto;
import io.github.cwyptt.crm.service.CustomerService;
import io.github.cwyptt.crm.utility.exception.CustomerHasAssociatedContactsException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.github.cwyptt.crm.utility.ErrorResponse;

import java.util.List;

@RestController
@RequestMapping("${api.base.url}/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerDto> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return new ResponseEntity<>(customerService.createCustomer(customerDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, customerDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomer(id));
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.ok().build();
        } catch (CustomerHasAssociatedContactsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("CUSTOMER_HAS_CONTACTS", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred while trying to delete this customer"));
        }
    }

    // Additional mappings
    @GetMapping("/search")
    public ResponseEntity<List<CustomerDto>> searchCustomers(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String company) {
        return ResponseEntity.ok(
                customerService.searchCustomers(firstName, lastName, email, phone, company));
    }
}
