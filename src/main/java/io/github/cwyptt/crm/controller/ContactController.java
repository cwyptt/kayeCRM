package io.github.cwyptt.crm.controller;

import io.github.cwyptt.crm.dto.ContactDto;
import io.github.cwyptt.crm.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.base.url}/contacts")
@RequiredArgsConstructor
public class ContactController {
    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactDto> createContact(@Valid @RequestBody ContactDto contactDto) {
        return new ResponseEntity<>(contactService.createContact(contactDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDto> updateContact(
            @PathVariable Long id,
            @Valid @RequestBody ContactDto contactDto
    ) {
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
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    // Additional mappings
    @GetMapping("/search")
    public ResponseEntity<List<ContactDto>> searchContacts(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String position,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) Long customerId) {
        return ResponseEntity.ok(
                contactService.searchContacts(firstName, lastName, email, phone, position, department, customerId)
        );
    }
}
