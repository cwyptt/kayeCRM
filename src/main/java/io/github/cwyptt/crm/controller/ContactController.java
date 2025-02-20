package io.github.cwyptt.crm.controller;

import io.github.cwyptt.crm.dto.ContactDto;
import io.github.cwyptt.crm.service.ContactService;
import io.github.cwyptt.crm.utility.ErrorResponse;
import io.github.cwyptt.crm.utility.exception.ContactHasCustomerRelationshipException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("${api.base.url}/contacts")
public class ContactController {
    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @PostMapping
    public ResponseEntity<ContactDto> createContact(@Valid @RequestBody ContactDto contactDto) {
        return new ResponseEntity<>(contactService.createContact(contactDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDto> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody ContactDto contactDto) {
        return ResponseEntity.ok(contactService.updateContact(id, contactDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDto> getContact(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContact(id));
    }

    @GetMapping
    public ResponseEntity<List<ContactDto>> getAllContacts() {
        return ResponseEntity.ok(contactService.getAllContacts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        try {
            contactService.deleteContact(id);
            return ResponseEntity.noContent().build();
        } catch (ContactHasCustomerRelationshipException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("CONTACT_HAS_CUSTOMERS",
                            "This contact cannot be deleted because they are a customer. Please deactivate their customer relationship first."));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_ERROR", e.getMessage()));
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<ContactDto>> getContactsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(contactService.getContactsByCompany(companyId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ContactDto>> searchContacts(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Boolean isCustomer
    ) {
        return ResponseEntity.ok(
                contactService.searchContacts(firstName, lastName, email, phone, position, department, companyId, isCustomer));
    }
}

